package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ServiceProviderDTO;
import com.cemetery.domain.dto.ServiceProviderQueryDTO;
import com.cemetery.domain.vo.ServiceProviderVO;
import com.cemetery.service.IServiceProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务商管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
@Api(tags = "服务商管理")
public class ServiceProviderController {

    private final IServiceProviderService serviceProviderService;

    @PostMapping
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("创建服务商")
    public Result<Long> createProvider(@Validated @RequestBody ServiceProviderDTO providerDTO) {
        log.info("创建服务商, providerName={}, contactPerson={}", 
                providerDTO.getProviderName(), providerDTO.getContactPerson());
        Long providerId = serviceProviderService.createProvider(providerDTO);
        return Result.success(providerId);
    }

    @PutMapping
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("更新服务商")
    public Result<Void> updateProvider(@Validated @RequestBody ServiceProviderDTO providerDTO) {
        log.info("更新服务商, providerId={}", providerDTO.getId());
        serviceProviderService.updateProvider(providerDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("删除服务商")
    public Result<Void> deleteProvider(@ApiParam("服务商ID") @PathVariable Long id) {
        log.info("删除服务商, providerId={}", id);
        serviceProviderService.deleteProvider(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取服务商详情")
    public Result<ServiceProviderVO> getProviderDetail(@ApiParam("服务商ID") @PathVariable Long id) {
        log.info("获取服务商详情, providerId={}", id);
        ServiceProviderVO provider = serviceProviderService.getProviderDetail(id);
        return Result.success(provider);
    }

    @GetMapping("/no/{providerNo}")
    @ApiOperation("通过服务商编号获取服务商")
    public Result<ServiceProviderVO> getProviderByNo(@ApiParam("服务商编号") @PathVariable String providerNo) {
        log.info("通过服务商编号获取服务商, providerNo={}", providerNo);
        ServiceProviderVO provider = serviceProviderService.getProviderByNo(providerNo);
        if (provider == null) {
            return Result.error("服务商不存在");
        }
        return Result.success(provider);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询服务商")
    public Result<Page<ServiceProviderVO>> pageProviders(@Validated ServiceProviderQueryDTO queryDTO) {
        log.info("分页查询服务商, page={}, size={}", queryDTO.getPage(), queryDTO.getPageSize());
        Page<ServiceProviderVO> providerPage = serviceProviderService.pageProviders(queryDTO);
        return Result.success(providerPage);
    }

    @GetMapping("/recommended")
    @ApiOperation("获取推荐服务商")
    public Result<List<ServiceProviderVO>> getRecommendedProviders(
            @ApiParam("返回数量") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取推荐服务商, limit={}", limit);
        List<ServiceProviderVO> providers = serviceProviderService.getRecommendedProviders(limit);
        return Result.success(providers);
    }

    @GetMapping("/search")
    @ApiOperation("搜索服务商")
    public Result<Page<ServiceProviderVO>> searchProviders(
            @ApiParam("关键词") @RequestParam(required = false) String keyword,
            @ApiParam("状态") @RequestParam(required = false) Integer status,
            @Validated PageQueryDTO pageQueryDTO) {
        log.info("搜索服务商, keyword={}, status={}", keyword, status);
        Page<ServiceProviderVO> providerPage = serviceProviderService.searchProviders(
                keyword, status, pageQueryDTO);
        return Result.success(providerPage);
    }

    @PostMapping("/{id}/audit")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("审核服务商")
    public Result<Void> auditProvider(
            @ApiParam("服务商ID") @PathVariable Long id,
            @ApiParam("是否通过") @RequestParam Boolean approved,
            @ApiParam("审核备注") @RequestParam(required = false) String auditRemark) {
        log.info("审核服务商, providerId={}, approved={}", id, approved);
        serviceProviderService.auditProvider(id, approved, auditRemark);
        return Result.success();
    }

    @PostMapping("/{id}/rating")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("更新服务商评分")
    public Result<Void> updateRating(
            @ApiParam("服务商ID") @PathVariable Long id,
            @ApiParam("评分") @RequestParam BigDecimal rating) {
        log.info("更新服务商评分, providerId={}, rating={}", id, rating);
        serviceProviderService.updateRating(id, rating);
        return Result.success();
    }

    @PostMapping("/{id}/increment-service")
    // @RequireRole({"ADMIN", "SERVICE"})  // 临时注释用于测试
    @ApiOperation("增加服务次数")
    public Result<Void> incrementServiceCount(@ApiParam("服务商ID") @PathVariable Long id) {
        log.info("增加服务次数, providerId={}", id);
        serviceProviderService.incrementServiceCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/update-statistics")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("更新服务商统计信息")
    public Result<Void> updateStatistics(@ApiParam("服务商ID") @PathVariable Long id) {
        log.info("更新服务商统计信息, providerId={}", id);
        serviceProviderService.updateStatistics(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("获取服务商统计信息")
    public Result<Map<String, Object>> getStatistics() {
        log.info("获取服务商统计信息");
        Map<String, Object> statistics = serviceProviderService.getProviderStatistics();
        return Result.success(statistics);
    }

    @PostMapping("/{id}/status")
    // @RequireRole("ADMIN")  // 临时注释用于测试
    @ApiOperation("修改服务商状态")
    public Result<Void> changeProviderStatus(
            @ApiParam("服务商ID") @PathVariable Long id,
            @ApiParam("状态") @RequestParam Integer status) {
        log.info("修改服务商状态, providerId={}, status={}", id, status);
        serviceProviderService.changeProviderStatus(id, status);
        return Result.success();
    }
}
