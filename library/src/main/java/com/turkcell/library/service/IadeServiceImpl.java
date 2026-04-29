package com.turkcell.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.iade.CreateIadeRequest;
import com.turkcell.library.dto.iade.IadeResponse;
import com.turkcell.library.dto.iade.UpdateIadeRequest;
import com.turkcell.library.entity.Gorevli;
import com.turkcell.library.entity.Iade;
import com.turkcell.library.entity.KitapKopya;
import com.turkcell.library.entity.OduncAlma;
import com.turkcell.library.exception.EntityNotFoundException;
import com.turkcell.library.repository.GorevliRepository;
import com.turkcell.library.repository.IadeRepository;
import com.turkcell.library.repository.KitapKopyaRepository;
import com.turkcell.library.repository.OduncAlmaRepository;

@Service
public class IadeServiceImpl {

    private final IadeRepository iadeRepository;
    private final OduncAlmaRepository oduncAlmaRepository;
    private final GorevliRepository gorevliRepository;
    private final KitapKopyaRepository kitapKopyaRepository;

    public IadeServiceImpl(IadeRepository iadeRepository,
                            OduncAlmaRepository oduncAlmaRepository,
                            GorevliRepository gorevliRepository,
                            KitapKopyaRepository kitapKopyaRepository) {
        this.iadeRepository = iadeRepository;
        this.oduncAlmaRepository = oduncAlmaRepository;
        this.gorevliRepository = gorevliRepository;
        this.kitapKopyaRepository = kitapKopyaRepository;
    }

    public IadeResponse create(CreateIadeRequest request) {
        OduncAlma oduncAlma = oduncAlmaRepository.findById(request.getOduncId())
                .orElseThrow(() -> new EntityNotFoundException("Ödünç kaydı", request.getOduncId()));
        Gorevli gorevli = gorevliRepository.findById(request.getGorevliId())
                .orElseThrow(() -> new EntityNotFoundException("Görevli", request.getGorevliId()));

        Iade iade = new Iade();
        iade.setOduncAlma(oduncAlma);
        iade.setGorevli(gorevli);
        iade.setIadeTarihi(LocalDateTime.now());
        iade.setKitapDurumu(request.getKitapDurumu());
        iade.setNotlar(request.getNotlar());

        oduncAlma.setDurum("iade_edildi");
        oduncAlmaRepository.save(oduncAlma);

        KitapKopya kopya = oduncAlma.getKopya();
        if (kopya != null) {
            String yeniDurum = switch (request.getKitapDurumu()) {
                case "hasarli" -> "hasarli";
                case "kayip" -> "kayip";
                default -> "musait";
            };
            kopya.setDurum(yeniDurum);
            kitapKopyaRepository.save(kopya);
        }

        return toResponse(iadeRepository.save(iade));
    }

    public List<IadeResponse> getAll() {
        return iadeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public IadeResponse getById(Long id) {
        Iade iade = iadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("İade kaydı", id));
        return toResponse(iade);
    }

    public IadeResponse update(Long id, UpdateIadeRequest request) {
        Iade iade = iadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("İade kaydı", id));
        Gorevli gorevli = gorevliRepository.findById(request.getGorevliId())
                .orElseThrow(() -> new EntityNotFoundException("Görevli", request.getGorevliId()));

        iade.setGorevli(gorevli);
        iade.setKitapDurumu(request.getKitapDurumu());
        iade.setNotlar(request.getNotlar());
        return toResponse(iadeRepository.save(iade));
    }

    public void delete(Long id) {
        Iade iade = iadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("İade kaydı", id));
        iadeRepository.delete(iade);
    }

    private IadeResponse toResponse(Iade iade) {
        IadeResponse response = new IadeResponse();
        response.setIadeId(iade.getIadeId());
        response.setIadeTarihi(iade.getIadeTarihi());
        response.setKitapDurumu(iade.getKitapDurumu());
        response.setNotlar(iade.getNotlar());
        if (iade.getOduncAlma() != null) {
            response.setOduncId(iade.getOduncAlma().getOduncId());
        }
        if (iade.getGorevli() != null) {
            response.setGorevliId(iade.getGorevli().getGorevliId());
            response.setGorevliAdSoyad(iade.getGorevli().getAd() + " " + iade.getGorevli().getSoyad());
        }
        return response;
    }
}
