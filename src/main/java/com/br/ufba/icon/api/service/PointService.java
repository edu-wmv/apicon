package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.domain.PointEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import com.br.ufba.icon.api.repository.PointRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository repository;
    private final IconicoRepository iconicoRepository;

    public PointService(PointRepository repository, IconicoRepository iconicoRepository) {
        this.repository = repository;
        this.iconicoRepository = iconicoRepository;
    }

    @Transactional
    public void addPoint(@NonNull String uid, String time) {
        Optional<IconicoEntity> iconico = iconicoRepository.findByUid(uid);
        if (iconico.isEmpty()) {
            throw new NotFoundException("Tag não encontrada em Iconico cadastrado");
        }

        Optional<PointEntity> lastPoint = repository.findFirstByUserIdOrderByDateDesc(iconico.get().getId());
        boolean lastStatus;
        if (lastPoint.isEmpty()) {
            lastStatus = false;
        } else {
            lastStatus = lastPoint.get().getStatus();
        }

        // create and send a new point to repository
        PointEntity newPoint = new PointEntity();
        newPoint.setUserId(iconico.get().getId());
        newPoint.setUid(uid);
        newPoint.setUsername(iconico.get().getUsername());
        newPoint.setDate(new Timestamp(0));
        newPoint.setStatus(!lastStatus);

        PointEntity pointSend = repository.save(newPoint);

        // update user points
        String _points_ids = "";
        if (iconico.get().getPoints_ids().isEmpty()) {
            _points_ids.concat(pointSend.getId());
        } else {
           _points_ids.concat(",").concat(pointSend.getId());
        }

        IconicoEntity updateIconico;

    }
}
