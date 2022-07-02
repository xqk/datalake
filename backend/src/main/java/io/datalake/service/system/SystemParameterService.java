package io.datalake.service.system;

import io.datalake.commons.constants.ParamConstants;
import io.datalake.commons.exception.DEException;
import io.datalake.commons.utils.BeanUtils;
import io.datalake.commons.utils.EncryptUtils;
import io.datalake.controller.sys.response.BasicInfo;
import io.datalake.dto.SystemParameterDTO;
import io.datalake.ext.*;
import io.datalake.plugins.common.base.domain.FileMetadata;
import io.datalake.plugins.common.base.domain.SystemParameter;
import io.datalake.plugins.common.base.domain.SystemParameterExample;
import io.datalake.plugins.common.base.mapper.SystemParameterMapper;
import io.datalake.service.FileService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemParameterService {

    private final static String LOGIN_TYPE_KEY = "basic.loginType";
    @Resource
    private SystemParameterMapper systemParameterMapper;
    @Resource
    private ExtSystemParameterMapper extSystemParameterMapper;
    @Resource
    private FileService fileService;

    public String searchEmail() {
        return extSystemParameterMapper.email();
    }

    public BasicInfo basicInfo() {
        List<SystemParameter> paramList = this.getParamList("basic");
        List<SystemParameter> homePageList = this.getParamList("ui.openHomePage");
        paramList.addAll(homePageList);
        BasicInfo result = new BasicInfo();
        result.setOpenHomePage("true");
        if (!CollectionUtils.isEmpty(paramList)) {
            for (SystemParameter param : paramList) {
                if (StringUtils.equals(param.getParamKey(), ParamConstants.BASIC.FRONT_TIME_OUT.getValue())) {
                    result.setFrontTimeOut(param.getParamValue());
                }
                if (StringUtils.equals(param.getParamKey(), ParamConstants.BASIC.MSG_TIME_OUT.getValue())) {
                    result.setMsgTimeOut(param.getParamValue());
                }
                if (StringUtils.equals(param.getParamKey(), ParamConstants.BASIC.DEFAULT_LOGIN_TYPE.getValue())) {
                    String paramValue = param.getParamValue();
                    result.setLoginType(StringUtils.isNotBlank(paramValue) ? Integer.parseInt(paramValue) : 0);
                }
                if (StringUtils.equals(param.getParamKey(), ParamConstants.BASIC.OPEN_HOME_PAGE.getValue())) {
                    boolean open = StringUtils.equals("true", param.getParamValue());
                    result.setOpenHomePage(open ? "true" : "false");
                }
            }
        }
        return result;
    }

    public String getSystemLanguage() {
        String result = StringUtils.EMPTY;
        SystemParameterExample example = new SystemParameterExample();
        example.createCriteria().andParamKeyEqualTo(ParamConstants.I18n.LANGUAGE.getValue());
        List<SystemParameter> list = systemParameterMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            String value = list.get(0).getParamValue();
            if (StringUtils.isNotBlank(value)) {
                result = value;
            }
        }
        return result;
    }

    public void editBasic(List<SystemParameter> parameters) {
        parameters.forEach(parameter -> {
            SystemParameterExample example = new SystemParameterExample();

            example.createCriteria().andParamKeyEqualTo(parameter.getParamKey());
            if (systemParameterMapper.countByExample(example) > 0) {
                systemParameterMapper.updateByPrimaryKey(parameter);
            } else {
                systemParameterMapper.insert(parameter);
            }
            example.clear();

        });
    }

    public List<SystemParameter> getParamList(String type) {
        SystemParameterExample example = new SystemParameterExample();
        example.createCriteria().andParamKeyLike(type + "%");
        return systemParameterMapper.selectByExample(example);
    }

    public String getVersion() {
        return System.getenv("MS_VERSION");
    }

    public void saveLdap(List<SystemParameter> parameters) {
        SystemParameterExample example = new SystemParameterExample();
        parameters.forEach(param -> {
            if (param.getParamKey().equals(ParamConstants.LDAP.PASSWORD.getValue())) {
                String string = EncryptUtils.aesEncrypt(param.getParamValue()).toString();
                param.setParamValue(string);
            }
            example.createCriteria().andParamKeyEqualTo(param.getParamKey());
            if (systemParameterMapper.countByExample(example) > 0) {
                systemParameterMapper.updateByPrimaryKey(param);
            } else {
                systemParameterMapper.insert(param);
            }
            example.clear();
        });
    }

    public String getValue(String key) {
        SystemParameter param = systemParameterMapper.selectByPrimaryKey(key);
        if (param == null) {
            return null;
        }
        return param.getParamValue();
    }

    public Integer defaultLoginType() {
        String value = getValue(LOGIN_TYPE_KEY);
        return StringUtils.isNotBlank(value) ? Integer.parseInt(value) : 0;
    }

    public List<SystemParameterDTO> getSystemParameterInfo(String paramConstantsType) {
        List<SystemParameter> paramList = this.getParamList(paramConstantsType);
        List<SystemParameterDTO> dtoList = new ArrayList<>();
        for (SystemParameter systemParameter : paramList) {
            SystemParameterDTO systemParameterDTO = new SystemParameterDTO();
            BeanUtils.copyBean(systemParameterDTO, systemParameter);
            if (systemParameter.getType().equalsIgnoreCase("file")) {
                FileMetadata fileMetadata = fileService.getFileMetadataById(systemParameter.getParamValue());
                if (fileMetadata != null) {
                    systemParameterDTO.setFileName(fileMetadata.getName());
                }
            }
            dtoList.add(systemParameterDTO);
        }
        dtoList.sort(Comparator.comparingInt(SystemParameter::getSort));
        return dtoList;
    }

    public void saveUIInfo(Map<String, List<SystemParameterDTO>> request, List<MultipartFile> bodyFiles)
            throws IOException {
        List<SystemParameterDTO> parameters = request.get("systemParams");
        if (null != bodyFiles)
            for (MultipartFile multipartFile : bodyFiles) {
                if (!multipartFile.isEmpty()) {
                    // 防止添加非图片文件
                    try (InputStream input = multipartFile.getInputStream()) {
                        try {
                            // It's an image (only BMP, GIF, JPG and PNG are recognized).
                            ImageIO.read(input).toString();
                        } catch (Exception e) {
                            DEException.throwException("Uploaded images do not meet the image format requirements");
                            return;
                        }
                    }
                    String multipartFileName = multipartFile.getOriginalFilename();
                    String[] split = Objects.requireNonNull(multipartFileName).split(",");
                    parameters.stream()
                            .filter(systemParameterDTO -> systemParameterDTO.getParamKey().equalsIgnoreCase(split[1]))
                            .forEach(systemParameterDTO -> {
                                systemParameterDTO.setFileName(split[0]);
                                systemParameterDTO.setFile(multipartFile);
                            });
                }
            }
        for (SystemParameterDTO systemParameter : parameters) {
            MultipartFile file = systemParameter.getFile();
            if (systemParameter.getType().equalsIgnoreCase("file")) {
                if (StringUtils.isBlank(systemParameter.getFileName())) {
                    fileService.deleteFileById(systemParameter.getParamValue());
                }
                if (file != null) {
                    fileService.deleteFileById(systemParameter.getParamValue());
                    FileMetadata fileMetadata = fileService.saveFile(systemParameter.getFile(),
                            systemParameter.getFileName());
                    systemParameter.setParamValue(fileMetadata.getId());
                }
                if (file == null && systemParameter.getFileName() == null) {
                    systemParameter.setParamValue(null);
                }
            }
            systemParameterMapper.deleteByPrimaryKey(systemParameter.getParamKey());
            systemParameterMapper.insert(systemParameter);
        }

    }

}
