package io.datalake.mobile.dto;

import lombok.Data;

@Data
public class HomeItemShareDTO extends HomeItemDTO {

    private String nickName;

    private Long userId;
}
