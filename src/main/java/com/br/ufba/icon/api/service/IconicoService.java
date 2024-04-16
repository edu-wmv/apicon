package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.AddIconicoRequest;
import com.br.ufba.icon.api.controller.dto.RecalculateHoursResponse;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.domain.PointEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import com.br.ufba.icon.api.repository.PointRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.NotActiveException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IconicoService {

    private final IconicoRepository repository;
    private final PointService pointService;

    public IconicoService(IconicoRepository repository, PointRepository pointRepository, DeletedPointsService deletedPointsService, PointService pointService) {
        this.repository = repository;
        this.pointService = pointService;
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

    @Transactional
    public void recalculateAllHours() throws NotActiveException {
        List<IconicoEntity> iconicos = repository.findAll();
        for (IconicoEntity iconico : iconicos) {
            recalculateHours(iconico);
            System.out.println("Recalculating hours for: " + iconico.getName());
        }
    }

    private void recalculateHours(@NotNull IconicoEntity iconico) throws NotActiveException {

        List<PointEntity> points = pointService.checkUserPoints(iconico.getId());

        if (points.size() != iconico.getPoints_ids().split(",").length) {
            System.out.println("Points diff");
        }

        if (points.size() % 2 == 1) {
            throw new NotActiveException("Ponto de entrada sem saída");
        }

        List<PointEntity[]> pairs = new ArrayList<>();
        StringBuilder points_ids = new StringBuilder(36*points.size());

        for (int i = 0; i < points.size() - 1; i += 2) {
            pairs.add(new PointEntity[]{points.get(i), points.get(i+1)});
        }

        long milliseconds = 10800000;
        for (PointEntity[] pair : pairs) {
            Timestamp _in = pair[0].getDate();
            Timestamp _out = pair[1].getDate();

            System.out.println("in: " + _in);
            System.out.println("out: " + _out);

            long diff = _out.getTime() - _in.getTime();

            System.out.println("diff: " + diff);
            milliseconds += diff;
        }

        for (PointEntity[] pair: pairs) {
            String _in = pair[0].getId();
            String _out = pair[1].getId();

            points_ids.append(_in).append(",").append(_out).append(",");
        }

        iconico.setHours(new Timestamp(milliseconds));
        iconico.setPoints_ids(points_ids.toString());
        repository.save(iconico);
    }

    @Transactional
    public List<IconicoEntity> returnMainData() {
        List<IconicoEntity> iconicos = repository.findAll();
        for (IconicoEntity iconico : iconicos) {
            System.out.println(iconico.getName());
            System.out.println(iconico.getUsername());
            System.out.println(iconico.getUid());
            System.out.println(iconico.getHours());
            System.out.println(iconico.getStatus());
            System.out.println(iconico.getPoints_ids());
        }

        return iconicos;
    }

    @Transactional
    public void closeAllIconicos() {
        List<IconicoEntity> iconicos = repository.findAll();
        for (IconicoEntity iconico : iconicos) {
            if (iconico.getStatus()) {
                pointService.deleteUserLastPoint(iconico);
            }

            iconico.setStatus(false);
            repository.save(iconico);
        }
    }

}
