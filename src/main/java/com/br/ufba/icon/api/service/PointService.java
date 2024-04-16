package com.br.ufba.icon.api.service;

import com.br.ufba.icon.api.controller.dto.AddPointRequest;
import com.br.ufba.icon.api.controller.dto.AddPointResponse;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.domain.PointEntity;
import com.br.ufba.icon.api.exceptions.NotFoundException;
import com.br.ufba.icon.api.repository.IconicoRepository;
import com.br.ufba.icon.api.repository.PointRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository repository;
    private final IconicoRepository iconicoRepository;
    private final DeletedPointsService deletedPointsService;

    public PointService(PointRepository repository, IconicoRepository iconicoRepository, DeletedPointsService deletedPointsService) {
        this.repository = repository;
        this.iconicoRepository = iconicoRepository;
        this.deletedPointsService = deletedPointsService;
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

    @Transactional
    public void deleteUserLastPoint(@NonNull IconicoEntity entity) {
        List<PointEntity> points = repository.findAllByUserIdOrderByDate(entity.getId());
        if (!points.isEmpty()) {
            repository.delete(points.getLast());
        }

        entity.setPoints_ids(entity.getPoints_ids().substring(0, entity.getPoints_ids().lastIndexOf(",")));
        System.out.println(entity.getName() + " last point deleted");

        DeletedPointsEntity deletedPoint = getDeletedPointsEntity(points);
        deletedPointsService.addDeletedPoint(deletedPoint);

    }

    private static @NotNull DeletedPointsEntity getDeletedPointsEntity(@NotNull List<PointEntity> points) {
        DeletedPointsEntity deletedPoint = new DeletedPointsEntity();
        deletedPoint.setId(points.getLast().getId());
        deletedPoint.setDate(points.getLast().getDate());
        deletedPoint.setStatus(points.getLast().getStatus());
        deletedPoint.setUsername(points.getLast().getUsername());
        deletedPoint.setUid(points.getLast().getUid());
        deletedPoint.setUser_id(points.getLast().getUserId());
        deletedPoint.setReason("User logged out after 22:00");
        return deletedPoint;
    }

    @Transactional
    public List<PointEntity> checkUserPoints(Long userId) {
        List<PointEntity> points = repository.findAllByUserIdOrderByDate(userId);
        System.out.println("points: " + points);

        if (points.isEmpty()) {
            throw new NotFoundException("Nenhum ponto encontrado para o usuário");
        }

        return points;
    }

    @Transactional
    public void updateUserIdOnPoints() {
        List<PointEntity> points = repository.findAll();

        for (PointEntity point : points) {
            Optional<IconicoEntity> iconico = iconicoRepository.findByUid(point.getUid());
            if (iconico.isEmpty()) {
                throw new NotFoundException("Tag não encontrada em Iconico cadastrado");
            }

            if (Objects.equals(point.getUserId(), iconico.get().getId())) {
                continue;
            }

            point.setUserId(iconico.get().getId());
            repository.save(point);

            System.out.println("Point updated: " + point.getUserId() + " - " + point.getUsername());
        }

    }
}
