package com.turkcell.library.dto.iade;

import java.time.LocalDateTime;

public class IadeResponse {
    private Long iadeId;
    private Long oduncId;
    private Long gorevliId;
    private String gorevliAdSoyad;
    private LocalDateTime iadeTarihi;
    private String kitapDurumu;
    private String notlar;

    public Long getIadeId() {
        return iadeId;
    }

    public void setIadeId(Long iadeId) {
        this.iadeId = iadeId;
    }

    public Long getOduncId() {
        return oduncId;
    }

    public void setOduncId(Long oduncId) {
        this.oduncId = oduncId;
    }

    public Long getGorevliId() {
        return gorevliId;
    }

    public void setGorevliId(Long gorevliId) {
        this.gorevliId = gorevliId;
    }

    public String getGorevliAdSoyad() {
        return gorevliAdSoyad;
    }

    public void setGorevliAdSoyad(String gorevliAdSoyad) {
        this.gorevliAdSoyad = gorevliAdSoyad;
    }

    public LocalDateTime getIadeTarihi() {
        return iadeTarihi;
    }

    public void setIadeTarihi(LocalDateTime iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    public String getKitapDurumu() {
        return kitapDurumu;
    }

    public void setKitapDurumu(String kitapDurumu) {
        this.kitapDurumu = kitapDurumu;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }
}
