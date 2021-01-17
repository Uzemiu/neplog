package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.config.FileServiceFactory;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.repository.StorageRepository;
import cn.neptu.neplog.service.StorageService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("storageService")
public class StorageServiceImpl extends AbstractCrudService<Storage, Long> implements StorageService {

    private final StorageRepository storageRepository;
    private final FileServiceFactory fileServiceFactory;
    private final UploadFileConfig configuration;

    protected StorageServiceImpl(StorageRepository repository,
                                 FileServiceFactory fileServiceFactory,
                                 UploadFileConfig configuration) {
        super(repository);
        this.storageRepository = repository;
        this.fileServiceFactory = fileServiceFactory;
        this.configuration = configuration;
    }

    @Override
    public Page<Storage> listFiles(StorageQuery query, Pageable pageable) {
        return storageRepository.findAll(query.toSpecification(),pageable);
    }

    @Override
    public Storage upload(MultipartFile file) {
        return upload(file, "file");
    }

    @Override
    public Storage upload(MultipartFile file, String option) {
        return upload(file, option, LOCATION_DEFAULT);
    }

    @Override
    public Storage upload(MultipartFile file, String option, String location) {
        Storage result =  fileServiceFactory.getFileService(location).upload(file,configuration.get(location));
        return storageRepository.save(result);
    }

    @Override
    public Storage delete(String path) {
        Storage storage = storageRepository.findByFilePath(path)
                .orElseThrow(() -> new ResourceNotFoundException("无法从数据库获取文件路径: " + path));
        if(fileServiceFactory.getFileService(storage.getLocation()).delete(storage)){
            storageRepository.delete(storage);
        }
        return storage;
    }
}
