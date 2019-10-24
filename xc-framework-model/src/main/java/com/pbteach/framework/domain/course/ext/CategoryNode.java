package com.pbteach.framework.domain.course.ext;

import com.pbteach.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/2/7.
 */
@Data
@ToString
public class CategoryNode extends Category {

    List<CategoryNode> children;

}
