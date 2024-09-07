package com.hasten.content.biz.mapper;

import com.hasten.content.domain.entity.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    List<CourseCategory> queryTreeNodes(String id);

}
