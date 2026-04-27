package com.turkcell.library.dto.iade;

public class CreateIadeRequest {
    private Long oduncId;
    private Long gorevliId;
    private String kitapDurumu;
    private String notlar;

    public Long getOduncId() {
        return oduncId;
    }

    public void setOduncId(Long oduncId) {
        this.oduncId = oduncId;
    }

    public Long getGorevliId() {
        return gorevliId;
    }

    public void setGorevliId(Long gorevliId) {
        this.gorevliId = gorevliId;
    }

    public String getKitapDurumu() {
        return kitapDurumu;
    }

    public void setKitapDurumu(String kitapDurumu) {
        this.kitapDurumu = kitapDurumu;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }
}
