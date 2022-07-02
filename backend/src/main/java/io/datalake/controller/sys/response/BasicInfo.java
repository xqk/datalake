package io.datalake.controller.sys.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasicInfo implements Serializable {

    @ApiModelProperty("请求超时时间")
    private String frontTimeOut;
    @ApiModelProperty("消息保留时间")
    private String msgTimeOut;
    @ApiModelProperty("显示首页")
    private String openHomePage;
    @ApiModelProperty("默认登录方式")
    private Integer loginType = 0;

}
