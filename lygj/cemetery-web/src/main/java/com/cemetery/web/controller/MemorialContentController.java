package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.MemorialContentDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.MemorialContentVO;
import com.cemetery.service.MemorialContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 纪念空间内容管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/memorial/content")
@RequiredArgsConstructor
@Api(tags = "纪念空间内容管理")
public class MemorialContentController {

    private final MemorialContentService contentService;

    @PostMapping
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "添加纪念内容", notes = "支持文本、图片、视频、音频、文档等多种类型的内容")
    public Result<Long> addContent(@Validated @RequestBody MemorialContentDTO contentDTO) {
        log.info("添加纪念内容, memorialId={}, contentType={}", 
                contentDTO.getMemorialId(), contentDTO.getContentType());
        
        Long contentId = contentService.addContent(contentDTO);
        return Result.success(contentId);
    }

    @PutMapping
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "更新纪念内容", notes = "只能更新自己创建的内容，管理员可更新所有内容")
    public Result<Void> updateContent(@Validated @RequestBody MemorialContentDTO contentDTO) {
        log.info("更新纪念内容, contentId={}", contentDTO.getId());
        
        contentService.updateContent(contentDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "删除纪念内容", notes = "只能删除自己创建的内容，管理员可删除所有内容")
    public Result<Void> deleteContent(@ApiParam(value = "内容ID", required = true) @PathVariable Long id) {
        log.info("删除纪念内容, contentId={}", id);
        
        contentService.deleteContent(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取内容详情", notes = "获取指定内容的详细信息")
    public Result<MemorialContentVO> getContent(@ApiParam(value = "内容ID", required = true) @PathVariable Long id) {
        log.info("获取内容详情, contentId={}", id);
        
        MemorialContentVO content = contentService.getContentById(id);
        return Result.success(content);
    }

    @GetMapping("/memorial/{memorialId}")
    @ApiOperation(value = "获取纪念空间的所有内容", notes = "按内容类型和排序序号排列")
    public Result<List<MemorialContentVO>> getMemorialContents(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId,
            @ApiParam(value = "内容类型：1-文本，2-图片，3-视频，4-音频，5-文档") @RequestParam(required = false) Integer contentType) {
        log.info("获取纪念空间内容, memorialId={}, contentType={}", memorialId, contentType);
        
        List<MemorialContentVO> contents = contentService.getMemorialContents(memorialId, contentType);
        return Result.success(contents);
    }

    @GetMapping("/memorial/{memorialId}/page")
    @ApiOperation(value = "分页查询纪念空间内容", notes = "支持按内容类型筛选")
    public Result<Page<MemorialContentVO>> pageMemorialContents(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId,
            @ApiParam(value = "内容类型：1-文本，2-图片，3-视频，4-音频，5-文档") @RequestParam(required = false) Integer contentType,
            @Validated PageQueryDTO pageQueryDTO) {
        log.info("分页查询纪念空间内容, memorialId={}, contentType={}, page={}", 
                memorialId, contentType, pageQueryDTO.getPage());
        
        Page<MemorialContentVO> contentPage = contentService.pageMemorialContents(memorialId, contentType, pageQueryDTO);
        return Result.success(contentPage);
    }

    @GetMapping("/memorial/{memorialId}/featured")
    @ApiOperation(value = "获取精选内容", notes = "获取标记为精选展示的内容")
    public Result<List<MemorialContentVO>> getFeaturedContents(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId,
            @ApiParam(value = "返回数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取精选内容, memorialId={}, limit={}", memorialId, limit);
        
        List<MemorialContentVO> contents = contentService.getFeaturedContents(memorialId, limit);
        return Result.success(contents);
    }

    @PostMapping("/{id}/feature")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "设置为精选内容", notes = "将内容标记为精选展示")
    public Result<Void> featureContent(@ApiParam(value = "内容ID", required = true) @PathVariable Long id) {
        log.info("设置精选内容, contentId={}", id);
        
        contentService.featureContent(id);
        return Result.success();
    }

    @PostMapping("/{id}/unfeature")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "取消精选内容", notes = "取消内容的精选标记")
    public Result<Void> unfeatureContent(@ApiParam(value = "内容ID", required = true) @PathVariable Long id) {
        log.info("取消精选内容, contentId={}", id);
        
        contentService.unfeatureContent(id);
        return Result.success();
    }

    @PutMapping("/{id}/sort/{sortOrder}")
    @RequireRole({"ADMIN", "SERVICE", "FAMILY"})
    @ApiOperation(value = "调整内容排序", notes = "修改内容的排序序号")
    public Result<Void> updateSortOrder(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long id,
            @ApiParam(value = "排序序号", required = true) @PathVariable Integer sortOrder) {
        log.info("调整内容排序, contentId={}, sortOrder={}", id, sortOrder);
        
        contentService.updateSortOrder(id, sortOrder);
        return Result.success();
    }

    @GetMapping("/statistics/{memorialId}")
    @ApiOperation(value = "获取内容统计信息", notes = "统计各类型内容的数量和总大小")
    public Result<Object> getContentStatistics(
            @ApiParam(value = "纪念空间ID", required = true) @PathVariable Long memorialId) {
        log.info("获取内容统计信息, memorialId={}", memorialId);
        
        Object statistics = contentService.getContentStatistics(memorialId);
        return Result.success(statistics);
    }

    @PostMapping("/batch-delete")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation(value = "批量删除内容", notes = "批量删除多个内容")
    public Result<Void> batchDeleteContents(
            @ApiParam(value = "内容ID列表", required = true) @RequestBody List<Long> ids) {
        log.info("批量删除内容, ids={}", ids);
        
        contentService.batchDeleteContents(ids);
        return Result.success();
    }
}
