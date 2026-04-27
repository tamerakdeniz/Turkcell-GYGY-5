package com.turkcell.library.dto.rezervasyon;

import java.time.LocalDate;

public class CreateRezervasyonRequest {
    private Long ogrenciId;
    private Long kitapId;
    private LocalDate sonGecerlilikTarihi;

    public Long getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(Long ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public LocalDate getSonGecerlilikTarihi() {
        return sonGecerlilikTarihi;
    }

    public void setSonGecerlilikTarihi(LocalDate sonGecerlilikTarihi) {
        this.sonGecerlilikTarihi = sonGecerlilikTarihi;
    }
}
