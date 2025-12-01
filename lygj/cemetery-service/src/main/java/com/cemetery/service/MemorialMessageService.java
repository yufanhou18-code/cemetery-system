package com.cemetery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.domain.dto.MemorialMessageDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.MemorialMessageVO;

import java.util.List;

/**
 * 追思留言服务接口
 */
public interface MemorialMessageService {

    /**
     * 发送追思留言
     */
    Long sendMessage(MemorialMessageDTO messageDTO);

    /**
     * 回复留言
     */
    Long replyMessage(MemorialMessageDTO messageDTO);

    /**
     * 删除留言
     */
    void deleteMessage(Long id);

    /**
     * 获取留言详情
     */
    MemorialMessageVO getMessageById(Long id);

    /**
     * 获取纪念空间的留言列表
     */
    List<MemorialMessageVO> getMemorialMessages(Long memorialId, Integer messageType);

    /**
     * 分页查询留言
     */
    Page<MemorialMessageVO> pageMemorialMessages(Long memorialId, Integer messageType, PageQueryDTO pageQueryDTO);

    /**
     * 获取留言的所有回复
     */
    List<MemorialMessageVO> getMessageReplies(Long id);

    /**
     * 点赞留言
     */
    void likeMessage(Long id);

    /**
     * 置顶留言
     */
    void pinMessage(Long id);

    /**
     * 取消置顶留言
     */
    void unpinMessage(Long id);

    /**
     * 审核留言
     */
    void auditMessage(Long id, Integer status, String remark);

    /**
     * 获取最新留言
     */
    List<MemorialMessageVO> getRecentMessages(Integer limit);

    /**
     * 获取留言统计信息
     */
    Object getMessageStatistics(Long memorialId);

    /**
     * 批量删除留言
     */
    void batchDeleteMessages(List<Long> ids);

    /**
     * 批量审核留言
     */
    void batchAuditMessages(List<Long> ids, Integer status);
}
