package com.cemetery.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 上传单个文件
     *
     * @param file 文件
     * @param folder 文件夹路径
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * 上传图片（自动压缩和格式转换）
     *
     * @param file 图片文件
     * @param folder 文件夹路径
     * @param compress 是否压缩
     * @param convertToWebp 是否转换为WebP格式
     * @return 图片访问URL
     */
    String uploadImage(MultipartFile file, String folder, boolean compress, boolean convertToWebp);

    /**
     * 上传图片并生成缩略图
     *
     * @param file 图片文件
     * @param folder 文件夹路径
     * @return Map包含原图URL和缩略图URL
     */
    Map<String, String> uploadImageWithThumbnail(MultipartFile file, String folder);

    /**
     * 上传视频并生成缩略图
     *
     * @param file 视频文件
     * @param folder 文件夹路径
     * @return Map包含视频URL和缩略图URL
     */
    Map<String, String> uploadVideoWithThumbnail(MultipartFile file, String folder);

    /**
     * 分块上传 - 初始化
     *
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param totalChunks 总分块数
     * @return 上传ID
     */
    String initChunkUpload(String fileName, Long fileSize, Integer totalChunks);

    /**
     * 分块上传 - 上传分块
     *
     * @param uploadId 上传ID
     * @param chunkNumber 分块序号（从1开始）
     * @param chunkFile 分块文件
     * @return 是否成功
     */
    boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile chunkFile);

    /**
     * 分块上传 - 合并分块
     *
     * @param uploadId 上传ID
     * @param folder 文件夹路径
     * @return 文件访问URL
     */
    String mergeChunks(String uploadId, String folder);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否成功
     */
    boolean deleteFile(String fileUrl);

    /**
     * 批量删除文件
     *
     * @param fileUrls 文件URL列表
     * @return 成功删除的数量
     */
    int batchDeleteFiles(String... fileUrls);

    /**
     * 获取文件访问URL
     *
     * @param filePath 文件路径
     * @return 访问URL
     */
    String getFileUrl(String filePath);
}
