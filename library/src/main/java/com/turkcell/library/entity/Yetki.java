package com.turkcell.library.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "yetki")
public class Yetki {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yetki_id")
    private Long yetkiId;

    @Column(name = "yetki_ad", nullable = false, unique = true, length = 50)
    private String yetkiAd;

    @Column(name = "aciklama", length = 200)
    private String aciklama;

    @Column(name = "modul", nullable = false, length = 30)
    private String modul;

    @ManyToMany(mappedBy = "yetkiler")
    private Set<Gorevli> gorevliler;

    public Long getYetkiId() {
        return yetkiId;
    }

    public void setYetkiId(Long yetkiId) {
        this.yetkiId = yetkiId;
    }

    public String getYetkiAd() {
        return yetkiAd;
    }

    public void setYetkiAd(String yetkiAd) {
        this.yetkiAd = yetkiAd;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getModul() {
        return modul;
    }

    public void setModul(String modul) {
        this.modul = modul;
    }

    public Set<Gorevli> getGorevliler() {
        return gorevliler;
    }

    public void setGorevliler(Set<Gorevli> gorevliler) {
        this.gorevliler = gorevliler;
    }
}
