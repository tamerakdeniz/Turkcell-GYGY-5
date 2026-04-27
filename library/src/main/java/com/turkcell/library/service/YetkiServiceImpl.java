package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.yetki.CreateYetkiRequest;
import com.turkcell.library.dto.yetki.UpdateYetkiRequest;
import com.turkcell.library.dto.yetki.YetkiResponse;
import com.turkcell.library.entity.Yetki;
import com.turkcell.library.repository.YetkiRepository;

@Service
public class YetkiServiceImpl {

    private final YetkiRepository yetkiRepository;

    public YetkiServiceImpl(YetkiRepository yetkiRepository) {
        this.yetkiRepository = yetkiRepository;
    }

    public YetkiResponse create(CreateYetkiRequest request) {
        Yetki yetki = new Yetki();
        yetki.setYetkiAd(request.getYetkiAd());
        yetki.setAciklama(request.getAciklama());
        yetki.setModul(request.getModul());
        return toResponse(yetkiRepository.save(yetki));
    }

    public List<YetkiResponse> getAll() {
        return yetkiRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public YetkiResponse getById(Long id) {
        Yetki yetki = yetkiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yetki bulunamadı: " + id));
        return toResponse(yetki);
    }

    public YetkiResponse update(Long id, UpdateYetkiRequest request) {
        Yetki yetki = yetkiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yetki bulunamadı: " + id));
        yetki.setYetkiAd(request.getYetkiAd());
        yetki.setAciklama(request.getAciklama());
        yetki.setModul(request.getModul());
        return toResponse(yetkiRepository.save(yetki));
    }

    public void delete(Long id) {
        Yetki yetki = yetkiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yetki bulunamadı: " + id));
        yetkiRepository.delete(yetki);
    }

    private YetkiResponse toResponse(Yetki yetki) {
        YetkiResponse response = new YetkiResponse();
        response.setYetkiId(yetki.getYetkiId());
        response.setYetkiAd(yetki.getYetkiAd());
        response.setAciklama(yetki.getAciklama());
        response.setModul(yetki.getModul());
        return response;
    }
}
