package com.br.ufba.icon.api.repository;

import com.br.ufba.icon.api.domain.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {

    Optional<PointEntity> findFirstByUserIdOrderByDateDesc(Long userId);

    List<PointEntity> findAllByUserIdOrderByDate(Long userId);
}
