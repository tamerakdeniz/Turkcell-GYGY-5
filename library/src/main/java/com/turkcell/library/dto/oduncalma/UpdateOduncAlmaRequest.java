package com.turkcell.library.dto.oduncalma;

import java.time.LocalDate;

public class UpdateOduncAlmaRequest {
    private Long ogrenciId;
    private Long kopyaId;
    private Long gorevliId;
    private LocalDate iadeTarihiBeklenen;
    private String durum;

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

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
