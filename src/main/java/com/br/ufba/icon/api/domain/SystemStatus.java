package com.br.ufba.icon.api.domain;

import org.springframework.stereotype.Component;

@Component
public class SystemStatus {
    private boolean status;

    public SystemStatus() {
        this.status = true;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
