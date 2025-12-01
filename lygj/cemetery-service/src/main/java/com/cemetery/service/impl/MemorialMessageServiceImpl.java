package com.cemetery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.exception.BusinessException;
import com.cemetery.domain.dto.MemorialMessageDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.entity.MemorialMessage;
import com.cemetery.domain.enums.AuditStatusEnum;
import com.cemetery.domain.enums.MessageTypeEnum;
import com.cemetery.domain.mapper.MemorialMessageMapper;
import com.cemetery.domain.vo.MemorialMessageVO;
import com.cemetery.service.MemorialMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 追思留言服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemorialMessageServiceImpl implements MemorialMessageService {

    private final MemorialMessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(MemorialMessageDTO messageDTO) {
        log.info("发送追思留言, memorialId={}, authorName={}", 
                messageDTO.getMemorialId(), messageDTO.getAuthorName());

        MemorialMessage message = new MemorialMessage();
        BeanUtils.copyProperties(messageDTO, message);
        
        // 设置默认值
        if (message.getMessageType() == null) {
            message.setMessageType(MessageTypeEnum.NORMAL); // 普通留言
        }
        if (message.getIsAnonymous() == null) {
            message.setIsAnonymous(0);
        }
        message.setLikeCount(0);
        message.setIsPinned(0);
        message.setAuditStatus(AuditStatusEnum.APPROVED); // 默认审核通过

        messageMapper.insert(message);
        log.info("发送追思留言成功, messageId={}", message.getId());
        
        return message.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long replyMessage(MemorialMessageDTO messageDTO) {
        log.info("回复留言, memorialId={}, parentId={}, replyToId={}", 
                messageDTO.getMemorialId(), messageDTO.getParentId(), messageDTO.getReplyToId());

        // 校验父留言是否存在
        if (messageDTO.getParentId() != null) {
            MemorialMessage parentMessage = messageMapper.selectById(messageDTO.getParentId());
            if (parentMessage == null) {
                throw new BusinessException("父留言不存在");
            }
        }

        MemorialMessage message = new MemorialMessage();
        BeanUtils.copyProperties(messageDTO, message);
        
        // 设置默认值
        if (message.getMessageType() == null) {
            message.setMessageType(MessageTypeEnum.NORMAL);
        }
        if (message.getIsAnonymous() == null) {
            message.setIsAnonymous(0);
        }
        message.setLikeCount(0);
        message.setIsPinned(0);
        message.setAuditStatus(AuditStatusEnum.APPROVED);

        messageMapper.insert(message);
        log.info("回复留言成功, messageId={}", message.getId());
        
        return message.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        log.info("删除留言, messageId={}", id);

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        // 删除留言及其所有回复
        messageMapper.deleteById(id);
        
        // 删除子留言
        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getParentId, id);
        messageMapper.delete(wrapper);

        log.info("删除留言成功, messageId={}", id);
    }

    @Override
    public MemorialMessageVO getMessageById(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        return convertToVO(message);
    }

    @Override
    public List<MemorialMessageVO> getMemorialMessages(Long memorialId, Integer messageType) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getMemorialId, memorialId);
        wrapper.isNull(MemorialMessage::getParentId); // 只查询顶级留言
        
        if (messageType != null) {
            wrapper.eq(MemorialMessage::getMessageType, messageType);
        }
        
        wrapper.eq(MemorialMessage::getAuditStatus, AuditStatusEnum.APPROVED); // 只查询审核通过的
        wrapper.orderByDesc(MemorialMessage::getIsPinned);
        wrapper.orderByDesc(MemorialMessage::getCreateTime);

        List<MemorialMessage> messages = messageMapper.selectList(wrapper);
        
        // 构建树形结构
        return messages.stream()
                .map(this::buildMessageTree)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MemorialMessageVO> pageMemorialMessages(Long memorialId, Integer messageType, PageQueryDTO pageQueryDTO) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        Page<MemorialMessage> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        
        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getMemorialId, memorialId);
        wrapper.isNull(MemorialMessage::getParentId);
        
        if (messageType != null) {
            wrapper.eq(MemorialMessage::getMessageType, messageType);
        }
        
        wrapper.eq(MemorialMessage::getAuditStatus, 1);
        wrapper.orderByDesc(MemorialMessage::getIsPinned);
        wrapper.orderByDesc(MemorialMessage::getCreateTime);

        Page<MemorialMessage> messagePage = messageMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<MemorialMessageVO> voPage = new Page<>(messagePage.getCurrent(), 
                messagePage.getSize(), messagePage.getTotal());
        voPage.setRecords(messagePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    @Override
    public List<MemorialMessageVO> getMessageReplies(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getParentId, id);
        wrapper.eq(MemorialMessage::getAuditStatus, 1);
        wrapper.orderByAsc(MemorialMessage::getCreateTime);

        List<MemorialMessage> replies = messageMapper.selectList(wrapper);
        return replies.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeMessage(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        log.info("点赞留言, messageId={}", id);

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        message.setLikeCount(message.getLikeCount() + 1);
        messageMapper.updateById(message);
        
        log.info("点赞留言成功, messageId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pinMessage(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        log.info("置顶留言, messageId={}", id);

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        message.setIsPinned(1);
        messageMapper.updateById(message);
        
        log.info("置顶留言成功, messageId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpinMessage(Long id) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        log.info("取消置顶留言, messageId={}", id);

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        message.setIsPinned(0);
        messageMapper.updateById(message);
        
        log.info("取消置顶留言成功, messageId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditMessage(Long id, Integer status, String remark) {
        if (id == null) {
            throw new BusinessException("留言ID不能为空");
        }

        log.info("审核留言, messageId={}, status={}", id, status);

        MemorialMessage message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }

        message.setAuditStatus(AuditStatusEnum.getByCode(status));
        message.setAuditRemark(remark);
        messageMapper.updateById(message);
        
        log.info("审核留言成功, messageId={}", id);
    }

    @Override
    public List<MemorialMessageVO> getRecentMessages(Integer limit) {
        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getAuditStatus, 1);
        wrapper.orderByDesc(MemorialMessage::getCreateTime);
        wrapper.last("LIMIT " + limit);

        List<MemorialMessage> messages = messageMapper.selectList(wrapper);
        return messages.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Object getMessageStatistics(Long memorialId) {
        if (memorialId == null) {
            throw new BusinessException("纪念空间ID不能为空");
        }

        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getMemorialId, memorialId);
        
        List<MemorialMessage> messages = messageMapper.selectList(wrapper);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCount", messages.size());
        
        // 按类型统计
        Map<MessageTypeEnum, Long> typeCount = messages.stream()
                .collect(Collectors.groupingBy(MemorialMessage::getMessageType, Collectors.counting()));
        
        // 转换为Integer的Map
        Map<Integer, Long> typeCountMap = new HashMap<>();
        for (Map.Entry<MessageTypeEnum, Long> entry : typeCount.entrySet()) {
            typeCountMap.put(entry.getKey().getCode(), entry.getValue());
        }
        statistics.put("typeCount", typeCountMap);
        
        // 审核状态统计
        Map<AuditStatusEnum, Long> auditCount = messages.stream()
                .collect(Collectors.groupingBy(MemorialMessage::getAuditStatus, Collectors.counting()));
        
        // 转换为Integer的Map
        Map<Integer, Long> auditCountMap = new HashMap<>();
        for (Map.Entry<AuditStatusEnum, Long> entry : auditCount.entrySet()) {
            auditCountMap.put(entry.getKey().getCode(), entry.getValue());
        }
        statistics.put("auditCount", auditCountMap);
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteMessages(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("留言ID列表不能为空");
        }

        log.info("批量删除留言, ids={}", ids);
        
        for (Long id : ids) {
            deleteMessage(id); // 级联删除
        }
        
        log.info("批量删除留言成功, count={}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAuditMessages(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("留言ID列表不能为空");
        }

        log.info("批量审核留言, ids={}, status={}", ids, status);
        
        for (Long id : ids) {
            auditMessage(id, status, null);
        }
        
        log.info("批量审核留言成功, count={}", ids.size());
    }

    /**
     * 转换为VO
     */
    private MemorialMessageVO convertToVO(MemorialMessage message) {
        MemorialMessageVO vo = new MemorialMessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }

    /**
     * 构建留言树形结构
     */
    private MemorialMessageVO buildMessageTree(MemorialMessage message) {
        MemorialMessageVO vo = convertToVO(message);
        
        // 查询回复
        List<MemorialMessageVO> replies = getMessageReplies(message.getId());
        vo.setReplies(replies);
        
        return vo;
    }
}
