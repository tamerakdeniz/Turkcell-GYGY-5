package com.turkcell.library.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitap")
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitap_id")
    private Long kitapId;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori anaKategori;

    @ManyToOne
    @JoinColumn(name = "yayinevi_id", nullable = false)
    private Yayinevi yayinevi;

    @Column(name = "baslik", nullable = false, length = 255)
    private String baslik;

    @Column(name = "yayin_yili")
    private Integer yayinYili;

    @Column(name = "sayfa_sayisi")
    private Integer sayfaSayisi;

    @Column(name = "dil", length = 30)
    private String dil = "Türkçe";

    @Column(name = "baski_no")
    private Integer baskiNo = 1;

    @Column(name = "aciklama", nullable = false, columnDefinition = "TEXT")
    private String aciklama;

    @ManyToMany
    @JoinTable(
        name = "kitap_kategori",
        joinColumns = @JoinColumn(name = "kitap_id"),
        inverseJoinColumns = @JoinColumn(name = "kategori_id")
    )
    private Set<Kategori> ekKategoriler;

    @ManyToMany
    @JoinTable(
        name = "kitap_yazar",
        joinColumns = @JoinColumn(name = "kitap_id"),
        inverseJoinColumns = @JoinColumn(name = "yazar_id")
    )
    private Set<Yazar> yazarlar;

    @OneToMany(mappedBy = "kitap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitapKopya> kopyalar;

    @OneToMany(mappedBy = "kitap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rezervasyon> rezervasyonlar;

    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Kategori getAnaKategori() {
        return anaKategori;
    }

    public void setAnaKategori(Kategori anaKategori) {
        this.anaKategori = anaKategori;
    }

    public Yayinevi getYayinevi() {
        return yayinevi;
    }

    public void setYayinevi(Yayinevi yayinevi) {
        this.yayinevi = yayinevi;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public Integer getYayinYili() {
        return yayinYili;
    }

    public void setYayinYili(Integer yayinYili) {
        this.yayinYili = yayinYili;
    }

    public Integer getSayfaSayisi() {
        return sayfaSayisi;
    }

    public void setSayfaSayisi(Integer sayfaSayisi) {
        this.sayfaSayisi = sayfaSayisi;
    }

    public String getDil() {
        return dil;
    }

    public void setDil(String dil) {
        this.dil = dil;
    }

    public Integer getBaskiNo() {
        return baskiNo;
    }

    public void setBaskiNo(Integer baskiNo) {
        this.baskiNo = baskiNo;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Set<Kategori> getEkKategoriler() {
        return ekKategoriler;
    }

    public void setEkKategoriler(Set<Kategori> ekKategoriler) {
        this.ekKategoriler = ekKategoriler;
    }

    public Set<Yazar> getYazarlar() {
        return yazarlar;
    }

    public void setYazarlar(Set<Yazar> yazarlar) {
        this.yazarlar = yazarlar;
    }

    public List<KitapKopya> getKopyalar() {
        return kopyalar;
    }

    public void setKopyalar(List<KitapKopya> kopyalar) {
        this.kopyalar = kopyalar;
    }

    public List<Rezervasyon> getRezervasyonlar() {
        return rezervasyonlar;
    }

    public void setRezervasyonlar(List<Rezervasyon> rezervasyonlar) {
        this.rezervasyonlar = rezervasyonlar;
    }
}
