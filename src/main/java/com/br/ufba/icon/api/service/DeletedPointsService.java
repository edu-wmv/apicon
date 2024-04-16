package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.domain.DeletedPointsEntity;
import com.br.ufba.icon.api.repository.DeletedPointsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DeletedPointsService {

    DeletedPointsRepository repository;

    public DeletedPointsService(DeletedPointsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addDeletedPoint(DeletedPointsEntity entity) {
        repository.save(entity);
    }
}
