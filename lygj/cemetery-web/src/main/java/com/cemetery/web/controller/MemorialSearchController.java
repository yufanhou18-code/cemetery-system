package com.cemetery.web.controller;

import com.cemetery.common.result.Result;
import com.cemetery.domain.dto.MemorialSearchDTO;
import com.cemetery.service.MemorialSearchService;
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
 * 纪念空间搜索检索接口
 */
@Slf4j
@RestController
@RequestMapping("/api/memorial/search")
@RequiredArgsConstructor
@Api(tags = "搜索检索服务")
public class MemorialSearchController {

    private final MemorialSearchService searchService;

    @PostMapping
    @ApiOperation(value = "全文搜索", notes = "基于Elasticsearch的全文搜索，支持中文分词、同义词扩展和高亮显示")
    public Result<Object> search(@Validated @RequestBody MemorialSearchDTO searchDTO) {
        log.info("执行全文搜索, keyword={}, searchType={}, page={}", 
                searchDTO.getKeyword(), searchDTO.getSearchType(), searchDTO.getPage());
        
        Object searchResult = searchService.search(searchDTO);
        return Result.success(searchResult);
    }

    @GetMapping("/memorial")
    @ApiOperation(value = "搜索纪念空间", notes = "按关键词搜索纪念空间，匹配空间名称、逝者姓名、生平介绍等")
    public Result<Object> searchMemorial(
            @ApiParam(value = "搜索关键词", required = true) @RequestParam String keyword,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "是否高亮", defaultValue = "true") @RequestParam(defaultValue = "true") Boolean highlight) {
        log.info("搜索纪念空间, keyword={}, page={}, pageSize={}", keyword, page, pageSize);
        
        Object searchResult = searchService.searchMemorial(keyword, page, pageSize, highlight);
        return Result.success(searchResult);
    }

    @GetMapping("/deceased")
    @ApiOperation(value = "搜索逝者", notes = "按姓名搜索逝者信息")
    public Result<Object> searchDeceased(
            @ApiParam(value = "逝者姓名", required = true) @RequestParam String name,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("搜索逝者, name={}, page={}, pageSize={}", name, page, pageSize);
        
        Object searchResult = searchService.searchDeceased(name, page, pageSize);
        return Result.success(searchResult);
    }

    @GetMapping("/content")
    @ApiOperation(value = "搜索内容", notes = "搜索纪念内容，包括文本、标题、描述等")
    public Result<Object> searchContent(
            @ApiParam(value = "搜索关键词", required = true) @RequestParam String keyword,
            @ApiParam(value = "内容类型：1-文本，2-图片，3-视频，4-音频，5-文档") @RequestParam(required = false) Integer contentType,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("搜索内容, keyword={}, contentType={}, page={}", keyword, contentType, page);
        
        Object searchResult = searchService.searchContent(keyword, contentType, page, pageSize);
        return Result.success(searchResult);
    }

    @GetMapping("/message")
    @ApiOperation(value = "搜索留言", notes = "搜索追思留言内容")
    public Result<Object> searchMessage(
            @ApiParam(value = "搜索关键词", required = true) @RequestParam String keyword,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("搜索留言, keyword={}, page={}", keyword, page);
        
        Object searchResult = searchService.searchMessage(keyword, page, pageSize);
        return Result.success(searchResult);
    }

    @GetMapping("/suggest")
    @ApiOperation(value = "搜索建议", notes = "根据输入的关键词提供搜索建议")
    public Result<List<String>> searchSuggest(
            @ApiParam(value = "搜索关键词", required = true) @RequestParam String keyword,
            @ApiParam(value = "返回数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取搜索建议, keyword={}, limit={}", keyword, limit);
        
        List<String> suggestions = searchService.searchSuggest(keyword, limit);
        return Result.success(suggestions);
    }

    @GetMapping("/hot-keywords")
    @ApiOperation(value = "热门搜索词", notes = "获取热门搜索关键词")
    public Result<List<Map<String, Object>>> getHotKeywords(
            @ApiParam(value = "返回数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门搜索词, limit={}", limit);
        
        List<Map<String, Object>> hotKeywords = searchService.getHotKeywords(limit);
        return Result.success(hotKeywords);
    }

    @PostMapping("/index/memorial/{id}")
    @ApiOperation(value = "索引纪念空间", notes = "将指定纪念空间添加到搜索索引")
    public Result<Void> indexMemorial(@ApiParam(value = "纪念空间ID", required = true) @PathVariable Long id) {
        log.info("索引纪念空间, memorialId={}", id);
        
        searchService.indexMemorial(id);
        return Result.success();
    }

    @DeleteMapping("/index/memorial/{id}")
    @ApiOperation(value = "删除索引", notes = "从搜索索引中删除指定纪念空间")
    public Result<Void> deleteMemorialIndex(@ApiParam(value = "纪念空间ID", required = true) @PathVariable Long id) {
        log.info("删除纪念空间索引, memorialId={}", id);
        
        searchService.deleteMemorialIndex(id);
        return Result.success();
    }

    @PostMapping("/index/rebuild")
    @ApiOperation(value = "重建索引", notes = "重建所有纪念空间的搜索索引")
    public Result<Void> rebuildIndex() {
        log.info("开始重建搜索索引");
        
        searchService.rebuildIndex();
        return Result.success();
    }

    @GetMapping("/advanced")
    @ApiOperation(value = "高级搜索", notes = "支持多条件组合搜索")
    public Result<Object> advancedSearch(
            @ApiParam(value = "逝者姓名") @RequestParam(required = false) String deceasedName,
            @ApiParam(value = "空间名称") @RequestParam(required = false) String spaceName,
            @ApiParam(value = "出生日期开始") @RequestParam(required = false) String birthDateStart,
            @ApiParam(value = "出生日期结束") @RequestParam(required = false) String birthDateEnd,
            @ApiParam(value = "逝世日期开始") @RequestParam(required = false) String deathDateStart,
            @ApiParam(value = "逝世日期结束") @RequestParam(required = false) String deathDateEnd,
            @ApiParam(value = "访问权限：1-公开，2-家属可见") @RequestParam(required = false) Integer accessPermission,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", defaultValue = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("执行高级搜索, deceasedName={}, spaceName={}", deceasedName, spaceName);
        
        Object searchResult = searchService.advancedSearch(
                deceasedName, spaceName, birthDateStart, birthDateEnd, 
                deathDateStart, deathDateEnd, accessPermission, page, pageSize);
        return Result.success(searchResult);
    }
}
