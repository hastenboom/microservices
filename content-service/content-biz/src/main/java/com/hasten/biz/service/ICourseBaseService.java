package com.hasten.biz.service;

import com.hasten.common.domain.PageParams;
import com.hasten.common.domain.PageResult;
import com.hasten.domain.dto.QueryCourseParamsDto;
import com.hasten.domain.entity.CourseBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
public interface ICourseBaseService extends IService<CourseBase> {


    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
