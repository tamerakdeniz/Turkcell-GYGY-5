package com.turkcell.library.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.kitap.CreateKitapRequest;
import com.turkcell.library.dto.kitap.KitapResponse;
import com.turkcell.library.dto.kitap.UpdateKitapRequest;
import com.turkcell.library.entity.Kategori;
import com.turkcell.library.entity.Kitap;
import com.turkcell.library.entity.Yayinevi;
import com.turkcell.library.entity.Yazar;
import com.turkcell.library.repository.KategoriRepository;
import com.turkcell.library.repository.KitapRepository;
import com.turkcell.library.repository.YayineviRepository;
import com.turkcell.library.repository.YazarRepository;

@Service
public class KitapServiceImpl {

    private final KitapRepository kitapRepository;
    private final KategoriRepository kategoriRepository;
    private final YayineviRepository yayineviRepository;
    private final YazarRepository yazarRepository;

    public KitapServiceImpl(KitapRepository kitapRepository,
                            KategoriRepository kategoriRepository,
                            YayineviRepository yayineviRepository,
                            YazarRepository yazarRepository) {
        this.kitapRepository = kitapRepository;
        this.kategoriRepository = kategoriRepository;
        this.yayineviRepository = yayineviRepository;
        this.yazarRepository = yazarRepository;
    }

    public KitapResponse create(CreateKitapRequest request) {
        Kategori kategori = kategoriRepository.findById(request.getKategoriId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + request.getKategoriId()));
        Yayinevi yayinevi = yayineviRepository.findById(request.getYayineviId())
                .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + request.getYayineviId()));

        Kitap kitap = new Kitap();
        kitap.setIsbn(request.getIsbn());
        kitap.setAnaKategori(kategori);
        kitap.setYayinevi(yayinevi);
        kitap.setBaslik(request.getBaslik());
        kitap.setYayinYili(request.getYayinYili());
        kitap.setSayfaSayisi(request.getSayfaSayisi());
        kitap.setDil(request.getDil() != null ? request.getDil() : "Türkçe");
        kitap.setBaskiNo(request.getBaskiNo() != null ? request.getBaskiNo() : 1);
        kitap.setAciklama(request.getAciklama());
        kitap.setEkKategoriler(loadKategoriler(request.getEkKategoriIdleri()));
        kitap.setYazarlar(loadYazarlar(request.getYazarIdleri()));

        return toResponse(kitapRepository.save(kitap));
    }

    public List<KitapResponse> getAll() {
        return kitapRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public KitapResponse getById(Long id) {
        Kitap kitap = kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + id));
        return toResponse(kitap);
    }

    public KitapResponse update(Long id, UpdateKitapRequest request) {
        Kitap kitap = kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + id));
        Kategori kategori = kategoriRepository.findById(request.getKategoriId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + request.getKategoriId()));
        Yayinevi yayinevi = yayineviRepository.findById(request.getYayineviId())
                .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + request.getYayineviId()));

        kitap.setIsbn(request.getIsbn());
        kitap.setAnaKategori(kategori);
        kitap.setYayinevi(yayinevi);
        kitap.setBaslik(request.getBaslik());
        kitap.setYayinYili(request.getYayinYili());
        kitap.setSayfaSayisi(request.getSayfaSayisi());
        kitap.setDil(request.getDil());
        kitap.setBaskiNo(request.getBaskiNo());
        kitap.setAciklama(request.getAciklama());
        kitap.setEkKategoriler(loadKategoriler(request.getEkKategoriIdleri()));
        kitap.setYazarlar(loadYazarlar(request.getYazarIdleri()));

        return toResponse(kitapRepository.save(kitap));
    }

    public void delete(Long id) {
        Kitap kitap = kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + id));
        kitapRepository.delete(kitap);
    }

    private Set<Kategori> loadKategoriler(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(kategoriRepository.findAllById(ids));
    }

    private Set<Yazar> loadYazarlar(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(yazarRepository.findAllById(ids));
    }

    private KitapResponse toResponse(Kitap kitap) {
        KitapResponse response = new KitapResponse();
        response.setKitapId(kitap.getKitapId());
        response.setIsbn(kitap.getIsbn());
        response.setBaslik(kitap.getBaslik());
        response.setYayinYili(kitap.getYayinYili());
        response.setSayfaSayisi(kitap.getSayfaSayisi());
        response.setDil(kitap.getDil());
        response.setBaskiNo(kitap.getBaskiNo());
        response.setAciklama(kitap.getAciklama());
        if (kitap.getAnaKategori() != null) {
            response.setKategoriId(kitap.getAnaKategori().getKategoriId());
            response.setKategoriAd(kitap.getAnaKategori().getAd());
        }
        if (kitap.getYayinevi() != null) {
            response.setYayineviId(kitap.getYayinevi().getYayineviId());
            response.setYayineviAd(kitap.getYayinevi().getAd());
        }
        if (kitap.getYazarlar() != null) {
            response.setYazarlar(kitap.getYazarlar().stream()
                    .map(y -> y.getAd() + " " + y.getSoyad())
                    .collect(Collectors.toSet()));
        }
        return response;
    }
}
