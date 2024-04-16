package com.br.ufba.icon.api.repository;

import com.br.ufba.icon.api.domain.IconicoEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface IconicoRepository extends JpaRepository<IconicoEntity, Long> {

    boolean existsById(@NonNull Long id);
    Boolean existsByUid(@NonNull String uid);

    Optional<IconicoEntity> findByUid(@NonNull String uid);

    @NotNull
    List<IconicoEntity> findAll();
}
