package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.gorevli.CreateGorevliRequest;
import com.turkcell.library.dto.gorevli.GorevliResponse;
import com.turkcell.library.dto.gorevli.UpdateGorevliRequest;
import com.turkcell.library.entity.Gorevli;
import com.turkcell.library.exception.EntityNotFoundException;
import com.turkcell.library.repository.GorevliRepository;

@Service
public class GorevliServiceImpl {

    private final GorevliRepository gorevliRepository;

    public GorevliServiceImpl(GorevliRepository gorevliRepository) {
        this.gorevliRepository = gorevliRepository;
    }

    public GorevliResponse create(CreateGorevliRequest request) {
        Gorevli gorevli = new Gorevli();
        gorevli.setTcKimlik(request.getTcKimlik());
        gorevli.setKullaniciAdi(request.getKullaniciAdi());
        gorevli.setAd(request.getAd());
        gorevli.setSoyad(request.getSoyad());
        gorevli.setEmail(request.getEmail());
        gorevli.setTelefon(request.getTelefon());
        gorevli.setPozisyon(request.getPozisyon());
        gorevli.setIseBaslamaTarihi(request.getIseBaslamaTarihi());
        gorevli.setMaas(request.getMaas());
        gorevli.setSifreHash(request.getSifreHash());
        gorevli.setAktifMi(request.getAktifMi() == null ? Boolean.TRUE : request.getAktifMi());
        return toResponse(gorevliRepository.save(gorevli));
    }

    public List<GorevliResponse> getAll() {
        return gorevliRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public GorevliResponse getById(Long id) {
        Gorevli gorevli = gorevliRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Görevli", id));
        return toResponse(gorevli);
    }

    public GorevliResponse update(Long id, UpdateGorevliRequest request) {
        Gorevli gorevli = gorevliRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Görevli", id));
        gorevli.setTcKimlik(request.getTcKimlik());
        gorevli.setKullaniciAdi(request.getKullaniciAdi());
        gorevli.setAd(request.getAd());
        gorevli.setSoyad(request.getSoyad());
        gorevli.setEmail(request.getEmail());
        gorevli.setTelefon(request.getTelefon());
        gorevli.setPozisyon(request.getPozisyon());
        gorevli.setIseBaslamaTarihi(request.getIseBaslamaTarihi());
        gorevli.setMaas(request.getMaas());
        if (request.getSifreHash() != null) {
            gorevli.setSifreHash(request.getSifreHash());
        }
        gorevli.setAktifMi(request.getAktifMi());
        return toResponse(gorevliRepository.save(gorevli));
    }

    public void delete(Long id) {
        Gorevli gorevli = gorevliRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Görevli", id));
        gorevliRepository.delete(gorevli);
    }

    private GorevliResponse toResponse(Gorevli gorevli) {
        GorevliResponse response = new GorevliResponse();
        response.setGorevliId(gorevli.getGorevliId());
        response.setTcKimlik(gorevli.getTcKimlik());
        response.setKullaniciAdi(gorevli.getKullaniciAdi());
        response.setAd(gorevli.getAd());
        response.setSoyad(gorevli.getSoyad());
        response.setEmail(gorevli.getEmail());
        response.setTelefon(gorevli.getTelefon());
        response.setPozisyon(gorevli.getPozisyon());
        response.setIseBaslamaTarihi(gorevli.getIseBaslamaTarihi());
        response.setMaas(gorevli.getMaas());
        response.setAktifMi(gorevli.getAktifMi());
        return response;
    }
}
