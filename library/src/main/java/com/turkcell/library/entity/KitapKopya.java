package com.turkcell.library.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitap_kopya")
public class KitapKopya {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kopya_id")
    private Long kopyaId;

    @ManyToOne
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;

    @Column(name = "barkod", nullable = false, unique = true, length = 20)
    private String barkod;

    @Column(name = "raf_no", length = 20)
    private String rafNo;

    @Column(name = "durum", nullable = false, length = 15)
    private String durum = "musait";

    @Column(name = "alim_tarihi")
    private LocalDate alimTarihi;

    @Column(name = "fiyat", precision = 10, scale = 2)
    private BigDecimal fiyat;

    public Long getKopyaId() {
        return kopyaId;
    }

    public void setKopyaId(Long kopyaId) {
        this.kopyaId = kopyaId;
    }

    public Kitap getKitap() {
        return kitap;
    }

    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getRafNo() {
        return rafNo;
    }

    public void setRafNo(String rafNo) {
        this.rafNo = rafNo;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public LocalDate getAlimTarihi() {
        return alimTarihi;
    }

    public void setAlimTarihi(LocalDate alimTarihi) {
        this.alimTarihi = alimTarihi;
    }

    public BigDecimal getFiyat() {
        return fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }
}
