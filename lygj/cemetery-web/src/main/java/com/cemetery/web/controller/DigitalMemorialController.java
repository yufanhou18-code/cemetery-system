package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.DigitalMemorialDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.enums.UserTypeEnum;
import com.cemetery.domain.vo.DigitalMemorialVO;
import com.cemetery.service.MemorialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数字纪念空间管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/memorial")
@RequiredArgsConstructor
@Api(tags = "数字纪念空间管理")
public class DigitalMemorialController {

    private final MemorialService memorialService;

    @PostMapping
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("创建纪念空间")
    public Result<Long> createMemorial(@Validated @RequestBody DigitalMemorialDTO memorialDTO) {
        log.info("创建纪念空间, deceasedId={}", memorialDTO.getDeceasedId());
        Long memorialId = memorialService.createMemorial(memorialDTO);
        return Result.success(memorialId);
    }

    @PutMapping
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("更新纪念空间")
    public Result<Void> updateMemorial(@Validated @RequestBody DigitalMemorialDTO memorialDTO) {
        log.info("更新纪念空间, memorialId={}", memorialDTO.getId());
        memorialService.updateMemorial(memorialDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取纪念空间详情")
    public Result<DigitalMemorialVO> getMemorialDetail(
            @ApiParam("纪念空间ID") @PathVariable Long id,
            @ApiParam("访问密码（如需要）") @RequestParam(required = false) String password) {
        log.info("获取纪念空间详情, memorialId={}", id);
        
        // 验证访问权限
        if (!memorialService.verifyAccess(id, password)) {
            return Result.error("访问权限验证失败");
        }
        
        // 增加访问次数
        memorialService.incrementVisitCount(id);
        
        DigitalMemorialVO memorial = memorialService.getMemorialDetail(id);
        return Result.success(memorial);
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    @ApiOperation("删除纪念空间")
    public Result<Void> deleteMemorial(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("删除纪念空间, memorialId={}", id);
        memorialService.deleteMemorial(id);
        return Result.success();
    }

    @GetMapping("/space/{spaceNo}")
    @ApiOperation("通过空间编号获取纪念空间")
    public Result<DigitalMemorialVO> getMemorialBySpaceNo(
            @ApiParam("空间编号") @PathVariable String spaceNo,
            @ApiParam("访问密码（如需要）") @RequestParam(required = false) String password) {
        log.info("通过空间编号获取纪念空间, spaceNo={}", spaceNo);
        
        DigitalMemorialVO memorial = memorialService.getMemorialBySpaceNo(spaceNo);
        if (memorial == null) {
            return Result.error("纪念空间不存在");
        }
        
        // 验证访问权限
        if (!memorialService.verifyAccess(memorial.getId(), password)) {
            return Result.error("访问权限验证失败");
        }
        
        // 增加访问次数
        memorialService.incrementVisitCount(memorial.getId());
        
        return Result.success(memorial);
    }

    @GetMapping("/deceased/{deceasedId}")
    @ApiOperation("通过逝者ID获取纪念空间")
    public Result<DigitalMemorialVO> getMemorialByDeceasedId(@ApiParam("逝者ID") @PathVariable Long deceasedId) {
        log.info("通过逝者ID获取纪念空间, deceasedId={}", deceasedId);
        DigitalMemorialVO memorial = memorialService.getMemorialByDeceasedId(deceasedId);
        return Result.success(memorial);
    }

    @GetMapping("/tomb/{tombId}")
    @ApiOperation("通过墓位ID获取纪念空间")
    public Result<DigitalMemorialVO> getMemorialByTombId(@ApiParam("墓位ID") @PathVariable Long tombId) {
        log.info("通过墓位ID获取纪念空间, tombId={}", tombId);
        DigitalMemorialVO memorial = memorialService.getMemorialByTombId(tombId);
        return Result.success(memorial);
    }

    @GetMapping("/published")
    @ApiOperation("分页查询已发布的纪念空间")
    public Result<Page<DigitalMemorialVO>> pagePublishedMemorials(@Validated PageQueryDTO pageQueryDTO) {
        log.info("分页查询已发布的纪念空间, page={}, size={}", pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<DigitalMemorialVO> memorialPage = memorialService.pagePublishedMemorials(pageQueryDTO);
        return Result.success(memorialPage);
    }

    @PostMapping("/{id}/publish")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("发布纪念空间")
    public Result<Void> publishMemorial(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("发布纪念空间, memorialId={}", id);
        memorialService.publishMemorial(id);
        return Result.success();
    }

    @PostMapping("/{id}/unpublish")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("取消发布纪念空间")
    public Result<Void> unpublishMemorial(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("取消发布纪念空间, memorialId={}", id);
        memorialService.unpublishMemorial(id);
        return Result.success();
    }

    @PostMapping("/{id}/candle")
    @ApiOperation("点蜡烛")
    public Result<Void> lightCandle(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("点蜡烛, memorialId={}", id);
        memorialService.incrementCandleCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/flower")
    @ApiOperation("献花")
    public Result<Void> offerFlower(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("献花, memorialId={}", id);
        memorialService.incrementFlowerCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/incense")
    @ApiOperation("上香")
    public Result<Void> offerIncense(@ApiParam("纪念空间ID") @PathVariable Long id) {
        log.info("上香, memorialId={}", id);
        memorialService.incrementIncenseCount(id);
        return Result.success();
    }

    @GetMapping("/popular")
    @ApiOperation("获取热门纪念空间")
    public Result<List<DigitalMemorialVO>> getPopularMemorials(
            @ApiParam("返回数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门纪念空间, limit={}", limit);
        List<DigitalMemorialVO> memorials = memorialService.getPopularMemorials(limit);
        return Result.success(memorials);
    }

    @GetMapping("/expiring")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("获取即将到期的纪念空间")
    public Result<List<DigitalMemorialVO>> getExpiringMemorials(
            @ApiParam("提前天数") @RequestParam(defaultValue = "30") Integer days) {
        log.info("获取即将到期的纪念空间, days={}", days);
        List<DigitalMemorialVO> memorials = memorialService.getExpiringMemorials(days);
        return Result.success(memorials);
    }

    @GetMapping("/statistics")
    @RequireRole("ADMIN")
    @ApiOperation("获取纪念空间统计信息")
    public Result<Map<String, Object>> getStatistics() {
        log.info("获取纪念空间统计信息");
        Map<String, Object> statistics = memorialService.getMemorialStatistics();
        return Result.success(statistics);
    }
}
