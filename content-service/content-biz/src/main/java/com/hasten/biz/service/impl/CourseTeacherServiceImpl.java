package com.hasten.biz.service.impl;

import com.hasten.biz.mapper.CourseTeacherMapper;
import com.hasten.biz.service.ICourseTeacherService;
import com.hasten.domain.entity.CourseTeacher;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

}
