package com.turkcell.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.rezervasyon.CreateRezervasyonRequest;
import com.turkcell.library.dto.rezervasyon.RezervasyonResponse;
import com.turkcell.library.dto.rezervasyon.UpdateRezervasyonRequest;
import com.turkcell.library.entity.Kitap;
import com.turkcell.library.entity.Ogrenci;
import com.turkcell.library.entity.Rezervasyon;
import com.turkcell.library.repository.KitapRepository;
import com.turkcell.library.repository.OgrenciRepository;
import com.turkcell.library.repository.RezervasyonRepository;

@Service
public class RezervasyonServiceImpl {

    private final RezervasyonRepository rezervasyonRepository;
    private final OgrenciRepository ogrenciRepository;
    private final KitapRepository kitapRepository;

    public RezervasyonServiceImpl(RezervasyonRepository rezervasyonRepository,
                                   OgrenciRepository ogrenciRepository,
                                   KitapRepository kitapRepository) {
        this.rezervasyonRepository = rezervasyonRepository;
        this.ogrenciRepository = ogrenciRepository;
        this.kitapRepository = kitapRepository;
    }

    public RezervasyonResponse create(CreateRezervasyonRequest request) {
        Ogrenci ogrenci = ogrenciRepository.findById(request.getOgrenciId())
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + request.getOgrenciId()));
        Kitap kitap = kitapRepository.findById(request.getKitapId())
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + request.getKitapId()));

        Rezervasyon rezervasyon = new Rezervasyon();
        rezervasyon.setOgrenci(ogrenci);
        rezervasyon.setKitap(kitap);
        rezervasyon.setRezervasyonTarihi(LocalDateTime.now());
        rezervasyon.setSonGecerlilikTarihi(request.getSonGecerlilikTarihi());
        rezervasyon.setDurum("bekliyor");
        return toResponse(rezervasyonRepository.save(rezervasyon));
    }

    public List<RezervasyonResponse> getAll() {
        return rezervasyonRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RezervasyonResponse getById(Long id) {
        Rezervasyon rezervasyon = rezervasyonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı: " + id));
        return toResponse(rezervasyon);
    }

    public RezervasyonResponse update(Long id, UpdateRezervasyonRequest request) {
        Rezervasyon rezervasyon = rezervasyonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı: " + id));
        rezervasyon.setSonGecerlilikTarihi(request.getSonGecerlilikTarihi());
        rezervasyon.setDurum(request.getDurum());
        return toResponse(rezervasyonRepository.save(rezervasyon));
    }

    public void delete(Long id) {
        Rezervasyon rezervasyon = rezervasyonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı: " + id));
        rezervasyonRepository.delete(rezervasyon);
    }

    private RezervasyonResponse toResponse(Rezervasyon rezervasyon) {
        RezervasyonResponse response = new RezervasyonResponse();
        response.setRezervasyonId(rezervasyon.getRezervasyonId());
        response.setRezervasyonTarihi(rezervasyon.getRezervasyonTarihi());
        response.setSonGecerlilikTarihi(rezervasyon.getSonGecerlilikTarihi());
        response.setDurum(rezervasyon.getDurum());
        if (rezervasyon.getOgrenci() != null) {
            response.setOgrenciId(rezervasyon.getOgrenci().getOgrenciId());
            response.setOgrenciAdSoyad(rezervasyon.getOgrenci().getAd() + " " + rezervasyon.getOgrenci().getSoyad());
        }
        if (rezervasyon.getKitap() != null) {
            response.setKitapId(rezervasyon.getKitap().getKitapId());
            response.setKitapBaslik(rezervasyon.getKitap().getBaslik());
        }
        return response;
    }
}
