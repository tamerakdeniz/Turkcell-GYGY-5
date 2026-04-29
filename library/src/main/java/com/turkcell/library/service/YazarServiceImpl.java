package com.turkcell.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.library.dto.yazar.CreateYazarRequest;
import com.turkcell.library.dto.yazar.UpdateYazarRequest;
import com.turkcell.library.dto.yazar.YazarResponse;
import com.turkcell.library.entity.Yazar;
import com.turkcell.library.exception.EntityNotFoundException;
import com.turkcell.library.repository.YazarRepository;

@Service
public class YazarServiceImpl {

    private final YazarRepository yazarRepository;

    public YazarServiceImpl(YazarRepository yazarRepository) {
        this.yazarRepository = yazarRepository;
    }

    public YazarResponse create(CreateYazarRequest request) {
        Yazar yazar = new Yazar();
        yazar.setAd(request.getAd());
        yazar.setSoyad(request.getSoyad());
        yazar.setDogumTarihi(request.getDogumTarihi());
        yazar.setOlumTarihi(request.getOlumTarihi());
        yazar.setUyruk(request.getUyruk());
        yazar.setBiyografi(request.getBiyografi());
        return toResponse(yazarRepository.save(yazar));
    }

    public List<YazarResponse> getAll() {
        return yazarRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public YazarResponse getById(Long id) {
        Yazar yazar = yazarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yazar", id));
        return toResponse(yazar);
    }

    public YazarResponse update(Long id, UpdateYazarRequest request) {
        Yazar yazar = yazarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yazar", id));
        yazar.setAd(request.getAd());
        yazar.setSoyad(request.getSoyad());
        yazar.setDogumTarihi(request.getDogumTarihi());
        yazar.setOlumTarihi(request.getOlumTarihi());
        yazar.setUyruk(request.getUyruk());
        yazar.setBiyografi(request.getBiyografi());
        return toResponse(yazarRepository.save(yazar));
    }

    public void delete(Long id) {
        Yazar yazar = yazarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yazar", id));
        yazarRepository.delete(yazar);
    }

    private YazarResponse toResponse(Yazar yazar) {
        YazarResponse response = new YazarResponse();
        response.setYazarId(yazar.getYazarId());
        response.setAd(yazar.getAd());
        response.setSoyad(yazar.getSoyad());
        response.setDogumTarihi(yazar.getDogumTarihi());
        response.setOlumTarihi(yazar.getOlumTarihi());
        response.setUyruk(yazar.getUyruk());
        response.setBiyografi(yazar.getBiyografi());
        return response;
    }
}
