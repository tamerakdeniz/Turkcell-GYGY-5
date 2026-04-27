package com.turkcell.library.dto.rezervasyon;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RezervasyonResponse {
    private Long rezervasyonId;
    private Long ogrenciId;
    private String ogrenciAdSoyad;
    private Long kitapId;
    private String kitapBaslik;
    private LocalDateTime rezervasyonTarihi;
    private LocalDate sonGecerlilikTarihi;
    private String durum;

    public Long getRezervasyonId() {
        return rezervasyonId;
    }

    public void setRezervasyonId(Long rezervasyonId) {
        this.rezervasyonId = rezervasyonId;
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

    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapBaslik() {
        return kitapBaslik;
    }

    public void setKitapBaslik(String kitapBaslik) {
        this.kitapBaslik = kitapBaslik;
    }

    public LocalDateTime getRezervasyonTarihi() {
        return rezervasyonTarihi;
    }

    public void setRezervasyonTarihi(LocalDateTime rezervasyonTarihi) {
        this.rezervasyonTarihi = rezervasyonTarihi;
    }

    public LocalDate getSonGecerlilikTarihi() {
        return sonGecerlilikTarihi;
    }

    public void setSonGecerlilikTarihi(LocalDate sonGecerlilikTarihi) {
        this.sonGecerlilikTarihi = sonGecerlilikTarihi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
