package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.MemorialMessageDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.MemorialMessageVO;
import com.cemetery.service.MemorialMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 追思留言管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/memorial/message")
@RequiredArgsConstructor
@Api(tags = "追思留言管理")
public class MemorialMessageController {

    private final MemorialMessageService messageService;

    @PostMapping
    @ApiOperation(value = "发送追思留言", notes = "发送追思留言，支持匿名留言和图片")
    public Result<Long> sendMessage(@Validated @RequestBody MemorialMessageDTO messageDTO) {
        log.info("发送追思留言, memorialId={}, authorName={}", 
                messageDTO.getMemorialId(), messageDTO.getAuthorName());
        
        Long messageId = messageService.sendMessage(messageDTO);
        return Result.success(messageId);
    }

    @PostMapping("/reply")
    @ApiOperation(value = "回复留言", notes = "回复某条留言，支持多级回复")
    public Result<Long> replyMessage(@Validated @RequestBody MemorialMessageDTO messageDTO) {
        log.info("回复留言, memorialId={}, parentId={}, replyToId={}", 
                messageDTO.getMemorialId(), messageDTO.getParentId(), messageDTO.getReplyToId());
        
        Long messageId = messageService.replyMessage(messageDTO);
        return Result.success(messageId);
    }

    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "删除留言", notes = "删除指定留言，级联删除所有回复")
    public Result<Void> deleteMessage(@ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("删除留言, messageId={}", id);
        
        messageService.deleteMessage(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取留言详情", notes = "获取指定留言的详细信息")
    public Result<MemorialMessageVO> getMessage(@ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("获取留言详情, messageId={}", id);
        
        MemorialMessageVO message = messageService.getMessageById(id);
        return Result.success(message);
    }

    @GetMapping("/memorial/{memorialId}")
    @ApiOperation(value = "获取纪念空间的留言列表", notes = "获取指定纪念空间的所有留言（树形结构）")
    public Result<List<MemorialMessageVO>> getMemorialMessages(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId,
            @ApiParam(value = "留言类型：1-普通留言，2-祭日留言，3-纪念日留言") @RequestParam(required = false) Integer messageType) {
        log.info("获取纪念空间留言, memorialId={}, messageType={}", memorialId, messageType);
        
        List<MemorialMessageVO> messages = messageService.getMemorialMessages(memorialId, messageType);
        return Result.success(messages);
    }

    @GetMapping("/memorial/{memorialId}/page")
    @ApiOperation(value = "分页查询留言", notes = "分页查询指定纪念空间的留言")
    public Result<Page<MemorialMessageVO>> pageMemorialMessages(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId,
            @ApiParam(value = "留言类型：1-普通留言，2-祭日留言，3-纪念日留言") @RequestParam(required = false) Integer messageType,
            @Validated PageQueryDTO pageQueryDTO) {
        log.info("分页查询留言, memorialId={}, messageType={}, page={}", 
                memorialId, messageType, pageQueryDTO.getPage());
        
        Page<MemorialMessageVO> messagePage = messageService.pageMemorialMessages(memorialId, messageType, pageQueryDTO);
        return Result.success(messagePage);
    }

    @GetMapping("/{id}/replies")
    @ApiOperation(value = "获取留言的所有回复", notes = "获取指定留言的所有回复（树形结构）")
    public Result<List<MemorialMessageVO>> getMessageReplies(
            @ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("获取留言回复, messageId={}", id);
        
        List<MemorialMessageVO> replies = messageService.getMessageReplies(id);
        return Result.success(replies);
    }

    @PostMapping("/{id}/like")
    @ApiOperation(value = "点赞留言", notes = "为留言点赞")
    public Result<Void> likeMessage(@ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("点赞留言, messageId={}", id);
        
        messageService.likeMessage(id);
        return Result.success();
    }

    @PostMapping("/{id}/pin")
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "置顶留言", notes = "将留言置顶显示")
    public Result<Void> pinMessage(@ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("置顶留言, messageId={}", id);
        
        messageService.pinMessage(id);
        return Result.success();
    }

    @PostMapping("/{id}/unpin")
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "取消置顶留言", notes = "取消留言的置顶状态")
    public Result<Void> unpinMessage(@ApiParam(value = "留言ID", required = true) @PathVariable Long id) {
        log.info("取消置顶留言, messageId={}", id);
        
        messageService.unpinMessage(id);
        return Result.success();
    }

    @PostMapping("/{id}/audit")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "审核留言", notes = "审核留言通过或拒绝")
    public Result<Void> auditMessage(
            @ApiParam(value = "留言ID", required = true) @PathVariable Long id,
            @ApiParam(value = "审核状态：1-通过，2-拒绝", required = true) @RequestParam Integer status,
            @ApiParam(value = "审核备注") @RequestParam(required = false) String remark) {
        log.info("审核留言, messageId={}, status={}, remark={}", id, status, remark);
        
        messageService.auditMessage(id, status, remark);
        return Result.success();
    }

    @GetMapping("/recent")
    @ApiOperation(value = "获取最新留言", notes = "获取所有纪念空间的最新留言")
    public Result<List<MemorialMessageVO>> getRecentMessages(
            @ApiParam(value = "返回数量", defaultValue = "20") @RequestParam(defaultValue = "20") Integer limit) {
        log.info("获取最新留言, limit={}", limit);
        
        List<MemorialMessageVO> messages = messageService.getRecentMessages(limit);
        return Result.success(messages);
    }

    @GetMapping("/statistics/{memorialId}")
    @ApiOperation(value = "获取留言统计信息", notes = "统计各类型留言的数量")
    public Result<Object> getMessageStatistics(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId) {
        log.info("获取留言统计信息, memorialId={}", memorialId);
        
        Object statistics = messageService.getMessageStatistics(memorialId);
        return Result.success(statistics);
    }

    @PostMapping("/batch-delete")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "批量删除留言", notes = "批量删除多条留言及其回复")
    public Result<Void> batchDeleteMessages(
            @ApiParam(value = "留言ID列表", required = true) @RequestBody List<Long> ids) {
        log.info("批量删除留言, ids={}", ids);
        
        messageService.batchDeleteMessages(ids);
        return Result.success();
    }

    @PostMapping("/batch-audit")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "批量审核留言", notes = "批量审核多条留言")
    public Result<Void> batchAuditMessages(
            @ApiParam(value = "留言ID列表", required = true) @RequestBody List<Long> ids,
            @ApiParam(value = "审核状态：1-通过，2-拒绝", required = true) @RequestParam Integer status) {
        log.info("批量审核留言, ids={}, status={}", ids, status);
        
        messageService.batchAuditMessages(ids, status);
        return Result.success();
    }
}
