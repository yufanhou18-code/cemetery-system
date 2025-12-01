package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.DeceasedInfoDTO;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.vo.DeceasedInfoVO;
import com.cemetery.service.DeceasedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 逝者信息管理控制器
 */
@Api(tags = "逝者信息管理")
@RestController
@RequestMapping("/api/deceased")
public class DeceasedController {

    @Autowired
    private DeceasedService deceasedService;

    @ApiOperation("创建逝者信息")
    @PostMapping
    public Result<Long> createDeceased(@Validated @RequestBody DeceasedInfoDTO deceasedDTO) {
        Long deceasedId = deceasedService.createDeceased(deceasedDTO);
        return Result.success("逝者信息创建成功", deceasedId);
    }

    @ApiOperation("更新逝者信息")
    @PutMapping
    public Result<Void> updateDeceased(@Validated @RequestBody DeceasedInfoDTO deceasedDTO) {
        deceasedService.updateDeceased(deceasedDTO);
        return Result.success();
    }

    @ApiOperation("删除逝者信息")
    @DeleteMapping("/{deceasedId}")
    public Result<Void> deleteDeceased(@PathVariable Long deceasedId) {
        deceasedService.deleteDeceased(deceasedId);
        return Result.success();
    }

    @ApiOperation("根据ID查询逝者信息")
    @GetMapping("/{deceasedId}")
    public Result<DeceasedInfoVO> getDeceasedById(@PathVariable Long deceasedId) {
        DeceasedInfoVO deceasedVO = deceasedService.getDeceasedById(deceasedId);
        return Result.success(deceasedVO);
    }

    @ApiOperation("分页查询逝者信息列表")
    @PostMapping("/page")
    public Result<Page<DeceasedInfoVO>> pageDeceased(@RequestBody PageQueryDTO pageQueryDTO) {
        Page<DeceasedInfoVO> page = deceasedService.pageDeceased(pageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("根据身份证号查询逝者")
    @GetMapping("/id-card/{idCard}")
    public Result<DeceasedInfoVO> getDeceasedByIdCard(@PathVariable String idCard) {
        DeceasedInfoVO deceasedVO = deceasedService.getDeceasedByIdCard(idCard);
        return Result.success(deceasedVO);
    }

    @ApiOperation("根据墓位ID查询逗者列表")
    @GetMapping("/tomb/{tombId}")
    public Result<List<DeceasedInfoVO>> getDeceasedByTombId(@PathVariable Long tombId) {
        List<DeceasedInfoVO> deceasedList = deceasedService.getDeceasedByTombId(tombId);
        return Result.success(deceasedList);
    }

    @ApiOperation("根据姓名模糊查询逗者列表")
    @GetMapping("/name/{name}")
    public Result<List<DeceasedInfoVO>> searchDeceasedByName(@PathVariable String name) {
        List<DeceasedInfoVO> deceasedList = deceasedService.searchDeceasedByName(name);
        return Result.success(deceasedList);
    }
}
