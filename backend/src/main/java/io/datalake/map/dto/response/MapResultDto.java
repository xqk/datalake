package io.datalake.map.dto.response;

import io.datalake.map.dto.entity.Feature;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class MapResultDto implements Serializable {

    private String type;

    private List<Feature> features;
}
