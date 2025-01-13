package qingcloud.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class VoucherVO implements Serializable {

    private Long id;


    private String title;


    private String subTitle;


    private String rules;


    private Long payValue;


    private Long actualValue;


}
