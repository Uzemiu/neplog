package cn.neptu.neplog.service.mapstruct;

import cn.neptu.neplog.model.dto.StorageDTO;
import cn.neptu.neplog.model.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorageMapper extends BaseMapper<StorageDTO, Storage> {
}
