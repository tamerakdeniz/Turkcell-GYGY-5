package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.kitapkopya.CreateKitapKopyaRequest;
import com.turkcell.library.dto.kitapkopya.KitapKopyaResponse;
import com.turkcell.library.dto.kitapkopya.UpdateKitapKopyaRequest;
import com.turkcell.library.entity.Kitap;
import com.turkcell.library.entity.KitapKopya;
import com.turkcell.library.exception.EntityNotFoundException;
import com.turkcell.library.repository.KitapKopyaRepository;
import com.turkcell.library.repository.KitapRepository;

@Service
public class KitapKopyaServiceImpl {

    private final KitapKopyaRepository kitapKopyaRepository;
    private final KitapRepository kitapRepository;

    public KitapKopyaServiceImpl(KitapKopyaRepository kitapKopyaRepository, KitapRepository kitapRepository) {
        this.kitapKopyaRepository = kitapKopyaRepository;
        this.kitapRepository = kitapRepository;
    }

    public KitapKopyaResponse create(CreateKitapKopyaRequest request) {
        Kitap kitap = kitapRepository.findById(request.getKitapId())
                .orElseThrow(() -> new EntityNotFoundException("Kitap", request.getKitapId()));

        KitapKopya kopya = new KitapKopya();
        kopya.setKitap(kitap);
        kopya.setBarkod(request.getBarkod());
        kopya.setRafNo(request.getRafNo());
        kopya.setDurum(request.getDurum() != null ? request.getDurum() : "musait");
        kopya.setAlimTarihi(request.getAlimTarihi());
        kopya.setFiyat(request.getFiyat());
        return toResponse(kitapKopyaRepository.save(kopya));
    }

    public List<KitapKopyaResponse> getAll() {
        return kitapKopyaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public KitapKopyaResponse getById(Long id) {
        KitapKopya kopya = kitapKopyaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitap kopyası", id));
        return toResponse(kopya);
    }

    public KitapKopyaResponse update(Long id, UpdateKitapKopyaRequest request) {
        KitapKopya kopya = kitapKopyaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitap kopyası", id));
        Kitap kitap = kitapRepository.findById(request.getKitapId())
                .orElseThrow(() -> new EntityNotFoundException("Kitap", request.getKitapId()));

        kopya.setKitap(kitap);
        kopya.setBarkod(request.getBarkod());
        kopya.setRafNo(request.getRafNo());
        kopya.setDurum(request.getDurum());
        kopya.setAlimTarihi(request.getAlimTarihi());
        kopya.setFiyat(request.getFiyat());
        return toResponse(kitapKopyaRepository.save(kopya));
    }

    public void delete(Long id) {
        KitapKopya kopya = kitapKopyaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitap kopyası", id));
        kitapKopyaRepository.delete(kopya);
    }

    private KitapKopyaResponse toResponse(KitapKopya kopya) {
        KitapKopyaResponse response = new KitapKopyaResponse();
        response.setKopyaId(kopya.getKopyaId());
        response.setBarkod(kopya.getBarkod());
        response.setRafNo(kopya.getRafNo());
        response.setDurum(kopya.getDurum());
        response.setAlimTarihi(kopya.getAlimTarihi());
        response.setFiyat(kopya.getFiyat());
        if (kopya.getKitap() != null) {
            response.setKitapId(kopya.getKitap().getKitapId());
            response.setKitapBaslik(kopya.getKitap().getBaslik());
        }
        return response;
    }
}
