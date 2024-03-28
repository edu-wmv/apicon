package com.br.ufba.icon.api.repository;

import com.br.ufba.icon.api.domain.IconicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;


public interface IconicoRepository extends JpaRepository<IconicoEntity, Long> {

    boolean existsById(@NonNull Long id);
    Boolean existsByUid(@NonNull String uid);
}
