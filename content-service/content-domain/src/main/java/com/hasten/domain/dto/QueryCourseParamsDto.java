package com.hasten.domain.dto;

import lombok.Data;

/**
 * @author Hasten
 */
@Data
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;


}
