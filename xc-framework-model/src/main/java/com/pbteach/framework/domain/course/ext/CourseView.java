package com.pbteach.framework.domain.course.ext;

import com.pbteach.framework.domain.course.CourseBase;
import com.pbteach.framework.domain.course.CourseMarket;
import com.pbteach.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-05 11:07
 **/
@Data
@ToString
@NoArgsConstructor
public class CourseView   implements Serializable {
    CourseBase courseBase;//基础信息
    CourseMarket courseMarket;//课程营销
    CoursePic coursePic;//课程图片
    TeachplanNode TeachplanNode;//教学计划

}
