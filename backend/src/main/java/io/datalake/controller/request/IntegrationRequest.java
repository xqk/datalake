package io.datalake.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationRequest {
    private String platform;
    private String orgId;
}
