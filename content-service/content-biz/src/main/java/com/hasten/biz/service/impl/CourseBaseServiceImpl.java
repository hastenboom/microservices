package com.hasten.biz.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasten.biz.mapper.CourseBaseMapper;
import com.hasten.biz.service.ICourseBaseService;
import com.hasten.common.domain.PageParams;
import com.hasten.common.domain.PageResult;
import com.hasten.domain.dto.QueryCourseParamsDto;
import com.hasten.domain.entity.CourseBase;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Service
@RequiredArgsConstructor
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements ICourseBaseService {

    private final CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams,
                                                      QueryCourseParamsDto queryCourseParamsDto)
    {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus
                , queryCourseParamsDto.getAuditStatus());
        queryWrapper.like(StringUtils.isNotBlank(queryCourseParamsDto.getCourseName()), CourseBase::getName,
                queryCourseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getPublishStatus()), CourseBase::getStatus,
                queryCourseParamsDto.getPublishStatus());

        Page<CourseBase> page = courseBaseMapper.selectPage(
                new Page<>(pageParams.getPageNo(), pageParams.getPageSize()),
                queryWrapper
        );
        System.out.println("hhfsldkfjalf");

        return new PageResult<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize()
        );
    }
}
