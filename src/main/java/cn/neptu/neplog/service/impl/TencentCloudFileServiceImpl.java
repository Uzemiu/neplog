package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.dto.StorageDTO;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.property.TencentCosProperty;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.CosService;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.PropertyService;
import cn.neptu.neplog.service.StorageService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("tencentFileService")
@RequiredArgsConstructor
public class TencentCloudFileServiceImpl implements CosService {

    private final PropertyService propertyService;

    @Override
    public Storage upload(MultipartFile file, UploadFileOption option) {
        TencentCosProperty tencentCosProperty = propertyService.getTencentCosProperty();
        COSClient client = initClient(tencentCosProperty);
        String filename = FileService.generateBaseFileName(file,option).toString();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    tencentCosProperty.getBucketName(), filename, file.getInputStream(),objectMetadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            Storage storage = new Storage();
            storage.setLocation(StorageService.LOCATION_TENCENT_COS);
            storage.setVirtualPath(buildResourceUrl(tencentCosProperty, filename));
            storage.setFilename(filename);
            storage.setHash(putObjectResult.getContentMd5());

            return storage;
        } catch (IOException e) {
            log.error("上传文件到腾讯云失败",e);
            throw new UploadFailureException("上传文件到腾讯云失败: " + e.getMessage());
        } finally {
            client.shutdown();
        }
    }

    @Override
    public boolean delete(Storage storage) {
        return false;
    }

    @Override
    public Object listBuckets(Map<String, String> properties) {
        COSClient client = initClient(new TencentCosProperty(properties));
        try{
            return client.listBuckets();
        } finally {
            client.shutdown();
        }
    }

    @Override
    public boolean isValid(Map<String,String> properties) {
        TencentCosProperty tencentCosProperty = new TencentCosProperty(properties);
        COSClient client = initClient(tencentCosProperty);
        try{
            return client.listBuckets().stream().anyMatch(
                    bucket -> bucket.getName().equals(tencentCosProperty.getBucketName())
                            && bucket.getLocation().equals(tencentCosProperty.getRegion()));
        } finally {
            client.shutdown();
        }
    }

    private COSClient initClient(TencentCosProperty tencentCosProperty){
        COSCredentials cred = new BasicCOSCredentials(
                tencentCosProperty.getSecretId(),
                tencentCosProperty.getSecretKey());
        Region region = new Region(tencentCosProperty.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    private String buildResourceUrl(TencentCosProperty tencentCosProperty, String filename){
        return "https://" +
                tencentCosProperty.getBucketName() +
                ".cos." +
                tencentCosProperty.getRegion() +
                ".myqcloud.com/" +
                filename;
    }
}
