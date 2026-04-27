package com.turkcell.library.dto.rezervasyon;

import java.time.LocalDate;

public class UpdateRezervasyonRequest {
    private LocalDate sonGecerlilikTarihi;
    private String durum;

    public LocalDate getSonGecerlilikTarihi() {
        return sonGecerlilikTarihi;
    }

    public void setSonGecerlilikTarihi(LocalDate sonGecerlilikTarihi) {
        this.sonGecerlilikTarihi = sonGecerlilikTarihi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
