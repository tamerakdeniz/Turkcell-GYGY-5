package com.turkcell.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.ceza.CezaResponse;
import com.turkcell.library.dto.ceza.CreateCezaRequest;
import com.turkcell.library.dto.ceza.UpdateCezaRequest;
import com.turkcell.library.entity.Ceza;
import com.turkcell.library.entity.OduncAlma;
import com.turkcell.library.entity.Ogrenci;
import com.turkcell.library.repository.CezaRepository;
import com.turkcell.library.repository.OduncAlmaRepository;
import com.turkcell.library.repository.OgrenciRepository;

@Service
public class CezaServiceImpl {

    private final CezaRepository cezaRepository;
    private final OgrenciRepository ogrenciRepository;
    private final OduncAlmaRepository oduncAlmaRepository;

    public CezaServiceImpl(CezaRepository cezaRepository,
                            OgrenciRepository ogrenciRepository,
                            OduncAlmaRepository oduncAlmaRepository) {
        this.cezaRepository = cezaRepository;
        this.ogrenciRepository = ogrenciRepository;
        this.oduncAlmaRepository = oduncAlmaRepository;
    }

    public CezaResponse create(CreateCezaRequest request) {
        Ogrenci ogrenci = ogrenciRepository.findById(request.getOgrenciId())
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + request.getOgrenciId()));
        OduncAlma oduncAlma = oduncAlmaRepository.findById(request.getOduncId())
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı: " + request.getOduncId()));

        Ceza ceza = new Ceza();
        ceza.setOgrenci(ogrenci);
        ceza.setOduncAlma(oduncAlma);
        ceza.setCezaMiktari(request.getCezaMiktari());
        ceza.setCezaNedeni(request.getCezaNedeni());
        ceza.setCezaTarihi(LocalDate.now());
        ceza.setOdendiMi(Boolean.FALSE);
        return toResponse(cezaRepository.save(ceza));
    }

    public List<CezaResponse> getAll() {
        return cezaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CezaResponse getById(Long id) {
        Ceza ceza = cezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ceza bulunamadı: " + id));
        return toResponse(ceza);
    }

    public CezaResponse update(Long id, UpdateCezaRequest request) {
        Ceza ceza = cezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ceza bulunamadı: " + id));
        ceza.setCezaMiktari(request.getCezaMiktari());
        ceza.setCezaNedeni(request.getCezaNedeni());
        ceza.setOdendiMi(request.getOdendiMi());
        ceza.setOdemeTarihi(request.getOdemeTarihi());
        return toResponse(cezaRepository.save(ceza));
    }

    public void delete(Long id) {
        Ceza ceza = cezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ceza bulunamadı: " + id));
        cezaRepository.delete(ceza);
    }

    private CezaResponse toResponse(Ceza ceza) {
        CezaResponse response = new CezaResponse();
        response.setCezaId(ceza.getCezaId());
        response.setCezaMiktari(ceza.getCezaMiktari());
        response.setCezaNedeni(ceza.getCezaNedeni());
        response.setCezaTarihi(ceza.getCezaTarihi());
        response.setOdendiMi(ceza.getOdendiMi());
        response.setOdemeTarihi(ceza.getOdemeTarihi());
        if (ceza.getOgrenci() != null) {
            response.setOgrenciId(ceza.getOgrenci().getOgrenciId());
            response.setOgrenciAdSoyad(ceza.getOgrenci().getAd() + " " + ceza.getOgrenci().getSoyad());
        }
        if (ceza.getOduncAlma() != null) {
            response.setOduncId(ceza.getOduncAlma().getOduncId());
        }
        return response;
    }
}
