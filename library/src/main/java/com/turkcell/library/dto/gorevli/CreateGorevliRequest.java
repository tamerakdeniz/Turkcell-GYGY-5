package com.turkcell.library.dto.gorevli;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateGorevliRequest {
    private String tcKimlik;
    private String kullaniciAdi;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String pozisyon;
    private LocalDate iseBaslamaTarihi;
    private BigDecimal maas;
    private String sifreHash;
    private Boolean aktifMi;

    public String getTcKimlik() {
        return tcKimlik;
    }

    public void setTcKimlik(String tcKimlik) {
        this.tcKimlik = tcKimlik;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
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

    public String getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
    }

    public LocalDate getIseBaslamaTarihi() {
        return iseBaslamaTarihi;
    }

    public void setIseBaslamaTarihi(LocalDate iseBaslamaTarihi) {
        this.iseBaslamaTarihi = iseBaslamaTarihi;
    }

    public BigDecimal getMaas() {
        return maas;
    }

    public void setMaas(BigDecimal maas) {
        this.maas = maas;
    }

    public String getSifreHash() {
        return sifreHash;
    }

    public void setSifreHash(String sifreHash) {
        this.sifreHash = sifreHash;
    }

    public Boolean getAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(Boolean aktifMi) {
        this.aktifMi = aktifMi;
    }
}
