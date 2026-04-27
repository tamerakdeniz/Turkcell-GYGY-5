package com.turkcell.library.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ogrenci")
public class Ogrenci {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ogrenci_id")
    private Long ogrenciId;

    @Column(name = "ogrenci_no", nullable = false, unique = true, length = 15)
    private String ogrenciNo;

    @Column(name = "tc_kimlik", nullable = false, unique = true, length = 11)
    private String tcKimlik;

    @Column(name = "ad", nullable = false, length = 50)
    private String ad;

    @Column(name = "soyad", nullable = false, length = 50)
    private String soyad;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "telefon", nullable = false, length = 20)
    private String telefon;

    @Column(name = "adres", length = 300)
    private String adres;

    @Column(name = "dogum_tarihi", nullable = false)
    private LocalDate dogumTarihi;

    @Column(name = "cinsiyet", length = 1)
    private String cinsiyet;

    @Column(name = "kayit_tarihi")
    private LocalDate kayitTarihi;

    @Column(name = "aktif_mi")
    private Boolean aktifMi = Boolean.TRUE;

    @OneToMany(mappedBy = "ogrenci")
    private List<OduncAlma> oduncler;

    @OneToMany(mappedBy = "ogrenci")
    private List<Ceza> cezalar;

    @OneToMany(mappedBy = "ogrenci")
    private List<Rezervasyon> rezervasyonlar;

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public String getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(String ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public String getTcKimlik() {
        return tcKimlik;
    }

    public void setTcKimlik(String tcKimlik) {
        this.tcKimlik = tcKimlik;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public LocalDate getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(LocalDate dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public LocalDate getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDate kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public Boolean getAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(Boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public List<OduncAlma> getOduncler() {
        return oduncler;
    }

    public void setOduncler(List<OduncAlma> oduncler) {
        this.oduncler = oduncler;
    }

    public List<Ceza> getCezalar() {
        return cezalar;
    }

    public void setCezalar(List<Ceza> cezalar) {
        this.cezalar = cezalar;
    }

    public List<Rezervasyon> getRezervasyonlar() {
        return rezervasyonlar;
    }

    public void setRezervasyonlar(List<Rezervasyon> rezervasyonlar) {
        this.rezervasyonlar = rezervasyonlar;
    }
}
