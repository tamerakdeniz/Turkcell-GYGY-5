package com.turkcell.library.entity;

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
@Table(name = "iade")
public class Iade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iade_id")
    private Long iadeId;

    @OneToOne
    @JoinColumn(name = "odunc_id", nullable = false, unique = true)
    private OduncAlma oduncAlma;

    @ManyToOne
    @JoinColumn(name = "gorevli_id", nullable = false)
    private Gorevli gorevli;

    @Column(name = "iade_tarihi")
    private LocalDateTime iadeTarihi;

    @Column(name = "kitap_durumu", nullable = false, length = 15)
    private String kitapDurumu;

    @Column(name = "notlar", columnDefinition = "TEXT")
    private String notlar;

    public Long getIadeId() {
        return iadeId;
    }

    public void setIadeId(Long iadeId) {
        this.iadeId = iadeId;
    }

    public OduncAlma getOduncAlma() {
        return oduncAlma;
    }

    public void setOduncAlma(OduncAlma oduncAlma) {
        this.oduncAlma = oduncAlma;
    }

    public Gorevli getGorevli() {
        return gorevli;
    }

    public void setGorevli(Gorevli gorevli) {
        this.gorevli = gorevli;
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
