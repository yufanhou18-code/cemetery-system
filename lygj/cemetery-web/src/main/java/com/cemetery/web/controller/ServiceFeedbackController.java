package com.cemetery.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemetery.common.annotation.RequireRole;
import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.ServiceFeedbackDTO;
import com.cemetery.domain.dto.ServiceFeedbackQueryDTO;
import com.cemetery.domain.vo.ServiceFeedbackVO;
import com.cemetery.service.IServiceFeedbackService;
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
@RequestMapping("/api/service-feedback")
@RequiredArgsConstructor
@Api(tags = "Service Feedback Management")
public class ServiceFeedbackController {

    private final IServiceFeedbackService serviceFeedbackService;

    @PostMapping
    @RequireRole({"ADMIN", "SERVICE", "USER"})
    @ApiOperation("Create Feedback")
    public Result<Long> createFeedback(@Validated @RequestBody ServiceFeedbackDTO feedbackDTO) {
        Long feedbackId = serviceFeedbackService.createFeedback(feedbackDTO);
        return Result.success(feedbackId);
    }

    @PutMapping
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Update Feedback")
    public Result<Void> updateFeedback(@Validated @RequestBody ServiceFeedbackDTO feedbackDTO) {
        serviceFeedbackService.updateFeedback(feedbackDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    @ApiOperation("Delete Feedback")
    public Result<Void> deleteFeedback(@PathVariable Long id) {
        serviceFeedbackService.deleteFeedback(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Feedback Detail")
    public Result<ServiceFeedbackVO> getFeedbackDetail(@PathVariable Long id) {
        ServiceFeedbackVO feedback = serviceFeedbackService.getFeedbackDetail(id);
        return Result.success(feedback);
    }

    @GetMapping("/page")
    @ApiOperation("Page Query")
    public Result<Page<ServiceFeedbackVO>> pageFeedbacks(@Validated ServiceFeedbackQueryDTO queryDTO) {
        Page<ServiceFeedbackVO> feedbackPage = serviceFeedbackService.pageFeedbacks(queryDTO);
        return Result.success(feedbackPage);
    }

    @GetMapping("/service/{serviceId}")
    @ApiOperation("Get Feedbacks By Service")
    public Result<List<ServiceFeedbackVO>> getFeedbacksByService(@PathVariable Long serviceId) {
        List<ServiceFeedbackVO> feedbacks = serviceFeedbackService.getFeedbacksByService(serviceId);
        return Result.success(feedbacks);
    }

    @GetMapping("/provider/{providerId}")
    @ApiOperation("Get Feedbacks By Provider")
    public Result<List<ServiceFeedbackVO>> getFeedbacksByProvider(@PathVariable Long providerId) {
        List<ServiceFeedbackVO> feedbacks = serviceFeedbackService.getFeedbacksByProvider(providerId);
        return Result.success(feedbacks);
    }

    @PostMapping("/{id}/like")
    @ApiOperation("Like Feedback")
    public Result<Void> likeFeedback(@PathVariable Long id) {
        serviceFeedbackService.incrementLikeCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/reply")
    @RequireRole({"ADMIN", "SERVICE"})
    @ApiOperation("Reply Feedback")
    public Result<Void> replyFeedback(@PathVariable Long id, @RequestParam String reply) {
        serviceFeedbackService.replyFeedback(id, reply);
        return Result.success();
    }

    @PostMapping("/{id}/audit")
    @RequireRole("ADMIN")
    @ApiOperation("Audit Feedback")
    public Result<Void> auditFeedback(@PathVariable Long id, @RequestParam Boolean approved, @RequestParam(required = false) String auditRemark) {
        serviceFeedbackService.auditFeedback(id, approved, auditRemark);
        return Result.success();
    }

    @GetMapping("/statistics")
    @RequireRole("ADMIN")
    @ApiOperation("Get Statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = serviceFeedbackService.getFeedbackStatistics();
        return Result.success(statistics);
    }
}
