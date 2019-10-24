package com.pbteach.framework.domain.course.ext;

import com.pbteach.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/2/7.
 */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    //课程计划媒资信息
    private String mediaId;
    private String mediaFileOriginalName;

}
