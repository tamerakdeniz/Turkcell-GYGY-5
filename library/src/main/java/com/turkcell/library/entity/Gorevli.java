package com.turkcell.library.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "gorevli")
public class Gorevli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gorevli_id")
    private Long gorevliId;

    @Column(name = "tc_kimlik", nullable = false, unique = true, length = 11)
    private String tcKimlik;

    @Column(name = "kullanici_adi", nullable = false, unique = true, length = 50)
    private String kullaniciAdi;

    @Column(name = "ad", nullable = false, length = 50)
    private String ad;

    @Column(name = "soyad", nullable = false, length = 50)
    private String soyad;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "telefon", nullable = false, length = 20)
    private String telefon;

    @Column(name = "pozisyon", nullable = false, length = 15)
    private String pozisyon;

    @Column(name = "ise_baslama_tarihi", nullable = false)
    private LocalDate iseBaslamaTarihi;

    @Column(name = "maas", precision = 10, scale = 2)
    private BigDecimal maas;

    @Column(name = "sifre_hash", nullable = false, length = 255)
    private String sifreHash;

    @Column(name = "aktif_mi")
    private Boolean aktifMi = Boolean.TRUE;

    @ManyToMany
    @JoinTable(
        name = "gorevli_yetki",
        joinColumns = @JoinColumn(name = "gorevli_id"),
        inverseJoinColumns = @JoinColumn(name = "yetki_id")
    )
    private Set<Yetki> yetkiler;

    public Long getGorevliId() {
        return gorevliId;
    }

    public void setGorevliId(Long gorevliId) {
        this.gorevliId = gorevliId;
    }

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

    public Set<Yetki> getYetkiler() {
        return yetkiler;
    }

    public void setYetkiler(Set<Yetki> yetkiler) {
        this.yetkiler = yetkiler;
    }
}
