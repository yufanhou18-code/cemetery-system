package com.cemetery.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemetery.domain.entity.CemeteryNotice;
import com.cemetery.domain.enums.NoticePriorityEnum;
import com.cemetery.domain.enums.NoticeStatusEnum;
import com.cemetery.domain.enums.NoticeTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 公告通知Mapper接口
 */
@Mapper
public interface CemeteryNoticeMapper extends BaseMapper<CemeteryNotice> {

    /**
     * 根据公告类型查询
     * @param noticeType 公告类型
     * @return 公告列表
     */
    List<CemeteryNotice> selectByNoticeType(@Param("noticeType") NoticeTypeEnum noticeType);

    /**
     * 根据状态查询公告
     * @param status 状态
     * @return 公告列表
     */
    List<CemeteryNotice> selectByStatus(@Param("status") NoticeStatusEnum status);

    /**
     * 根据优先级查询
     * @param priority 优先级
     * @return 公告列表
     */
    List<CemeteryNotice> selectByPriority(@Param("priority") NoticePriorityEnum priority);

    /**
     * 查询已发布的公告
     * @return 已发布公告列表
     */
    List<CemeteryNotice> selectPublishedNotices();

    /**
     * 查询有效期内的公告
     * @param currentDate 当前日期
     * @return 有效公告列表
     */
    List<CemeteryNotice> selectValidNotices(@Param("currentDate") Date currentDate);

    /**
     * 根据标题模糊查询
     * @param title 标题关键字
     * @return 公告列表
     */
    List<CemeteryNotice> selectByTitleLike(@Param("title") String title);

    /**
     * 查询重要公告
     * @return 重要公告列表
     */
    List<CemeteryNotice> selectImportantNotices();

    /**
     * 增加阅读次数
     * @param id 公告ID
     */
    void incrementViewCount(@Param("id") Long id);
}
