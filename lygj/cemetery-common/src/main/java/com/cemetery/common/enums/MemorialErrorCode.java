package com.cemetery.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数字纪念空间错误码定义
 */
@Getter
@AllArgsConstructor
public enum MemorialErrorCode {

    // 纪念空间相关 (5000-5099)
    MEMORIAL_NOT_FOUND(5000, "纪念空间不存在"),
    MEMORIAL_ALREADY_EXISTS(5001, "该逝者已创建纪念空间"),
    MEMORIAL_NOT_PUBLISHED(5002, "纪念空间未发布"),
    MEMORIAL_EXPIRED(5003, "纪念空间已过期"),
    MEMORIAL_ACCESS_DENIED(5004, "访问权限验证失败"),
    MEMORIAL_PASSWORD_ERROR(5005, "纪念空间密码错误"),
    MEMORIAL_CREATE_FAILED(5006, "创建纪念空间失败"),
    MEMORIAL_UPDATE_FAILED(5007, "更新纪念空间失败"),
    MEMORIAL_DELETE_FAILED(5008, "删除纪念空间失败"),
    MEMORIAL_PUBLISH_FAILED(5009, "发布纪念空间失败"),

    // 内容管理相关 (5100-5199)
    CONTENT_NOT_FOUND(5100, "内容不存在"),
    CONTENT_TYPE_INVALID(5101, "内容类型无效"),
    CONTENT_SIZE_EXCEEDED(5102, "内容大小超出限制"),
    CONTENT_UPLOAD_FAILED(5103, "内容上传失败"),
    CONTENT_DELETE_FAILED(5104, "内容删除失败"),
    CONTENT_AUDIT_FAILED(5105, "内容审核失败"),
    CONTENT_FORMAT_INVALID(5106, "内容格式不支持"),

    // 留言相关 (5200-5299)
    MESSAGE_NOT_FOUND(5200, "留言不存在"),
    MESSAGE_SEND_FAILED(5201, "留言发送失败"),
    MESSAGE_DELETE_FAILED(5202, "留言删除失败"),
    MESSAGE_AUDIT_FAILED(5203, "留言审核失败"),
    MESSAGE_REPLY_FAILED(5204, "留言回复失败"),
    MESSAGE_CONTENT_INVALID(5205, "留言内容不合法"),
    MESSAGE_TOO_FREQUENT(5206, "留言过于频繁，请稍后再试"),

    // 互动相关 (5300-5399)
    INTERACTION_FAILED(5300, "互动操作失败"),
    CANDLE_LIGHT_FAILED(5301, "点蜡烛失败"),
    FLOWER_OFFER_FAILED(5302, "献花失败"),
    INCENSE_OFFER_FAILED(5303, "上香失败"),
    INTERACTION_LIMIT_EXCEEDED(5304, "今日互动次数已达上限"),

    // 搜索相关 (5400-5499)
    SEARCH_FAILED(5400, "搜索失败"),
    SEARCH_KEYWORD_INVALID(5401, "搜索关键词无效"),
    SEARCH_RESULT_EMPTY(5402, "未找到相关结果"),
    SEARCH_INDEX_ERROR(5403, "搜索索引异常"),

    // 统计相关 (5500-5599)
    STATISTICS_QUERY_FAILED(5500, "统计查询失败"),
    STATISTICS_DATA_ERROR(5501, "统计数据异常"),
    STATISTICS_EXPORT_FAILED(5502, "统计数据导出失败");

    private final int code;
    private final String message;
}
