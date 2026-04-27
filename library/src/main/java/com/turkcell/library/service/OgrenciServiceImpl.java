package com.turkcell.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.ogrenci.CreateOgrenciRequest;
import com.turkcell.library.dto.ogrenci.OgrenciResponse;
import com.turkcell.library.dto.ogrenci.UpdateOgrenciRequest;
import com.turkcell.library.entity.Ogrenci;
import com.turkcell.library.repository.OgrenciRepository;

@Service
public class OgrenciServiceImpl {

    private final OgrenciRepository ogrenciRepository;

    public OgrenciServiceImpl(OgrenciRepository ogrenciRepository) {
        this.ogrenciRepository = ogrenciRepository;
    }

    public OgrenciResponse create(CreateOgrenciRequest request) {
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setOgrenciNo(request.getOgrenciNo());
        ogrenci.setTcKimlik(request.getTcKimlik());
        ogrenci.setAd(request.getAd());
        ogrenci.setSoyad(request.getSoyad());
        ogrenci.setEmail(request.getEmail());
        ogrenci.setTelefon(request.getTelefon());
        ogrenci.setAdres(request.getAdres());
        ogrenci.setDogumTarihi(request.getDogumTarihi());
        ogrenci.setCinsiyet(request.getCinsiyet());
        ogrenci.setKayitTarihi(LocalDate.now());
        ogrenci.setAktifMi(request.getAktifMi() == null ? Boolean.TRUE : request.getAktifMi());
        return toResponse(ogrenciRepository.save(ogrenci));
    }

    public List<OgrenciResponse> getAll() {
        return ogrenciRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OgrenciResponse getById(Long id) {
        Ogrenci ogrenci = ogrenciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + id));
        return toResponse(ogrenci);
    }

    public OgrenciResponse update(Long id, UpdateOgrenciRequest request) {
        Ogrenci ogrenci = ogrenciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + id));
        ogrenci.setOgrenciNo(request.getOgrenciNo());
        ogrenci.setTcKimlik(request.getTcKimlik());
        ogrenci.setAd(request.getAd());
        ogrenci.setSoyad(request.getSoyad());
        ogrenci.setEmail(request.getEmail());
        ogrenci.setTelefon(request.getTelefon());
        ogrenci.setAdres(request.getAdres());
        ogrenci.setDogumTarihi(request.getDogumTarihi());
        ogrenci.setCinsiyet(request.getCinsiyet());
        ogrenci.setAktifMi(request.getAktifMi());
        return toResponse(ogrenciRepository.save(ogrenci));
    }

    public void delete(Long id) {
        Ogrenci ogrenci = ogrenciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı: " + id));
        ogrenciRepository.delete(ogrenci);
    }

    private OgrenciResponse toResponse(Ogrenci ogrenci) {
        OgrenciResponse response = new OgrenciResponse();
        response.setOgrenciId(ogrenci.getOgrenciId());
        response.setOgrenciNo(ogrenci.getOgrenciNo());
        response.setTcKimlik(ogrenci.getTcKimlik());
        response.setAd(ogrenci.getAd());
        response.setSoyad(ogrenci.getSoyad());
        response.setEmail(ogrenci.getEmail());
        response.setTelefon(ogrenci.getTelefon());
        response.setAdres(ogrenci.getAdres());
        response.setDogumTarihi(ogrenci.getDogumTarihi());
        response.setCinsiyet(ogrenci.getCinsiyet());
        response.setKayitTarihi(ogrenci.getKayitTarihi());
        response.setAktifMi(ogrenci.getAktifMi());
        return response;
    }
}
