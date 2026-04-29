package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.kategori.CreateKategoriRequest;
import com.turkcell.library.dto.kategori.KategoriResponse;
import com.turkcell.library.dto.kategori.UpdateKategoriRequest;
import com.turkcell.library.entity.Kategori;
import com.turkcell.library.exception.EntityNotFoundException;
import com.turkcell.library.repository.KategoriRepository;

@Service
public class KategoriServiceImpl {

    private final KategoriRepository kategoriRepository;

    public KategoriServiceImpl(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    public KategoriResponse create(CreateKategoriRequest request) {
        Kategori kategori = new Kategori();
        kategori.setAd(request.getAd());
        kategori.setAciklama(request.getAciklama());
        if (request.getUstKategoriId() != null) {
            Kategori ust = kategoriRepository.findById(request.getUstKategoriId())
                    .orElseThrow(() -> new EntityNotFoundException("Üst kategori", request.getUstKategoriId()));
            kategori.setUstKategori(ust);
        }
        return toResponse(kategoriRepository.save(kategori));
    }

    public List<KategoriResponse> getAll() {
        return kategoriRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public KategoriResponse getById(Long id) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategori", id));
        return toResponse(kategori);
    }

    public KategoriResponse update(Long id, UpdateKategoriRequest request) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategori", id));
        kategori.setAd(request.getAd());
        kategori.setAciklama(request.getAciklama());
        if (request.getUstKategoriId() != null) {
            Kategori ust = kategoriRepository.findById(request.getUstKategoriId())
                    .orElseThrow(() -> new EntityNotFoundException("Üst kategori", request.getUstKategoriId()));
            kategori.setUstKategori(ust);
        } else {
            kategori.setUstKategori(null);
        }
        return toResponse(kategoriRepository.save(kategori));
    }

    public void delete(Long id) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategori", id));
        kategoriRepository.delete(kategori);
    }

    private KategoriResponse toResponse(Kategori kategori) {
        KategoriResponse response = new KategoriResponse();
        response.setKategoriId(kategori.getKategoriId());
        response.setAd(kategori.getAd());
        response.setAciklama(kategori.getAciklama());
        if (kategori.getUstKategori() != null) {
            response.setUstKategoriId(kategori.getUstKategori().getKategoriId());
            response.setUstKategoriAd(kategori.getUstKategori().getAd());
        }
        return response;
    }
}
