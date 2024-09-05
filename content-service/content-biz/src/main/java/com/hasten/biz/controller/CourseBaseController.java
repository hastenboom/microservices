package com.hasten.biz.controller;

import com.hasten.biz.service.ICourseBaseService;
import com.hasten.common.domain.PageParams;
import com.hasten.common.domain.PageResult;
import com.hasten.domain.dto.QueryCourseParamsDto;
import com.hasten.domain.entity.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * The application.yaml defines the servlet context-path: /content, so this prefix should not be added here
 * @author Hasten
 */
@Api(value = "course-base-info", tags = {"course"})
@RestController
@RequiredArgsConstructor
public class CourseBaseController {

    private final ICourseBaseService courseBaseService;


    @ApiOperation(value="query course list")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(
            PageParams pageParams,
            @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto)
    {
        return courseBaseService.queryCourseBaseList(pageParams, queryCourseParamsDto);
    }


}
