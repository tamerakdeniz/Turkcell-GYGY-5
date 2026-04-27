package com.turkcell.library.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kategori_id")
    private Long kategoriId;

    @ManyToOne
    @JoinColumn(name = "ust_kategori_id")
    private Kategori ustKategori;

    @OneToMany(mappedBy = "ustKategori")
    private List<Kategori> altKategoriler;

    @Column(name = "ad", length = 100)
    private String ad;

    @Column(name = "aciklama", columnDefinition = "TEXT")
    private String aciklama;

    @ManyToMany(mappedBy = "ekKategoriler")
    private Set<Kitap> kitaplar;

    public Long getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public Kategori getUstKategori() {
        return ustKategori;
    }

    public void setUstKategori(Kategori ustKategori) {
        this.ustKategori = ustKategori;
    }

    public List<Kategori> getAltKategoriler() {
        return altKategoriler;
    }

    public void setAltKategoriler(List<Kategori> altKategoriler) {
        this.altKategoriler = altKategoriler;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Set<Kitap> getKitaplar() {
        return kitaplar;
    }

    public void setKitaplar(Set<Kitap> kitaplar) {
        this.kitaplar = kitaplar;
    }
}
