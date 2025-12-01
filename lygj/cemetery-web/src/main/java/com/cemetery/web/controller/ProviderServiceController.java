package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.PageQueryDTO;
import com.cemetery.domain.dto.ProviderServiceDTO;
import com.cemetery.domain.dto.ProviderServiceQueryDTO;
import com.cemetery.domain.vo.ProviderServiceVO;
import com.cemetery.service.IProviderServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/provider-service")
@RequiredArgsConstructor
@Api(tags = "Provider Service Management")
public class ProviderServiceController {

    private final IProviderServiceService providerServiceService;

    @PostMapping
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Create Service")
    public Result<Long> createService(@Validated @RequestBody ProviderServiceDTO serviceDTO) {
        Long serviceId = providerServiceService.createService(serviceDTO);
        return Result.success(serviceId);
    }

    @PutMapping
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Update Service")
    public Result<Void> updateService(@Validated @RequestBody ProviderServiceDTO serviceDTO) {
        providerServiceService.updateService(serviceDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    @ApiOperation("Delete Service")
    public Result<Void> deleteService(@PathVariable Long id) {
        providerServiceService.deleteService(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Service Detail")
    public Result<ProviderServiceVO> getServiceDetail(@PathVariable Long id) {
        ProviderServiceVO service = providerServiceService.getServiceDetail(id);
        providerServiceService.incrementViewCount(id);
        return Result.success(service);
    }

    @GetMapping("/no/{serviceNo}")
    @ApiOperation("Get Service By No")
    public Result<ProviderServiceVO> getServiceByNo(@PathVariable String serviceNo) {
        ProviderServiceVO service = providerServiceService.getServiceByNo(serviceNo);
        if (service == null) {
            return Result.error("Service not found");
        }
        return Result.success(service);
    }

    @GetMapping("/page")
    @ApiOperation("Page Query")
    public Result<Page<ProviderServiceVO>> pageServices(@Validated ProviderServiceQueryDTO queryDTO) {
        Page<ProviderServiceVO> servicePage = providerServiceService.pageServices(queryDTO);
        return Result.success(servicePage);
    }

    @GetMapping("/provider/{providerId}")
    @ApiOperation("Get Services By Provider")
    public Result<List<ProviderServiceVO>> getServicesByProvider(@PathVariable Long providerId) {
        List<ProviderServiceVO> services = providerServiceService.getServicesByProvider(providerId);
        return Result.success(services);
    }

    @GetMapping("/search")
    @ApiOperation("Search Services")
    public Result<Page<ProviderServiceVO>> searchServices(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer serviceType,
            @Validated PageQueryDTO pageQueryDTO) {
        Page<ProviderServiceVO> servicePage = providerServiceService.searchServices(
                keyword, serviceType, pageQueryDTO);
        return Result.success(servicePage);
    }

    @GetMapping("/popular")
    @ApiOperation("Get Popular Services")
    public Result<List<ProviderServiceVO>> getPopularServices(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<ProviderServiceVO> services = providerServiceService.getPopularServices(limit);
        return Result.success(services);
    }

    @GetMapping("/recommended")
    @ApiOperation("Get Recommended Services")
    public Result<List<ProviderServiceVO>> getRecommendedServices(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<ProviderServiceVO> services = providerServiceService.getRecommendedServices(limit);
        return Result.success(services);
    }

    @PostMapping("/{id}/publish")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Publish Service")
    public Result<Void> publishService(@PathVariable Long id) {
        providerServiceService.publishService(id);
        return Result.success();
    }

    @PostMapping("/{id}/unpublish")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Unpublish Service")
    public Result<Void> unpublishService(@PathVariable Long id) {
        providerServiceService.unpublishService(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    @RequireRole("ADMIN")
    @ApiOperation("Get Statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = providerServiceService.getServiceStatistics();
        return Result.success(statistics);
    }
}
