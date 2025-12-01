package com.cemetery.domain.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 数字纪念空间Elasticsearch文档
 */
@Data
@Document(indexName = "memorial", createIndex = true)
public class MemorialDocument {

    @Id
    private Long id;

    /**
     * 纪念空间编号
     */
    @Field(type = FieldType.Keyword)
    private String spaceNo;

    /**
     * 纪念空间名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String spaceName;

    /**
     * 逝者姓名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String deceasedName;

    /**
     * 生平介绍
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String biography;

    /**
     * 生平成就
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String lifeAchievements;

    /**
     * 家属寄语
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String familyWords;

    /**
     * 访问权限
     */
    @Field(type = FieldType.Integer)
    private Integer accessPermission;

    /**
     * 背景主题
     */
    @Field(type = FieldType.Keyword)
    private String backgroundTheme;

    /**
     * 访问次数
     */
    @Field(type = FieldType.Integer)
    private Integer visitCount;

    /**
     * 点烛次数
     */
    @Field(type = FieldType.Integer)
    private Integer candleCount;

    /**
     * 献花次数
     */
    @Field(type = FieldType.Integer)
    private Integer flowerCount;

    /**
     * 上香次数
     */
    @Field(type = FieldType.Integer)
    private Integer incenseCount;

    /**
     * 留言次数
     */
    @Field(type = FieldType.Integer)
    private Integer messageCount;

    /**
     * 是否已发布
     */
    @Field(type = FieldType.Integer)
    private Integer isPublished;

    /**
     * 发布时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

    /**
     * 墓位编号
     */
    @Field(type = FieldType.Keyword)
    private String tombNo;

    /**
     * 逝者照片
     */
    @Field(type = FieldType.Keyword)
    private String deceasedPhoto;

    /**
     * 综合评分（用于排序）
     */
    @Field(type = FieldType.Double)
    private Double score;
}
