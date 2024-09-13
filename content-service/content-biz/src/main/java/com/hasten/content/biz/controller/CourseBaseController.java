package com.hasten.content.biz.controller;

import com.hasten.common.domain.Result;
import com.hasten.content.biz.service.ICourseBaseService;
import com.hasten.common.domain.PageParams;
import com.hasten.common.domain.PageResult;
import com.hasten.content.domain.dto.AddCourseDto;
import com.hasten.content.domain.dto.QueryCourseParamsDto;
import com.hasten.content.domain.entity.CourseBase;
import com.hasten.media.api.MediaFilesClient;
import com.hasten.media.domain.entity.MediaFiles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * The application.yaml defines the servlet context-path: /content, so this prefix should not be added here
 * @author Hasten
 */
@Api(value = "course-base-info", tags = {"course"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class CourseBaseController {

    private final ICourseBaseService courseBaseService;

    private final MediaFilesClient mediaFilesClient;

    @ApiOperation(value = "query course list")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(
            PageParams pageParams,
            @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto)
    {
        return courseBaseService.queryCourseBaseList(pageParams, queryCourseParamsDto);
    }

    @ApiOperation(value = "create course base")
    @PostMapping("/course")
    public Result<Void> createCourseBase(
            @RequestBody @Validated
            AddCourseDto addCourseDto)
    {
        //TODO: fixme when create the VALIDATION_SYSTEM
        Long companyId = 123123L;

        return null;
    }


    @GetMapping("/test")
    public Result<String> testApi() {
        return Result.ok("Content,test. Wow, you can really dance!!!");
    }

    @GetMapping("/test/openfeign")
    public Result<List<MediaFiles>> testOpenFeign() {
        System.out.println("testOpenFeign....");
        Result<List<MediaFiles>> listResult = mediaFilesClient.testUpload();
        return listResult;
    }

}
