package io.datalake.ext;

import io.datalake.mobile.dto.MeItemDTO;

public interface MobileMeMapper {

    MeItemDTO query(Long userId);
}
