package com.turkcell.library.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "yazar")
public class Yazar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yazar_id")
    private Long yazarId;

    @Column(name = "ad", nullable = false, length = 100)
    private String ad;

    @Column(name = "soyad", nullable = false, length = 100)
    private String soyad;

    @Column(name = "dogum_tarihi")
    private LocalDate dogumTarihi;

    @Column(name = "olum_tarihi")
    private LocalDate olumTarihi;

    @Column(name = "uyruk", length = 50)
    private String uyruk;

    @Column(name = "biyografi", columnDefinition = "TEXT")
    private String biyografi;

    @ManyToMany(mappedBy = "yazarlar")
    private Set<Kitap> kitaplar;

    public Long getYazarId() {
        return yazarId;
    }

    public void setYazarId(Long yazarId) {
        this.yazarId = yazarId;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public LocalDate getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(LocalDate dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public LocalDate getOlumTarihi() {
        return olumTarihi;
    }

    public void setOlumTarihi(LocalDate olumTarihi) {
        this.olumTarihi = olumTarihi;
    }

    public String getUyruk() {
        return uyruk;
    }

    public void setUyruk(String uyruk) {
        this.uyruk = uyruk;
    }

    public String getBiyografi() {
        return biyografi;
    }

    public void setBiyografi(String biyografi) {
        this.biyografi = biyografi;
    }

    public Set<Kitap> getKitaplar() {
        return kitaplar;
    }

    public void setKitaplar(Set<Kitap> kitaplar) {
        this.kitaplar = kitaplar;
    }
}
