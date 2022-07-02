package io.datalake.dto;

import io.datalake.plugins.common.base.domain.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDao extends Schedule {

    private String resourceName;
    private String userName;
}
