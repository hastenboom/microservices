package com.hasten.content.biz.service;

import com.hasten.content.domain.dto.AddCourseDto;
import com.hasten.content.domain.dto.CategoryTreeNode;
import com.hasten.content.domain.dto.CourseBaseInfoDto;
import com.hasten.content.domain.entity.CourseCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import jdk.jfr.Category;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
public interface ICourseCategoryService extends IService<CourseCategory> {

    List<CategoryTreeNode> queryTreeNodes(String id);

    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

}
