package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.AddIconicoRequest;
import com.br.ufba.icon.api.controller.dto.RecalculateHoursResponse;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.domain.PointEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import com.br.ufba.icon.api.repository.PointRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class IconicoService {

    private final IconicoRepository repository;
    private final PointRepository pointRepository;

    public IconicoService(IconicoRepository repository, PointRepository pointRepository) {
        this.repository = repository;
        this.pointRepository = pointRepository;
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

    @Transactional
    public RecalculateHoursResponse recalculateHours(@NonNull Long userId) {
        Optional<IconicoEntity> iconicoExists = repository.findById(userId);

        if (iconicoExists.isEmpty()) {
            throw new NotFoundException("Iconico não encontrado por ID");
        }

        IconicoEntity iconico = iconicoExists.get();

        if (iconico.getPoints_ids() == null) {
            iconico.setHours(new Timestamp(10800000));
            repository.save(iconico);
            return new RecalculateHoursResponse(200, "User has no points", "no data");
        }

        if ((iconico.getPoints_ids().split(",").length) % 2 == 1) {
            return new RecalculateHoursResponse(428, "Usuario ainda logado", "no data");
        }

        List<PointEntity> points = pointRepository.findAllByUserIdOrderByDate(userId);
        List<PointEntity[]> pairs = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i += 2) {
            pairs.add(new PointEntity[]{points.get(i), points.get(i+1)});
        }

        long milliseconds = 10800000;
        for (PointEntity[] pair : pairs) {
            Timestamp _in = pair[0].getDate();
            Timestamp _out = pair[1].getDate();

            long diff = _out.getTime() - _in.getTime();

            System.out.println("diff: " + diff);
            milliseconds += diff;
        }

        System.out.println(new Timestamp(milliseconds));


//        return new RecalculateHoursResponse(code, message, data);
        return null;
    }
}
