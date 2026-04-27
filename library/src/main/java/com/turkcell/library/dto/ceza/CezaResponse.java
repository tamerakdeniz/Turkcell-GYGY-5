package com.turkcell.library.dto.ceza;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CezaResponse {
    private Long cezaId;
    private Long ogrenciId;
    private String ogrenciAdSoyad;
    private Long oduncId;
    private BigDecimal cezaMiktari;
    private String cezaNedeni;
    private LocalDate cezaTarihi;
    private Boolean odendiMi;
    private LocalDate odemeTarihi;

    public Long getCezaId() {
        return cezaId;
    }

    public void setCezaId(Long cezaId) {
        this.cezaId = cezaId;
    }

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public String getOgrenciAdSoyad() {
        return ogrenciAdSoyad;
    }

    public void setOgrenciAdSoyad(String ogrenciAdSoyad) {
        this.ogrenciAdSoyad = ogrenciAdSoyad;
    }

    public Long getOduncId() {
        return oduncId;
    }

    public void setOduncId(Long oduncId) {
        this.oduncId = oduncId;
    }

    public BigDecimal getCezaMiktari() {
        return cezaMiktari;
    }

    public void setCezaMiktari(BigDecimal cezaMiktari) {
        this.cezaMiktari = cezaMiktari;
    }

    public String getCezaNedeni() {
        return cezaNedeni;
    }

    public void setCezaNedeni(String cezaNedeni) {
        this.cezaNedeni = cezaNedeni;
    }

    public LocalDate getCezaTarihi() {
        return cezaTarihi;
    }

    public void setCezaTarihi(LocalDate cezaTarihi) {
        this.cezaTarihi = cezaTarihi;
    }

    public Boolean getOdendiMi() {
        return odendiMi;
    }

    public void setOdendiMi(Boolean odendiMi) {
        this.odendiMi = odendiMi;
    }

    public LocalDate getOdemeTarihi() {
        return odemeTarihi;
    }

    public void setOdemeTarihi(LocalDate odemeTarihi) {
        this.odemeTarihi = odemeTarihi;
    }
}
