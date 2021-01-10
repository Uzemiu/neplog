package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.PropertyService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service("tencent")
@RequiredArgsConstructor
public class TencentCloudFileServiceImpl implements FileService {

    private final PropertyService propertyService;

    private COSClient client;

    @Override
    public String upload(MultipartFile file, UploadFileOption option) {
        initClient();
        String filename = FileService.generateBaseFileName(file,option).append("_tmp").toString();
        File tmp = new File(filename).getAbsoluteFile();
        try {
            file.transferTo(tmp);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(String path) {
        return false;
    }

    private void initClient(){
        if(client != null){
            return;
        }
        TencentCosProperty tencentCosProperty = propertyService.getTencentCosProperty();

            COSCredentials cred = new BasicCOSCredentials(
                    tencentCosProperty.getSecretId(),
                    tencentCosProperty.getSecretKey());
            Region region = new Region(tencentCosProperty.getRegion());
            ClientConfig clientConfig = new ClientConfig(region);
            this.client =  new COSClient(cred, clientConfig);
    }
}
