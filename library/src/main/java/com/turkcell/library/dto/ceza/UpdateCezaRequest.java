package com.turkcell.library.dto.ceza;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateCezaRequest {
    private BigDecimal cezaMiktari;
    private String cezaNedeni;
    private Boolean odendiMi;
    private LocalDate odemeTarihi;

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
