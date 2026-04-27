package com.turkcell.library.dto.kategori;

public class CreateKategoriRequest {
    private Long ustKategoriId;
    private String ad;
    private String aciklama;

    public Long getUstKategoriId() {
        return ustKategoriId;
    }

    public void setUstKategoriId(Long ustKategoriId) {
        this.ustKategoriId = ustKategoriId;
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
