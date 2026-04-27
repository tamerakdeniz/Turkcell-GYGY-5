package com.turkcell.library.dto.kitap;

import java.util.Set;

public class KitapResponse {
    private Long kitapId;
    private String isbn;
    private Long kategoriId;
    private String kategoriAd;
    private Long yayineviId;
    private String yayineviAd;
    private String baslik;
    private Integer yayinYili;
    private Integer sayfaSayisi;
    private String dil;
    private Integer baskiNo;
    private String aciklama;
    private Set<String> yazarlar;

    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getKategoriAd() {
        return kategoriAd;
    }

    public void setKategoriAd(String kategoriAd) {
        this.kategoriAd = kategoriAd;
    }

    public Long getYayineviId() {
        return yayineviId;
    }

    public void setYayineviId(Long yayineviId) {
        this.yayineviId = yayineviId;
    }

    public String getYayineviAd() {
        return yayineviAd;
    }

    public void setYayineviAd(String yayineviAd) {
        this.yayineviAd = yayineviAd;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public Integer getYayinYili() {
        return yayinYili;
    }

    public void setYayinYili(Integer yayinYili) {
        this.yayinYili = yayinYili;
    }

    public Integer getSayfaSayisi() {
        return sayfaSayisi;
    }

    public void setSayfaSayisi(Integer sayfaSayisi) {
        this.sayfaSayisi = sayfaSayisi;
    }

    public String getDil() {
        return dil;
    }

    public void setDil(String dil) {
        this.dil = dil;
    }

    public Integer getBaskiNo() {
        return baskiNo;
    }

    public void setBaskiNo(Integer baskiNo) {
        this.baskiNo = baskiNo;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Set<String> getYazarlar() {
        return yazarlar;
    }

    public void setYazarlar(Set<String> yazarlar) {
        this.yazarlar = yazarlar;
    }
}
