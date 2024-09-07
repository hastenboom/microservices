package com.hasten.content.domain.dto;

import com.hasten.content.domain.entity.CourseCategory;

import java.util.List;

/**
 * @author Hasten
 */
public class CategoryTreeNode extends CourseCategory {

    List<CategoryTreeNode> children;
}
