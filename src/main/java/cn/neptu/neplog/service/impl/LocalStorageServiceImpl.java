package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.model.entity.LocalStorage;
import cn.neptu.neplog.repository.BaseRepository;
import cn.neptu.neplog.repository.LocalStorageRepository;
import cn.neptu.neplog.service.LocalStorageService;
import cn.neptu.neplog.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("localStorageService")
public class LocalStorageServiceImpl extends AbstractCrudService<LocalStorage, Long> implements LocalStorageService {

    private final LocalStorageRepository localStorageRepository;

    public LocalStorageServiceImpl(LocalStorageRepository localStorageRepository) {
        super(localStorageRepository);
        this.localStorageRepository = localStorageRepository;
    }

    @Override
    public List<LocalStorage> listByName(String name) {
        return localStorageRepository.findByName(name);
    }
}
