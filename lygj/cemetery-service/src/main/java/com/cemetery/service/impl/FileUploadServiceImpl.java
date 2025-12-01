package com.cemetery.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cemetery.common.config.FileUploadConfig;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 文件上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileUploadConfig fileConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CHUNK_UPLOAD_KEY = "chunk:upload:";
    private static final long CHUNK_UPLOAD_EXPIRE = 24; // 24小时过期

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.extName(originalFilename);
            String fileName = IdUtil.simpleUUID() + "." + extension;

            // 构建文件路径
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = StrUtil.isBlank(folder) ? datePath : folder + "/" + datePath;
            String fullPath = fileConfig.getPath() + "/" + relativePath;

            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 保存文件
            Path filePath = Paths.get(fullPath, fileName);
            file.transferTo(filePath.toFile());

            log.info("文件上传成功: {}", filePath);
            return getFileUrl(relativePath + "/" + fileName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadImage(MultipartFile file, String folder, boolean compress, boolean convertToWebp) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("图片文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);

        // 验证图片格式
        if (!fileConfig.getAllowedImageFormats().contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的图片格式: " + extension);
        }

        try {
            // 生成文件名
            String outputExtension = (convertToWebp && fileConfig.getEnableWebpConversion()) ? "webp" : extension;
            String fileName = IdUtil.simpleUUID() + "." + outputExtension;

            // 构建文件路径
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = StrUtil.isBlank(folder) ? datePath : folder + "/" + datePath;
            String fullPath = fileConfig.getPath() + "/" + relativePath;

            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 处理图片
            Path outputPath = Paths.get(fullPath, fileName);
            BufferedImage image = ImageIO.read(file.getInputStream());

            Thumbnails.Builder<BufferedImage> builder = Thumbnails.of(image);

            // 压缩处理
            if (compress) {
                int width = image.getWidth();
                int height = image.getHeight();
                
                // 如果超过最大尺寸，按比例缩放
                if (width > fileConfig.getImageMaxWidth() || height > fileConfig.getImageMaxHeight()) {
                    builder.size(fileConfig.getImageMaxWidth(), fileConfig.getImageMaxHeight());
                } else {
                    builder.scale(1.0);
                }
                
                builder.outputQuality(fileConfig.getImageQuality());
            } else {
                builder.scale(1.0);
            }

            // 输出格式
            if (convertToWebp && fileConfig.getEnableWebpConversion()) {
                builder.outputFormat("webp");
            } else {
                builder.outputFormat(extension);
            }

            builder.toFile(outputPath.toFile());

            log.info("图片上传成功: {}, 压缩: {}, WebP: {}", outputPath, compress, convertToWebp);
            return getFileUrl(relativePath + "/" + fileName);
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new BusinessException("图片上传失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> uploadImageWithThumbnail(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("图片文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);

        // 验证图片格式
        if (!fileConfig.getAllowedImageFormats().contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的图片格式: " + extension);
        }

        try {
            // 生成文件名
            String uuid = IdUtil.simpleUUID();
            String originalFileName = uuid + "." + extension;
            String thumbnailFileName = uuid + "_thumb." + extension;

            // 构建文件路径
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = StrUtil.isBlank(folder) ? datePath : folder + "/" + datePath;
            String fullPath = fileConfig.getPath() + "/" + relativePath;

            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            BufferedImage image = ImageIO.read(file.getInputStream());

            // 保存原图（压缩）
            Path originalPath = Paths.get(fullPath, originalFileName);
            Thumbnails.of(image)
                    .size(fileConfig.getImageMaxWidth(), fileConfig.getImageMaxHeight())
                    .outputQuality(fileConfig.getImageQuality())
                    .toFile(originalPath.toFile());

            // 生成缩略图
            Path thumbnailPath = Paths.get(fullPath, thumbnailFileName);
            Thumbnails.of(image)
                    .size(fileConfig.getThumbnailWidth(), fileConfig.getThumbnailHeight())
                    .outputQuality(0.7)
                    .toFile(thumbnailPath.toFile());

            Map<String, String> result = new HashMap<>();
            result.put("original", getFileUrl(relativePath + "/" + originalFileName));
            result.put("thumbnail", getFileUrl(relativePath + "/" + thumbnailFileName));

            log.info("图片及缩略图上传成功: 原图={}, 缩略图={}", originalPath, thumbnailPath);
            return result;
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new BusinessException("图片上传失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> uploadVideoWithThumbnail(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("视频文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);

        // 验证视频格式
        if (!fileConfig.getAllowedVideoFormats().contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的视频格式: " + extension);
        }

        try {
            // 生成文件名
            String uuid = IdUtil.simpleUUID();
            String videoFileName = uuid + "." + extension;
            String thumbnailFileName = uuid + "_thumb.jpg";

            // 构建文件路径
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = StrUtil.isBlank(folder) ? datePath : folder + "/" + datePath;
            String fullPath = fileConfig.getPath() + "/" + relativePath;

            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 保存视频文件
            Path videoPath = Paths.get(fullPath, videoFileName);
            file.transferTo(videoPath.toFile());

            // 生成视频缩略图
            Path thumbnailPath = Paths.get(fullPath, thumbnailFileName);
            generateVideoThumbnail(videoPath.toString(), thumbnailPath.toString());

            Map<String, String> result = new HashMap<>();
            result.put("video", getFileUrl(relativePath + "/" + videoFileName));
            result.put("thumbnail", getFileUrl(relativePath + "/" + thumbnailFileName));

            log.info("视频及缩略图上传成功: 视频={}, 缩略图={}", videoPath, thumbnailPath);
            return result;
        } catch (IOException e) {
            log.error("视频上传失败", e);
            throw new BusinessException("视频上传失败: " + e.getMessage());
        }
    }

    /**
     * 生成视频缩略图
     */
    private void generateVideoThumbnail(String videoPath, String thumbnailPath) {
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(videoPath);
            grabber.start();

            // 跳转到指定时间（秒）
            int frameToGrab = (int) Math.min(fileConfig.getVideoThumbnailTime() * grabber.getFrameRate(), 
                                      grabber.getLengthInFrames() - 1);
            grabber.setFrameNumber(frameToGrab);

            // 抓取帧
            Frame frame = grabber.grabImage();
            if (frame != null) {
                // 转换为BufferedImage
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.convert(frame);

                // 生成缩略图
                Thumbnails.of(bufferedImage)
                        .size(fileConfig.getThumbnailWidth(), fileConfig.getThumbnailHeight())
                        .outputQuality(0.8)
                        .toFile(thumbnailPath);

                converter.close();
            }

            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            log.error("生成视频缩略图失败: {}", videoPath, e);
            if (grabber != null) {
                try {
                    grabber.stop();
                    grabber.release();
                } catch (Exception ignored) {
                }
            }
            throw new BusinessException("生成视频缩略图失败");
        }
    }

    @Override
    public String initChunkUpload(String fileName, Long fileSize, Integer totalChunks) {
        // 生成上传ID
        String uploadId = IdUtil.simpleUUID();
        
        // 创建临时目录
        String chunkDir = fileConfig.getChunkTempPath() + "/" + uploadId;
        Path directory = Paths.get(chunkDir);
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new BusinessException("创建分块上传目录失败");
        }

        // 保存上传信息到Redis
        Map<String, Object> uploadInfo = new HashMap<>();
        uploadInfo.put("fileName", fileName);
        uploadInfo.put("fileSize", fileSize);
        uploadInfo.put("totalChunks", totalChunks);
        uploadInfo.put("uploadedChunks", new HashSet<Integer>());
        uploadInfo.put("chunkDir", chunkDir);
        
        redisTemplate.opsForHash().putAll(CHUNK_UPLOAD_KEY + uploadId, uploadInfo);
        redisTemplate.expire(CHUNK_UPLOAD_KEY + uploadId, CHUNK_UPLOAD_EXPIRE, TimeUnit.HOURS);

        log.info("分块上传初始化成功: uploadId={}, fileName={}, totalChunks={}", 
                uploadId, fileName, totalChunks);
        return uploadId;
    }

    @Override
    public boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile chunkFile) {
        // 获取上传信息
        Map<Object, Object> uploadInfo = redisTemplate.opsForHash().entries(CHUNK_UPLOAD_KEY + uploadId);
        if (uploadInfo.isEmpty()) {
            throw new BusinessException("上传会话已过期，请重新上传");
        }

        String chunkDir = (String) uploadInfo.get("chunkDir");
        
        try {
            // 保存分块文件
            Path chunkPath = Paths.get(chunkDir, String.valueOf(chunkNumber));
            chunkFile.transferTo(chunkPath.toFile());

            // 更新已上传分块列表
            @SuppressWarnings("unchecked")
            Set<Integer> uploadedChunks = (Set<Integer>) uploadInfo.get("uploadedChunks");
            if (uploadedChunks == null) {
                uploadedChunks = new HashSet<>();
            }
            uploadedChunks.add(chunkNumber);
            redisTemplate.opsForHash().put(CHUNK_UPLOAD_KEY + uploadId, "uploadedChunks", uploadedChunks);

            log.debug("分块上传成功: uploadId={}, chunkNumber={}", uploadId, chunkNumber);
            return true;
        } catch (IOException e) {
            log.error("分块上传失败: uploadId={}, chunkNumber={}", uploadId, chunkNumber, e);
            return false;
        }
    }

    @Override
    public String mergeChunks(String uploadId, String folder) {
        // 获取上传信息
        Map<Object, Object> uploadInfo = redisTemplate.opsForHash().entries(CHUNK_UPLOAD_KEY + uploadId);
        if (uploadInfo.isEmpty()) {
            throw new BusinessException("上传会话已过期");
        }

        String fileName = (String) uploadInfo.get("fileName");
        Integer totalChunks = (Integer) uploadInfo.get("totalChunks");
        String chunkDir = (String) uploadInfo.get("chunkDir");

        try {
            // 生成最终文件名和路径
            String extension = FileUtil.extName(fileName);
            String finalFileName = IdUtil.simpleUUID() + "." + extension;
            
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = StrUtil.isBlank(folder) ? datePath : folder + "/" + datePath;
            String fullPath = fileConfig.getPath() + "/" + relativePath;

            // 创建目录
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 合并分块
            Path finalFilePath = Paths.get(fullPath, finalFileName);
            try (FileOutputStream fos = new FileOutputStream(finalFilePath.toFile())) {
                for (int i = 1; i <= totalChunks; i++) {
                    Path chunkPath = Paths.get(chunkDir, String.valueOf(i));
                    if (!Files.exists(chunkPath)) {
                        throw new BusinessException("分块文件缺失: " + i);
                    }
                    
                    try (FileInputStream fis = new FileInputStream(chunkPath.toFile())) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            // 清理临时文件和Redis数据
            FileUtil.del(chunkDir);
            redisTemplate.delete(CHUNK_UPLOAD_KEY + uploadId);

            log.info("分块合并成功: uploadId={}, fileName={}", uploadId, finalFileName);
            return getFileUrl(relativePath + "/" + finalFileName);
        } catch (IOException e) {
            log.error("分块合并失败: uploadId={}", uploadId, e);
            throw new BusinessException("分块合并失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return false;
        }

        try {
            // 从URL提取文件路径
            String filePath = fileUrl.replace(fileConfig.getDomain(), "");
            Path path = Paths.get(fileConfig.getPath() + filePath);
            
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("文件删除成功: {}", path);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public int batchDeleteFiles(String... fileUrls) {
        if (fileUrls == null || fileUrls.length == 0) {
            return 0;
        }

        int count = 0;
        for (String fileUrl : fileUrls) {
            if (deleteFile(fileUrl)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String getFileUrl(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }
        
        // 移除开头的斜杠（如果有）
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        
        return fileConfig.getDomain() + "/upload/" + filePath;
    }
}
