package cn.neptu.neplog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.exception.UploadFailureException;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.config.TencentCosConfig;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.repository.TencentCosConfigRepository;
import cn.neptu.neplog.service.CosService;
import cn.neptu.neplog.service.FileService;
import cn.neptu.neplog.service.ConfigService;
import cn.neptu.neplog.service.StorageService;
import cn.neptu.neplog.service.base.AbstractConfigService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service("tencentFileService")
public class TencentCloudFileServiceImpl
        extends AbstractConfigService<TencentCosConfig, Long>
        implements CosService, ConfigService<TencentCosConfig, Long> {

    private final TencentCosConfigRepository repository;

    protected TencentCloudFileServiceImpl(TencentCosConfigRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Storage upload(MultipartFile file, UploadFileOption option) {
        TencentCosConfig tencentCosConfig = repository.findAll().get(0);
        COSClient client = initClient(tencentCosConfig);
        String filename = FileService.generateBaseFileName(file,option).toString();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    tencentCosConfig.getBucketName(), filename, file.getInputStream(),objectMetadata);

            PutObjectResult putObjectResult = client.putObject(putObjectRequest);

            Storage storage = new Storage();
            storage.setLocation(StorageService.LOCATION_TENCENT_COS);
            storage.setVirtualPath(buildResourceUrl(tencentCosConfig, filename));
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
        TencentCosConfig config = BeanUtil.mapToBean(properties, TencentCosConfig.class, true);
        COSClient client = initClient(config);
        try{
            return client.listBuckets();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        } finally {
            client.shutdown();
        }
    }

    @Override
    public boolean isValid(Map<String,String> properties) {
        TencentCosConfig config = BeanUtil.mapToBean(properties, TencentCosConfig.class, true);
        COSClient client = initClient(config);
        try{
            return client.listBuckets().stream().anyMatch(
                    bucket -> bucket.getName().equals(config.getBucketName())
                            && bucket.getLocation().equals(config.getRegion()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        } finally {
            client.shutdown();
        }
    }

    private COSClient initClient(TencentCosConfig tencentCosConfig){
        COSCredentials cred = new BasicCOSCredentials(
                tencentCosConfig.getSecretId(),
                tencentCosConfig.getSecretKey());
        Region region = new Region(tencentCosConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    private String buildResourceUrl(TencentCosConfig tencentCosConfig, String filename){
        return "https://" +
                tencentCosConfig.getBucketName() +
                ".cos." +
                tencentCosConfig.getRegion() +
                ".myqcloud.com/" +
                filename;
    }
}
