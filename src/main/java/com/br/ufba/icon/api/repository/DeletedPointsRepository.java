package com.br.ufba.icon.api.repository;

import com.br.ufba.icon.api.domain.DeletedPointsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedPointsRepository extends JpaRepository<DeletedPointsEntity, Long> {
}
