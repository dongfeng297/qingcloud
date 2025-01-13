package qingcloud.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CourseRankingVO implements Serializable {
    private String title;
    private String coverUrl;
    private Integer saleNum;

    private Integer rankingPosition;
}
