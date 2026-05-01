package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.GetTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.dto.UpdatedTagResponse;
import com.turkcell.spring_starter.entity.Tag;
import com.turkcell.spring_starter.exception.EntityNotFoundException;
import com.turkcell.spring_starter.repository.TagRepository;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public CreatedTagResponse create(CreateTagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.name());

        tag = tagRepository.save(tag);

        return new CreatedTagResponse(tag.getId(), tag.getName());
    }

    @Override
    public List<ListTagResponse> getAll() {
        return tagRepository.findAll().stream()
                .map(tag -> new ListTagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public GetTagResponse getById(UUID id) {
        Tag tag = getTagById(id);
        return new GetTagResponse(tag.getId(), tag.getName());
    }

    @Override
    public UpdatedTagResponse update(UUID id, UpdateTagRequest request) {
        Tag tag = getTagById(id);
        tag.setName(request.name());
        tag = tagRepository.save(tag);

        return new UpdatedTagResponse(tag.getId(), tag.getName());
    }

    @Override
    public void delete(UUID id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Etiket bulunamadı: " + id));
    }
}
