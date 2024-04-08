package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.AddIconicoRequest;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IconicoService {

    private final IconicoRepository repository;

    public IconicoService(IconicoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public IconicoEntity addIconico(@NonNull AddIconicoRequest request) {
        String uid = request.uid();
        boolean existingIconico = repository.existsByUid(uid);

        if (existingIconico) {
            throw new DuplicateKeyException("User already exists by UID");
        }

        IconicoEntity iconico = new IconicoEntity();
        iconico.setName(request.name());
        iconico.setUsername(request.username());
        iconico.setUid(request.uid());
        return repository.save(iconico);
    }

    @Transactional
    public Optional<IconicoEntity> getUserById(@NonNull Long id) {
        Optional<IconicoEntity> iconicoExists = repository.findById(id);

        if (iconicoExists.isEmpty()) {
            throw new NotFoundException("Iconico não encontrado por ID");
        }

        return iconicoExists;
    }

    private Long convertTimeToMilli(String time) {
        System.out.println(time);
        int hour = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3,5));
        int seconds = Integer.parseInt(time.substring(6,8));

        long total = ((long) hour * 60 * 60 * 1000) + ((long) minutes * 60 * 1000) + (seconds * 1000L);
        System.out.println(total);
        return total;
    }

}
