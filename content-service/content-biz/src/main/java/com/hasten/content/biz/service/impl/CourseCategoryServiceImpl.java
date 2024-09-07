package com.hasten.content.biz.service.impl;

import com.hasten.content.biz.mapper.CourseCategoryMapper;
import com.hasten.content.biz.service.ICourseCategoryService;
import com.hasten.content.domain.dto.AddCourseDto;
import com.hasten.content.domain.dto.CategoryTreeNode;
import com.hasten.content.domain.dto.CourseBaseInfoDto;
import com.hasten.content.domain.entity.CourseCategory;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Service
@RequiredArgsConstructor
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements ICourseCategoryService {

    private final CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CategoryTreeNode> queryTreeNodes(String id) {

        List<CourseCategory> courseCategories = courseCategoryMapper.queryTreeNodes(id);

        return List.of();
    }

    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        return null;
    }
}
