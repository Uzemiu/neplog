package cn.neptu.neplog.service;

import org.springframework.transaction.annotation.Transactional;

public interface VisitService {

    @Transactional
    void increaseVisit(String id, Long increment);
}
