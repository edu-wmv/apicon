package com.br.ufba.icon.api.jobs;

import com.br.ufba.icon.api.service.IconicoService;
import com.br.ufba.icon.api.service.PointService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.NotActiveException;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class CloseAllPoints {

    private final PointService pointService;
    IconicoService iconicoService;

    public CloseAllPoints(IconicoService iconicoService, PointService pointService) {
        this.iconicoService = iconicoService;
        this.pointService = pointService;
    }

    @Scheduled(cron = "0 0 22 1/1 * ?")
    public void everyNight() {
        System.out.println(MessageFormat
                .format("Periodic Task: {0}; Thread: {1};", new Date().toString(), Thread.currentThread().getName()));

        // close all iconicos points
        pointService.updateUserIdOnPoints();
        iconicoService.closeAllIconicos();
    }

    @Scheduled(cron = "0 30 22 1/1 * ?")
    public void everyNight2() throws NotActiveException {
        System.out.println(MessageFormat
                .format("Periodic Task: {0}; Thread: {1};", new Date().toString(), Thread.currentThread().getName()));

        // recalculate all hours
        iconicoService.recalculateAllHours();
    }
}
