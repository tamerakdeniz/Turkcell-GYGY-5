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
@Table(name = "ceza")
public class Ceza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ceza_id")
    private Long cezaId;

    @ManyToOne
    @JoinColumn(name = "ogrenci_id", nullable = false)
    private Ogrenci ogrenci;

    @ManyToOne
    @JoinColumn(name = "odunc_id", nullable = false)
    private OduncAlma oduncAlma;

    @Column(name = "ceza_miktari", nullable = false, precision = 8, scale = 2)
    private BigDecimal cezaMiktari;

    @Column(name = "ceza_nedeni", nullable = false, length = 15)
    private String cezaNedeni;

    @Column(name = "ceza_tarihi")
    private LocalDate cezaTarihi;

    @Column(name = "odendi_mi")
    private Boolean odendiMi = Boolean.FALSE;

    @Column(name = "odeme_tarihi")
    private LocalDate odemeTarihi;

    public Long getCezaId() {
        return cezaId;
    }

    public void setCezaId(Long cezaId) {
        this.cezaId = cezaId;
    }

    public Ogrenci getOgrenci() {
        return ogrenci;
    }

    public void setOgrenci(Ogrenci ogrenci) {
        this.ogrenci = ogrenci;
    }

    public OduncAlma getOduncAlma() {
        return oduncAlma;
    }

    public void setOduncAlma(OduncAlma oduncAlma) {
        this.oduncAlma = oduncAlma;
    }

    public BigDecimal getCezaMiktari() {
        return cezaMiktari;
    }

    public void setCezaMiktari(BigDecimal cezaMiktari) {
        this.cezaMiktari = cezaMiktari;
    }

    public String getCezaNedeni() {
        return cezaNedeni;
    }

    public void setCezaNedeni(String cezaNedeni) {
        this.cezaNedeni = cezaNedeni;
    }

    public LocalDate getCezaTarihi() {
        return cezaTarihi;
    }

    public void setCezaTarihi(LocalDate cezaTarihi) {
        this.cezaTarihi = cezaTarihi;
    }

    public Boolean getOdendiMi() {
        return odendiMi;
    }

    public void setOdendiMi(Boolean odendiMi) {
        this.odendiMi = odendiMi;
    }

    public LocalDate getOdemeTarihi() {
        return odemeTarihi;
    }

    public void setOdemeTarihi(LocalDate odemeTarihi) {
        this.odemeTarihi = odemeTarihi;
    }
}
