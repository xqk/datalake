package io.datalake.service.staticResource;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.Gson;
import io.datalake.commons.utils.FileUtils;
import io.datalake.commons.utils.LogUtil;
import io.datalake.commons.utils.StaticResourceUtils;
import io.datalake.controller.request.resource.StaticResourceRequest;
import io.datalake.exception.DataLakeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: xqk
 * Date: 2022/4/24
 * Description:
 */
@Service
public class StaticResourceService {

    private final Path staticDir = Paths.get("/opt/datalake/data/static-resource/");

    public void upload(String fileId,MultipartFile file) {
        // check if the path is valid (not outside staticDir)
        Assert.notNull(file, "Multipart file must not be null");
        try {
            String originName = file.getOriginalFilename();
            String newFileName = fileId+originName.substring(originName.lastIndexOf("."),originName.length());
            Path uploadPath = Paths.get(staticDir.toString(), newFileName);
            // create dir is absent
            FileUtils.createIfAbsent(Paths.get(staticDir.toString()));
            Files.createFile(uploadPath);
            file.transferTo(uploadPath);
        } catch (IOException e) {
            LogUtil.error("文件上传失败",e);
            DataLakeException.throwException("文件上传失败");
        } catch (Exception e){
            DataLakeException.throwException(e);
        }
    }

    public void saveFilesToServe(String staticResource){
        Gson gson = new Gson();
        if(StringUtils.isNotEmpty(staticResource)){
            Map<String,String> resource = gson.fromJson(staticResource,Map.class);
            for(Map.Entry<String,String> entry:resource.entrySet()){
                String path = entry.getKey();
                Path uploadPath = Paths.get(staticDir.toString(), path.substring(path.lastIndexOf("/")+1,path.length()));
                try{
                    if (uploadPath.toFile().exists()) {
                        LogUtil.info("file exists");
                    }else{
                        String content = entry.getValue();
                        if(StringUtils.isNotEmpty(content)){
                            Files.createFile(uploadPath);
                            FileCopyUtils.copy(Base64Decoder.decode(content),Files.newOutputStream(uploadPath));
                        }
                    }
                }catch (Exception e){
                    LogUtil.error("template static resource save error"+e.getMessage());
                }
            }
        }
    }

    public Map<String,String> findResourceAsBase64(StaticResourceRequest resourceRequest){
        Map<String,String> result = new HashMap<>();
        if(CollectionUtil.isNotEmpty(resourceRequest.getResourcePathList())){
            for(String path :resourceRequest.getResourcePathList()){
                String value = StaticResourceUtils.getImgFileToBase64(path.substring(path.lastIndexOf("/")+1,path.length()));
                result.put(path,value);
            }
        }
        return  result;
    }
}
