package qingcloud.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherRankingVO implements Serializable {
    private String name;
    private String avatar;
    private Integer fansNum;
    private Integer rankingPosition;
}
