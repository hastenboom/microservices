package com.hasten.content.biz.controller;

import com.hasten.common.domain.Result;
import com.hasten.content.biz.service.ICourseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hasten
 */
@RestController
@RequestMapping("/content/course-category")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final ICourseCategoryService courseCategoryService;


    @GetMapping("/tree-nodes")
    public Result<?> getTreeNode() {

        courseCategoryService.queryTreeNodes("1");
        return Result.ok("/course-category/tree-nodes success!!!");
    }

}
