package com.turkcell.library.dto.kitapkopya;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateKitapKopyaRequest {
    private Long kitapId;
    private String barkod;
    private String rafNo;
    private String durum;
    private LocalDate alimTarihi;
    private BigDecimal fiyat;

    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getRafNo() {
        return rafNo;
    }

    public void setRafNo(String rafNo) {
        this.rafNo = rafNo;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public LocalDate getAlimTarihi() {
        return alimTarihi;
    }

    public void setAlimTarihi(LocalDate alimTarihi) {
        this.alimTarihi = alimTarihi;
    }

    public BigDecimal getFiyat() {
        return fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }
}
