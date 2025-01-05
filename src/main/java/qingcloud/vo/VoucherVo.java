package qingcloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VoucherVo {

    private Long id;


    private String title;


    private String subTitle;


    private String rules;


    private Long payValue;


    private Long actualValue;


}
