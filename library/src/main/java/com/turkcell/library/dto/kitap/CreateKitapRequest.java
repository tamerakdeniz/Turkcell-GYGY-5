package com.turkcell.library.dto.kitap;

import java.util.Set;

public class CreateKitapRequest {
    private String isbn;
    private Long kategoriId;
    private Long yayineviId;
    private String baslik;
    private Integer yayinYili;
    private Integer sayfaSayisi;
    private String dil;
    private Integer baskiNo;
    private String aciklama;
    private Set<Long> ekKategoriIdleri;
    private Set<Long> yazarIdleri;

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

    public Long getYayineviId() {
        return yayineviId;
    }

    public void setYayineviId(Long yayineviId) {
        this.yayineviId = yayineviId;
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

    public Set<Long> getEkKategoriIdleri() {
        return ekKategoriIdleri;
    }

    public void setEkKategoriIdleri(Set<Long> ekKategoriIdleri) {
        this.ekKategoriIdleri = ekKategoriIdleri;
    }

    public Set<Long> getYazarIdleri() {
        return yazarIdleri;
    }

    public void setYazarIdleri(Set<Long> yazarIdleri) {
        this.yazarIdleri = yazarIdleri;
    }
}
