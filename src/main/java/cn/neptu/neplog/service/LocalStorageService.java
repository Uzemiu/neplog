package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.LocalStorage;
import cn.neptu.neplog.service.base.CrudService;

import java.util.List;

public interface LocalStorageService extends CrudService<LocalStorage, Long> {

    List<LocalStorage> listByName(String name);
}
