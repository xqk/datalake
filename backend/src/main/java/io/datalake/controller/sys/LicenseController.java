package io.datalake.controller.sys;


import io.datalake.commons.license.DefaultLicenseService;
import io.datalake.commons.license.F2CLicenseResponse;
import io.datalake.controller.ResultHolder;
import io.datalake.exception.DataLakeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@ApiIgnore
@RestController
@RequestMapping(headers = "Accept=application/json")
public class LicenseController {

    @Value("${datalake.need_validate_lic:true}")
    private Boolean need_validate_lic;

    @Resource
    private DefaultLicenseService defaultLicenseService;

    @GetMapping(value = "anonymous/license/validate")
    public ResultHolder validateLicense() throws Exception {
        if (!need_validate_lic) {
            return ResultHolder.success(null);
        }
        F2CLicenseResponse f2CLicenseResponse = defaultLicenseService.validateLicense();
        switch (f2CLicenseResponse.getStatus()) {
            case no_record:
                return ResultHolder.success(f2CLicenseResponse);
            case valid:
                return ResultHolder.success(null);
            case expired:
                String expired = f2CLicenseResponse.getLicense().getExpired();
                DataLakeException.throwException("License has expired since " + expired + ", please update license.");
            case invalid:
                DataLakeException.throwException(f2CLicenseResponse.getMessage());
            default:
                DataLakeException.throwException("Invalid License.");
        }
        return new ResultHolder();
    }
}
