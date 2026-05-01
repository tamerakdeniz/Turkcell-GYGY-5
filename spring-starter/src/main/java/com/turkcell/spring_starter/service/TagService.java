package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.GetTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.dto.UpdatedTagResponse;
import com.turkcell.spring_starter.entity.Tag;

public interface TagService {
    CreatedTagResponse create(CreateTagRequest request);

    List<ListTagResponse> getAll();

    GetTagResponse getById(UUID id);

    UpdatedTagResponse update(UUID id, UpdateTagRequest request);

    void delete(UUID id);

    // Service-to-service çağrılar için: Tag entity'sini doğrulayıp döner.
    Tag getTagById(UUID id);
}
