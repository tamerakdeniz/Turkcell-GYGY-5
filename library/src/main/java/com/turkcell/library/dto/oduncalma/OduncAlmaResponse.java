package com.turkcell.library.dto.oduncalma;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OduncAlmaResponse {
    private Long oduncId;
    private Long ogrenciId;
    private String ogrenciAdSoyad;
    private Long kopyaId;
    private String barkod;
    private String kitapBaslik;
    private Long gorevliId;
    private String gorevliAdSoyad;
    private LocalDateTime oduncTarihi;
    private LocalDate iadeTarihiBeklenen;
    private String durum;

    public Long getOduncId() {
        return oduncId;
    }

    public void setOduncId(Long oduncId) {
        this.oduncId = oduncId;
    }

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public String getOgrenciAdSoyad() {
        return ogrenciAdSoyad;
    }

    public void setOgrenciAdSoyad(String ogrenciAdSoyad) {
        this.ogrenciAdSoyad = ogrenciAdSoyad;
    }

    public Long getKopyaId() {
        return kopyaId;
    }

    public void setKopyaId(Long kopyaId) {
        this.kopyaId = kopyaId;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getKitapBaslik() {
        return kitapBaslik;
    }

    public void setKitapBaslik(String kitapBaslik) {
        this.kitapBaslik = kitapBaslik;
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

    public LocalDateTime getOduncTarihi() {
        return oduncTarihi;
    }

    public void setOduncTarihi(LocalDateTime oduncTarihi) {
        this.oduncTarihi = oduncTarihi;
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
