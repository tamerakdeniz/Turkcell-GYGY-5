package com.turkcell.library.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "yayinevi")
public class Yayinevi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yayinevi_id")
    private Long yayineviId;

    @Column(name = "ad", nullable = false, length = 150)
    private String ad;

    @Column(name = "adres", length = 300)
    private String adres;

    @Column(name = "telefon", length = 20)
    private String telefon;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "web_sitesi", length = 200)
    private String webSitesi;

    @OneToMany(mappedBy = "yayinevi")
    private List<Kitap> kitaplar;

    public Long getYayineviId() {
        return yayineviId;
    }

    public void setYayineviId(Long yayineviId) {
        this.yayineviId = yayineviId;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSitesi() {
        return webSitesi;
    }

    public void setWebSitesi(String webSitesi) {
        this.webSitesi = webSitesi;
    }

    public List<Kitap> getKitaplar() {
        return kitaplar;
    }

    public void setKitaplar(List<Kitap> kitaplar) {
        this.kitaplar = kitaplar;
    }
}
