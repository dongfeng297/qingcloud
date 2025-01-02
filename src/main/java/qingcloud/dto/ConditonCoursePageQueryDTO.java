package qingcloud.dto;

import lombok.Data;

@Data
public class ConditonCoursePageQueryDTO {
    private int page;
    private int pageSize;
    private String grade;
    private int courseType;
    //课程状态 1表示上架
    private int status;

}
