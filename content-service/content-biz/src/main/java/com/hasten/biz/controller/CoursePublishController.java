package com.hasten.biz.controller;


import com.hasten.biz.service.ICoursePublishService;
import com.hasten.domain.entity.CoursePublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程发布 前端控制器
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@RestController
@RequestMapping("/course-publish")
public class CoursePublishController {
    @Autowired
    private ICoursePublishService coursePublishService;

    @GetMapping
    public String publishCourse() {
        CoursePublish byId = coursePublishService.getById(2);
        System.out.println(byId);
        return byId.toString();
    }

}
