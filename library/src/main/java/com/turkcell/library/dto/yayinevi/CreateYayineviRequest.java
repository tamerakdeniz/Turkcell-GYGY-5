package com.turkcell.library.dto.yayinevi;

public class CreateYayineviRequest {
    private String ad;
    private String adres;
    private String telefon;
    private String email;
    private String webSitesi;

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
}
