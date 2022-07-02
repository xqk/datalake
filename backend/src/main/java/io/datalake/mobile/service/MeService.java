package io.datalake.mobile.service;

import io.datalake.commons.utils.AuthUtils;
import io.datalake.ext.MobileMeMapper;
import io.datalake.mobile.dto.MeItemDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MeService {

    @Resource
    private MobileMeMapper mobileMeMapper;

    public MeItemDTO personInfo() {
        return mobileMeMapper.query(AuthUtils.getUser().getUserId());
    }
}
