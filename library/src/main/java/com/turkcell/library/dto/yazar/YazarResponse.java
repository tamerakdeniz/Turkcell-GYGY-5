package com.turkcell.library.dto.yazar;

import java.time.LocalDate;

public class YazarResponse {
    private Long yazarId;
    private String ad;
    private String soyad;
    private LocalDate dogumTarihi;
    private LocalDate olumTarihi;
    private String uyruk;
    private String biyografi;

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
}
