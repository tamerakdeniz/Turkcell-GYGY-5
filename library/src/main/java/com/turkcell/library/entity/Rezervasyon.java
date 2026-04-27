package com.turkcell.library.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rezervasyon")
public class Rezervasyon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rezervasyon_id")
    private Long rezervasyonId;

    @ManyToOne
    @JoinColumn(name = "ogrenci_id", nullable = false)
    private Ogrenci ogrenci;

    @ManyToOne
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;

    @Column(name = "rezervasyon_tarihi")
    private LocalDateTime rezervasyonTarihi;

    @Column(name = "son_gecerlilik_tarihi", nullable = false)
    private LocalDate sonGecerlilikTarihi;

    @Column(name = "durum", nullable = false, length = 15)
    private String durum = "bekliyor";

    public Long getRezervasyonId() {
        return rezervasyonId;
    }

    public void setRezervasyonId(Long rezervasyonId) {
        this.rezervasyonId = rezervasyonId;
    }

    public Ogrenci getOgrenci() {
        return ogrenci;
    }

    public void setOgrenci(Ogrenci ogrenci) {
        this.ogrenci = ogrenci;
    }

    public Kitap getKitap() {
        return kitap;
    }

    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
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
