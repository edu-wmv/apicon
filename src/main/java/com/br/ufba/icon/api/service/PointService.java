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

        System.out.println("found");
        Optional<PointEntity> lastPoint = repository.findFirstByUserIdOrderByDateDesc(iconico.get().getId());
        boolean lastStatus;
        if (lastPoint.isEmpty()) {
            lastStatus = false;
        } else {
            lastStatus = lastPoint.get().getStatus();
        }
        System.out.println("lastStatus: " + lastStatus);

        // create and send a new point to repository
        PointEntity newPoint = new PointEntity();
        newPoint.setUserId(iconico.get().getId());
        newPoint.setUid(request.uid());
        newPoint.setUsername(iconico.get().getUsername());
        newPoint.setDate(Timestamp.valueOf(request.time()));
        newPoint.setStatus(!lastStatus);
        System.out.println("Point created");

        PointEntity pointSend = repository.save(newPoint);
        System.out.println("point saved");

        // update user points
        String _points_ids = String.valueOf(iconico.get().getPoints_ids());
        String points_ids;
        if (_points_ids.isEmpty()) {
            points_ids = _points_ids.concat(pointSend.getId());
        } else {
           points_ids = _points_ids.concat(",").concat(pointSend.getId());
        }
        System.out.println(points_ids);
        System.out.println("updated");

        // check for user hours
        Timestamp _final;
        String message;
        String code;
        if (lastStatus) {
            Timestamp in = lastPoint.get().getDate();
            Timestamp out = Timestamp.valueOf(request.time());

            Timestamp diff = new Timestamp((out.getTime() - in.getTime()));

            _final = new Timestamp((iconico.get().getHours().getTime() + diff.getTime()));

            message = String.format("Ate mais %s", iconico.get().getUsername());
            code = "bye";
        } else {
            _final = iconico.get().getHours();
            message = String.format("Ola %s", iconico.get().getUsername());
            code = "welcome";
        }
        System.out.println("hours checked");

        // update user status
        IconicoEntity updateIconico = iconico.get();
        updateIconico.setStatus(!lastStatus);
        updateIconico.setPoints_ids(points_ids);
        updateIconico.setHours(_final);
        iconicoRepository.save(updateIconico);
        System.out.println("Send!");

        return new AddPointResponse(message, code, iconico.get().getUsername());
    }
}
