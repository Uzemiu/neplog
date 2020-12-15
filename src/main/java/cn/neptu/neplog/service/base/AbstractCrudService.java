package cn.neptu.neplog.service.base;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.repository.BaseRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<ENTITY, ID> implements CrudService<ENTITY, ID>{

    private final String entityName;

    private final BaseRepository<ENTITY, ID> repository;

    protected AbstractCrudService(BaseRepository<ENTITY, ID> repository) {
        this.repository = repository;
        Class<ENTITY> actualClass = (Class<ENTITY>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityName =  actualClass.getSimpleName();
    }

    @Override
    public ENTITY create(ENTITY entity) {
        return repository.save(entity);
    }

    @Override
    public List<ENTITY> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ENTITY> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public ENTITY getNotNullById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException(entityName + "(id: " + id + ") not fount" ));
    }

    @Override
    public ENTITY update(ENTITY entity) {
        return repository.save(entity);
    }

    @Override
    public ENTITY removeById(ID id) {
        ENTITY entity = getNotNullById(id);
        repository.delete(entity);
        return entity;
    }

    @Override
    public long count() {
        return repository.count();
    }
}
