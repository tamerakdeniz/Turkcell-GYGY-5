package com.turkcell.library.dto.kategori;

public class KategoriResponse {
    private Long kategoriId;
    private Long ustKategoriId;
    private String ustKategoriAd;
    private String ad;
    private String aciklama;

    public Long getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public Long getUstKategoriId() {
        return ustKategoriId;
    }

    public void setUstKategoriId(Long ustKategoriId) {
        this.ustKategoriId = ustKategoriId;
    }

    public String getUstKategoriAd() {
        return ustKategoriAd;
    }

    public void setUstKategoriAd(String ustKategoriAd) {
        this.ustKategoriAd = ustKategoriAd;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
