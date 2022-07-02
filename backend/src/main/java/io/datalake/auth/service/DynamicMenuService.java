package io.datalake.auth.service;

import io.datalake.auth.api.dto.DynamicMenuDto;

import java.util.List;

public interface DynamicMenuService {

    List<DynamicMenuDto> load(String userId);
}
