package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.factory.FileServiceFactory;
import cn.neptu.neplog.config.common.UploadFileConfig;
import cn.neptu.neplog.exception.ResourceNotFoundException;
import cn.neptu.neplog.model.dto.PageDTO;
import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.repository.StorageRepository;
import cn.neptu.neplog.service.StorageService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

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
    public PageDTO<Storage> queryBy(StorageQuery query, Pageable pageable) {
        Page<Storage> storage = storageRepository.findAll(query.toSpecification(),pageable);
        return new PageDTO<>(storage);
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
        Storage result = fileServiceFactory.getFileService(location).upload(file,configuration.get(option));
        return storageRepository.save(result);
    }

    @Override
    public Storage deleteById(Long id) {
        Storage storage = getNotNullById(id);
        if(fileServiceFactory.getFileService(storage.getLocation()).delete(storage)){
            storageRepository.delete(storage);
        }
        return storage;
    }

    @Override
    public long deleteByIdIn(Collection<Long> longs) {
        long count = 0;
        for (Long aLong : longs) {
           count += deleteById(aLong) == null ? 0 : 1;
        }
        return count;
    }
}
