package com.cemetery.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {

    /**
     * 上传路径（本地存储）
     */
    private String path = "/data/cemetery/upload";

    /**
     * 访问域名
     */
    private String domain = "http://localhost:8080";

    /**
     * 分块上传临时目录
     */
    private String chunkTempPath = "/data/cemetery/upload/chunks";

    /**
     * 分块大小（MB）
     */
    private Integer chunkSize = 5;

    /**
     * 最大文件大小（MB）
     */
    private Integer maxFileSize = 500;

    /**
     * 图片压缩质量（0-1）
     */
    private Double imageQuality = 0.8;

    /**
     * 图片最大宽度
     */
    private Integer imageMaxWidth = 1920;

    /**
     * 图片最大高度
     */
    private Integer imageMaxHeight = 1080;

    /**
     * 缩略图宽度
     */
    private Integer thumbnailWidth = 300;

    /**
     * 缩略图高度
     */
    private Integer thumbnailHeight = 200;

    /**
     * 视频缩略图截取时间（秒）
     */
    private Integer videoThumbnailTime = 3;

    /**
     * 允许的图片格式
     */
    private List<String> allowedImageFormats = List.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    /**
     * 允许的视频格式
     */
    private List<String> allowedVideoFormats = List.of("mp4", "avi", "mov", "wmv", "flv", "mkv");

    /**
     * 允许的音频格式
     */
    private List<String> allowedAudioFormats = List.of("mp3", "wav", "flac", "aac", "ogg");

    /**
     * 是否启用WebP转换
     */
    private Boolean enableWebpConversion = true;

    /**
     * 存储类型：local, oss, qiniu
     */
    private String storageType = "local";
}
