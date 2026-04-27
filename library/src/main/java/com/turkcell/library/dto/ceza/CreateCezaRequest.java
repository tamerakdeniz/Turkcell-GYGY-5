package com.turkcell.library.dto.ceza;

import java.math.BigDecimal;

public class CreateCezaRequest {
    private Long ogrenciId;
    private Long oduncId;
    private BigDecimal cezaMiktari;
    private String cezaNedeni;

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
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
}
