package io.datalake.service;

import io.datalake.commons.constants.AuthConstants;
import io.datalake.commons.license.DefaultLicenseService;
import io.datalake.commons.license.F2CLicenseResponse;
import io.datalake.commons.utils.DateUtils;
import io.datalake.commons.utils.LogUtil;
import io.datalake.listener.util.CacheUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Optional;

@Service
public class AboutService {
    private static final String BUILD_VERSION = "/opt/datalake/conf/version";
    private static final String product = "DataLake";

    @Resource
    private DefaultLicenseService defaultLicenseService;


    @Value("${version}")
    private String version;

    public F2CLicenseResponse updateLicense(String licenseKey) {
        F2CLicenseResponse f2CLicenseResponse = defaultLicenseService.updateLicense(product, licenseKey);
        Optional.ofNullable(f2CLicenseResponse).ifPresent(resp -> {
            if (resp.getStatus() == F2CLicenseResponse.Status.valid) {
                String dateStr = f2CLicenseResponse.getLicense().getExpired();
                LogUtil.info("update valid lic, expired date is {}", dateStr);
                try {
                    Date date = DateUtils.getDate(dateStr);
                    CacheUtils.updateLicCache(date);
                    CacheUtils.removeAll(AuthConstants.USER_CACHE_NAME);
                    CacheUtils.removeAll(AuthConstants.USER_ROLE_CACHE_NAME);
                    CacheUtils.removeAll(AuthConstants.USER_PERMISSION_CACHE_NAME);
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        });
        return f2CLicenseResponse;
    }

    public F2CLicenseResponse validateLicense(String licenseKey) {
        if (StringUtils.isNotBlank(licenseKey)) {
            return defaultLicenseService.validateLicense(product, licenseKey);
        } else {
            return defaultLicenseService.validateLicense();
        }
    }

    public String getBuildVersion() {
        try {
            File file = new File(BUILD_VERSION);
            if (file.exists()) {
                String version = FileUtils.readFileToString(file, "UTF-8");
                if (StringUtils.isNotBlank(version)) {
                    return version;
                }
            }
            return Optional.ofNullable(version).orElse("V1.0");
        } catch (Exception e) {
            LogUtil.error("failed to get build version.", e);
        }
        return "unknown";
    }
}
