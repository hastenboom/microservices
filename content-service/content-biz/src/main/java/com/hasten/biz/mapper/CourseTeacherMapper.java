package com.hasten.biz.mapper;

import com.hasten.domain.entity.CourseTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程-教师关系表 Mapper 接口
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Mapper
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {

}
