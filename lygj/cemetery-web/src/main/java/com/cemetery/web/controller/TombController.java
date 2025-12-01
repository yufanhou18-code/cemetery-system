package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.TombLocationDTO;
import com.cemetery.domain.enums.TombStatusEnum;
import com.cemetery.domain.enums.TombTypeEnum;
import com.cemetery.domain.vo.TombLocationVO;
import com.cemetery.service.TombService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 墓位管理控制器
 */
@Api(tags = "墓位管理")
@RestController
@RequestMapping("/api/tombs")
public class TombController {

    @Autowired
    private TombService tombService;

    @ApiOperation("创建墓位")
    @PostMapping
    public Result<Long> createTomb(@Validated @RequestBody TombLocationDTO tombDTO) {
        Long tombId = tombService.createTomb(tombDTO);
        return Result.success("墓位创建成功", tombId);
    }

    @ApiOperation("更新墓位")
    @PutMapping
    public Result<Void> updateTomb(@Validated @RequestBody TombLocationDTO tombDTO) {
        tombService.updateTomb(tombDTO);
        return Result.success();
    }

    @ApiOperation("删除墓位")
    @DeleteMapping("/{tombId}")
    public Result<Void> deleteTomb(@PathVariable Long tombId) {
        tombService.deleteTomb(tombId);
        return Result.success();
    }

    @ApiOperation("根据ID查询墓位")
    @GetMapping("/{tombId}")
    public Result<TombLocationVO> getTombById(@PathVariable Long tombId) {
        TombLocationVO tombVO = tombService.getTombById(tombId);
        return Result.success(tombVO);
    }

    @ApiOperation("分页查询墓位列表")
    @PostMapping("/page")
    public Result<Page<TombLocationVO>> pageTombs(@RequestBody PageQueryDTO pageQueryDTO) {
        Page<TombLocationVO> page = tombService.pageTombs(pageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("根据墓位编号查询")
    @GetMapping("/tomb-no/{tombNo}")
    public Result<TombLocationVO> getTombByTombNo(@PathVariable String tombNo) {
        TombLocationVO tombVO = tombService.getTombByTombNo(tombNo);
        return Result.success(tombVO);
    }

    @ApiOperation("根据区域编码查询墓位列表")
    @GetMapping("/area/{areaCode}")
    public Result<List<TombLocationVO>> getTombsByArea(@PathVariable String areaCode) {
        List<TombLocationVO> tombs = tombService.getTombsByArea(areaCode);
        return Result.success(tombs);
    }

    @ApiOperation("查询可用墓位列表")
    @GetMapping("/available")
    public Result<List<TombLocationVO>> getAvailableTombs() {
        List<TombLocationVO> tombs = tombService.getAvailableTombs();
        return Result.success(tombs);
    }

    @ApiOperation("更新墓位状态")
    @PostMapping("/{tombId}/status")
    public Result<Void> changeTombStatus(
            @PathVariable Long tombId,
            @RequestParam Integer status) {
        TombStatusEnum statusEnum = TombStatusEnum.values()[status];
        tombService.changeTombStatus(tombId, statusEnum);
        return Result.success();
    }

    @ApiOperation("分配墓位给家属")
    @PostMapping("/{tombId}/assign")
    public Result<Void> assignTombToOwner(
            @PathVariable Long tombId,
            @RequestParam Long ownerId) {
        tombService.assignTombToOwner(tombId, ownerId);
        return Result.success();
    }

    @ApiOperation("根据墓位类型查询")
    @GetMapping("/type/{tombType}")
    public Result<List<TombLocationVO>> getTombsByType(@PathVariable Integer tombType) {
        TombTypeEnum tombTypeEnum = TombTypeEnum.values()[tombType];
        List<TombLocationVO> tombs = tombService.getTombsByType(tombTypeEnum);
        return Result.success(tombs);
    }

    @ApiOperation("根据状态查询墓位")
    @GetMapping("/status/{status}")
    public Result<List<TombLocationVO>> getTombsByStatus(@PathVariable Integer status) {
        TombStatusEnum statusEnum = TombStatusEnum.values()[status];
        List<TombLocationVO> tombs = tombService.getTombsByStatus(statusEnum);
        return Result.success(tombs);
    }

    @ApiOperation("根据所有者查询墓位")
    @GetMapping("/owner/{ownerId}")
    public Result<List<TombLocationVO>> getTombsByOwner(@PathVariable Long ownerId) {
        List<TombLocationVO> tombs = tombService.getTombsByOwner(ownerId);
        return Result.success(tombs);
    }
}
