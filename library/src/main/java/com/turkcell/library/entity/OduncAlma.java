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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "odunc_alma")
public class OduncAlma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "odunc_id")
    private Long oduncId;

    @ManyToOne
    @JoinColumn(name = "ogrenci_id", nullable = false)
    private Ogrenci ogrenci;

    @ManyToOne
    @JoinColumn(name = "kopya_id", nullable = false)
    private KitapKopya kopya;

    @ManyToOne
    @JoinColumn(name = "gorevli_id", nullable = false)
    private Gorevli gorevli;

    @Column(name = "odunc_tarihi")
    private LocalDateTime oduncTarihi;

    @Column(name = "iade_tarihi_beklenen", nullable = false)
    private LocalDate iadeTarihiBeklenen;

    @Column(name = "durum", nullable = false, length = 15)
    private String durum = "aktif";

    @OneToOne(mappedBy = "oduncAlma")
    private Iade iade;

    public Long getOduncId() {
        return oduncId;
    }

    public void setOduncId(Long oduncId) {
        this.oduncId = oduncId;
    }

    public Ogrenci getOgrenci() {
        return ogrenci;
    }

    public void setOgrenci(Ogrenci ogrenci) {
        this.ogrenci = ogrenci;
    }

    public KitapKopya getKopya() {
        return kopya;
    }

    public void setKopya(KitapKopya kopya) {
        this.kopya = kopya;
    }

    public Gorevli getGorevli() {
        return gorevli;
    }

    public void setGorevli(Gorevli gorevli) {
        this.gorevli = gorevli;
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

    public Iade getIade() {
        return iade;
    }

    public void setIade(Iade iade) {
        this.iade = iade;
    }
}
