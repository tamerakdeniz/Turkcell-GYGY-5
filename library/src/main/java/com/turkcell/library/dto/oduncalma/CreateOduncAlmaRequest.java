package com.turkcell.library.dto.oduncalma;

import java.time.LocalDate;

public class CreateOduncAlmaRequest {
    private Long ogrenciId;
    private Long kopyaId;
    private Long gorevliId;
    private LocalDate iadeTarihiBeklenen;

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public Long getKopyaId() {
        return kopyaId;
    }

    public void setKopyaId(Long kopyaId) {
        this.kopyaId = kopyaId;
    }

    public Long getGorevliId() {
        return gorevliId;
    }

    public void setGorevliId(Long gorevliId) {
        this.gorevliId = gorevliId;
    }

    public LocalDate getIadeTarihiBeklenen() {
        return iadeTarihiBeklenen;
    }

    public void setIadeTarihiBeklenen(LocalDate iadeTarihiBeklenen) {
        this.iadeTarihiBeklenen = iadeTarihiBeklenen;
    }
}
