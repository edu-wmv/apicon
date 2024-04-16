package com.br.ufba.icon.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedPointsRepository extends JpaRepository<DeletedPointsEntity, Long> {
}
