package com.cemetery.web.controller;

import com.cemetery.common.result.Result;
import com.cemetery.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Api(tags = "文件上传管理")
@Slf4j
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @ApiOperation("上传单个文件")
    @PostMapping("/file")
    public Result<String> uploadFile(
            @ApiParam("文件") @RequestParam("file") MultipartFile file,
            @ApiParam("文件夹") @RequestParam(value = "folder", required = false) String folder) {
        String fileUrl = fileUploadService.uploadFile(file, folder);
        return Result.success(fileUrl);
    }

    @ApiOperation("上传图片（自动压缩）")
    @PostMapping("/image")
    public Result<String> uploadImage(
            @ApiParam("图片文件") @RequestParam("file") MultipartFile file,
            @ApiParam("文件夹") @RequestParam(value = "folder", required = false, defaultValue = "images") String folder,
            @ApiParam("是否压缩") @RequestParam(value = "compress", required = false, defaultValue = "true") boolean compress,
            @ApiParam("是否转换为WebP") @RequestParam(value = "webp", required = false, defaultValue = "false") boolean webp) {
        String imageUrl = fileUploadService.uploadImage(file, folder, compress, webp);
        return Result.success(imageUrl);
    }

    @ApiOperation("上传图片并生成缩略图")
    @PostMapping("/image/thumbnail")
    public Result<Map<String, String>> uploadImageWithThumbnail(
            @ApiParam("图片文件") @RequestParam("file") MultipartFile file,
            @ApiParam("文件夹") @RequestParam(value = "folder", required = false, defaultValue = "images") String folder) {
        Map<String, String> urls = fileUploadService.uploadImageWithThumbnail(file, folder);
        return Result.success(urls);
    }

    @ApiOperation("上传视频并生成缩略图")
    @PostMapping("/video/thumbnail")
    public Result<Map<String, String>> uploadVideoWithThumbnail(
            @ApiParam("视频文件") @RequestParam("file") MultipartFile file,
            @ApiParam("文件夹") @RequestParam(value = "folder", required = false, defaultValue = "videos") String folder) {
        Map<String, String> urls = fileUploadService.uploadVideoWithThumbnail(file, folder);
        return Result.success(urls);
    }

    @ApiOperation("分块上传 - 初始化")
    @PostMapping("/chunk/init")
    public Result<String> initChunkUpload(
            @ApiParam("文件名") @RequestParam("fileName") String fileName,
            @ApiParam("文件大小") @RequestParam("fileSize") Long fileSize,
            @ApiParam("总分块数") @RequestParam("totalChunks") Integer totalChunks) {
        String uploadId = fileUploadService.initChunkUpload(fileName, fileSize, totalChunks);
        return Result.success(uploadId);
    }

    @ApiOperation("分块上传 - 上传分块")
    @PostMapping("/chunk/upload")
    public Result<Boolean> uploadChunk(
            @ApiParam("上传ID") @RequestParam("uploadId") String uploadId,
            @ApiParam("分块序号") @RequestParam("chunkNumber") Integer chunkNumber,
            @ApiParam("分块文件") @RequestParam("file") MultipartFile chunkFile) {
        boolean success = fileUploadService.uploadChunk(uploadId, chunkNumber, chunkFile);
        return Result.success(success);
    }

    @ApiOperation("分块上传 - 合并分块")
    @PostMapping("/chunk/merge")
    public Result<String> mergeChunks(
            @ApiParam("上传ID") @RequestParam("uploadId") String uploadId,
            @ApiParam("文件夹") @RequestParam(value = "folder", required = false) String folder) {
        String fileUrl = fileUploadService.mergeChunks(uploadId, folder);
        return Result.success(fileUrl);
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public Result<Boolean> deleteFile(
            @ApiParam("文件URL") @RequestParam("fileUrl") String fileUrl) {
        boolean success = fileUploadService.deleteFile(fileUrl);
        return Result.success(success);
    }

    @ApiOperation("批量删除文件")
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteFiles(
            @ApiParam("文件URL列表") @RequestParam("fileUrls") String[] fileUrls) {
        int deletedCount = fileUploadService.batchDeleteFiles(fileUrls);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", fileUrls.length);
        result.put("deleted", deletedCount);
        result.put("failed", fileUrls.length - deletedCount);
        
        return Result.success(result);
    }
}
