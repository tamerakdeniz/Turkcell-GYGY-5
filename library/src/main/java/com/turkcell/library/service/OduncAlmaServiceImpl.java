package com.turkcell.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.oduncalma.CreateOduncAlmaRequest;
import com.turkcell.library.dto.oduncalma.OduncAlmaResponse;
import com.turkcell.library.dto.oduncalma.UpdateOduncAlmaRequest;
import com.turkcell.library.entity.Gorevli;
import com.turkcell.library.entity.KitapKopya;
import com.turkcell.library.entity.OduncAlma;
import com.turkcell.library.entity.Ogrenci;
import com.turkcell.library.repository.GorevliRepository;
import com.turkcell.library.repository.KitapKopyaRepository;
import com.turkcell.library.repository.OduncAlmaRepository;
import com.turkcell.library.repository.OgrenciRepository;

@Service
public class OduncAlmaServiceImpl {

    private final OduncAlmaRepository oduncAlmaRepository;
    private final OgrenciRepository ogrenciRepository;
    private final KitapKopyaRepository kitapKopyaRepository;
    private final GorevliRepository gorevliRepository;

    public OduncAlmaServiceImpl(OduncAlmaRepository oduncAlmaRepository,
                                 OgrenciRepository ogrenciRepository,
                                 KitapKopyaRepository kitapKopyaRepository,
                                 GorevliRepository gorevliRepository) {
        this.oduncAlmaRepository = oduncAlmaRepository;
        this.ogrenciRepository = ogrenciRepository;
        this.kitapKopyaRepository = kitapKopyaRepository;
        this.gorevliRepository = gorevliRepository;
    }

    public OduncAlmaResponse create(CreateOduncAlmaRequest request) {
        Ogrenci ogrenci = ogrenciRepository.findById(request.getOgrenciId())
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + request.getOgrenciId()));
        KitapKopya kopya = kitapKopyaRepository.findById(request.getKopyaId())
                .orElseThrow(() -> new RuntimeException("Kitap kopyası bulunamadı: " + request.getKopyaId()));
        Gorevli gorevli = gorevliRepository.findById(request.getGorevliId())
                .orElseThrow(() -> new RuntimeException("Görevli bulunamadı: " + request.getGorevliId()));

        OduncAlma oduncAlma = new OduncAlma();
        oduncAlma.setOgrenci(ogrenci);
        oduncAlma.setKopya(kopya);
        oduncAlma.setGorevli(gorevli);
        oduncAlma.setOduncTarihi(LocalDateTime.now());
        oduncAlma.setIadeTarihiBeklenen(request.getIadeTarihiBeklenen());
        oduncAlma.setDurum("aktif");

        kopya.setDurum("oduncte");
        kitapKopyaRepository.save(kopya);

        return toResponse(oduncAlmaRepository.save(oduncAlma));
    }

    public List<OduncAlmaResponse> getAll() {
        return oduncAlmaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OduncAlmaResponse getById(Long id) {
        OduncAlma oduncAlma = oduncAlmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı: " + id));
        return toResponse(oduncAlma);
    }

    public OduncAlmaResponse update(Long id, UpdateOduncAlmaRequest request) {
        OduncAlma oduncAlma = oduncAlmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı: " + id));
        Ogrenci ogrenci = ogrenciRepository.findById(request.getOgrenciId())
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + request.getOgrenciId()));
        KitapKopya kopya = kitapKopyaRepository.findById(request.getKopyaId())
                .orElseThrow(() -> new RuntimeException("Kitap kopyası bulunamadı: " + request.getKopyaId()));
        Gorevli gorevli = gorevliRepository.findById(request.getGorevliId())
                .orElseThrow(() -> new RuntimeException("Görevli bulunamadı: " + request.getGorevliId()));

        oduncAlma.setOgrenci(ogrenci);
        oduncAlma.setKopya(kopya);
        oduncAlma.setGorevli(gorevli);
        oduncAlma.setIadeTarihiBeklenen(request.getIadeTarihiBeklenen());
        oduncAlma.setDurum(request.getDurum());

        return toResponse(oduncAlmaRepository.save(oduncAlma));
    }

    public void delete(Long id) {
        OduncAlma oduncAlma = oduncAlmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı: " + id));
        oduncAlmaRepository.delete(oduncAlma);
    }

    private OduncAlmaResponse toResponse(OduncAlma oduncAlma) {
        OduncAlmaResponse response = new OduncAlmaResponse();
        response.setOduncId(oduncAlma.getOduncId());
        response.setOduncTarihi(oduncAlma.getOduncTarihi());
        response.setIadeTarihiBeklenen(oduncAlma.getIadeTarihiBeklenen());
        response.setDurum(oduncAlma.getDurum());

        if (oduncAlma.getOgrenci() != null) {
            response.setOgrenciId(oduncAlma.getOgrenci().getOgrenciId());
            response.setOgrenciAdSoyad(oduncAlma.getOgrenci().getAd() + " " + oduncAlma.getOgrenci().getSoyad());
        }
        if (oduncAlma.getKopya() != null) {
            response.setKopyaId(oduncAlma.getKopya().getKopyaId());
            response.setBarkod(oduncAlma.getKopya().getBarkod());
            if (oduncAlma.getKopya().getKitap() != null) {
                response.setKitapBaslik(oduncAlma.getKopya().getKitap().getBaslik());
            }
        }
        if (oduncAlma.getGorevli() != null) {
            response.setGorevliId(oduncAlma.getGorevli().getGorevliId());
            response.setGorevliAdSoyad(oduncAlma.getGorevli().getAd() + " " + oduncAlma.getGorevli().getSoyad());
        }
        return response;
    }
}
