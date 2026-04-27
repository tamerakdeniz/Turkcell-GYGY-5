package com.turkcell.library.dto.yetki;

public class UpdateYetkiRequest {
    private String yetkiAd;
    private String aciklama;
    private String modul;

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
}
