package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.yayinevi.CreateYayineviRequest;
import com.turkcell.library.dto.yayinevi.UpdateYayineviRequest;
import com.turkcell.library.dto.yayinevi.YayineviResponse;
import com.turkcell.library.entity.Yayinevi;
import com.turkcell.library.repository.YayineviRepository;

@Service
public class YayineviServiceImpl {

    private final YayineviRepository yayineviRepository;

    public YayineviServiceImpl(YayineviRepository yayineviRepository) {
        this.yayineviRepository = yayineviRepository;
    }

    public YayineviResponse create(CreateYayineviRequest request) {
        Yayinevi yayinevi = new Yayinevi();
        yayinevi.setAd(request.getAd());
        yayinevi.setAdres(request.getAdres());
        yayinevi.setTelefon(request.getTelefon());
        yayinevi.setEmail(request.getEmail());
        yayinevi.setWebSitesi(request.getWebSitesi());
        return toResponse(yayineviRepository.save(yayinevi));
    }

    public List<YayineviResponse> getAll() {
        return yayineviRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public YayineviResponse getById(Long id) {
        Yayinevi yayinevi = yayineviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + id));
        return toResponse(yayinevi);
    }

    public YayineviResponse update(Long id, UpdateYayineviRequest request) {
        Yayinevi yayinevi = yayineviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + id));
        yayinevi.setAd(request.getAd());
        yayinevi.setAdres(request.getAdres());
        yayinevi.setTelefon(request.getTelefon());
        yayinevi.setEmail(request.getEmail());
        yayinevi.setWebSitesi(request.getWebSitesi());
        return toResponse(yayineviRepository.save(yayinevi));
    }

    public void delete(Long id) {
        Yayinevi yayinevi = yayineviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + id));
        yayineviRepository.delete(yayinevi);
    }

    private YayineviResponse toResponse(Yayinevi yayinevi) {
        YayineviResponse response = new YayineviResponse();
        response.setYayineviId(yayinevi.getYayineviId());
        response.setAd(yayinevi.getAd());
        response.setAdres(yayinevi.getAdres());
        response.setTelefon(yayinevi.getTelefon());
        response.setEmail(yayinevi.getEmail());
        response.setWebSitesi(yayinevi.getWebSitesi());
        return response;
    }
}
