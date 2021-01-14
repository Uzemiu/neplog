package cn.neptu.neplog.service;

import cn.neptu.neplog.model.entity.Storage;
import cn.neptu.neplog.model.query.StorageQuery;
import cn.neptu.neplog.model.support.UploadFileOption;
import cn.neptu.neplog.service.base.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService extends CrudService<Storage, Long> {

    String LOCATION_LOCAL = "local";

    String LOCATION_TENCENT_COS = "tencent";

    Page<Storage> listFiles(StorageQuery query, Pageable pageable);

    Storage upload(MultipartFile file);

    Storage upload(MultipartFile file, String option);

    Storage upload(MultipartFile file, String option, String location);

    Storage delete(String path);
}
