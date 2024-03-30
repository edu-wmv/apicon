package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.AddPointRequest;
import com.br.ufba.icon.api.controller.dto.AddPointResponse;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.domain.PointEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import com.br.ufba.icon.api.repository.PointRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    public AddPointResponse addPoint(@NonNull AddPointRequest request) {
        // request.time() = "YYYY-MM-DD HH:MM:SS"
        Optional<IconicoEntity> iconico = iconicoRepository.findByUid(request.uid());
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
        newPoint.setUid(request.uid());
        newPoint.setUsername(iconico.get().getUsername());
        newPoint.setDate(Timestamp.valueOf(request.time()));
        newPoint.setStatus(!lastStatus);

        PointEntity pointSend = repository.save(newPoint);

        // update user points
        String _points_ids = "";
        String points_ids;
        if (iconico.get().getPoints_ids().isEmpty()) {
            points_ids = _points_ids.concat(pointSend.getId());
        } else {
           points_ids = _points_ids.concat(",").concat(pointSend.getId());
        }

        // check for user hours
        Timestamp _final;
        String message;
        String code;
        if (lastStatus) {
            Timestamp in = lastPoint.get().getDate();
            Timestamp out = Timestamp.valueOf(request.time());

            Timestamp diff = new Timestamp((out.getTime() - in.getTime()));

            _final = new Timestamp((iconico.get().getHours().getTime() + diff.getTime()));

            message = String.format("Até mais %s", iconico.get().getUsername());
            code = "bye";
        } else {
            _final = iconico.get().getHours();
            message = String.format("Olá %s", iconico.get().getUsername());
            code = "welcome";
        }

        // update user status
        IconicoEntity updateIconico = new IconicoEntity();
        updateIconico.setStatus(!lastStatus);
        updateIconico.setPoints_ids(points_ids);
        updateIconico.setHours(_final);
        iconicoRepository.save(updateIconico);

        return new AddPointResponse(message, code, iconico.get().getUsername());
    }
}
