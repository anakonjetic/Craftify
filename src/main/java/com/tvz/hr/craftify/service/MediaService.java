package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.service.dto.MediaDTO;
import com.tvz.hr.craftify.service.dto.MediaGetDTO;
import com.tvz.hr.craftify.service.dto.MediaPutPostDTO;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    List<MediaDTO> getAllMedia();
    Optional<MediaGetDTO> getMedia(Long id);
    Optional<List<MediaGetDTO>> getMediaByIds(List<Long> ids);
    MediaGetDTO addMedia(MediaPutPostDTO media);
    MediaGetDTO updateMedia(MediaPutPostDTO media, Long id);
    void deleteMedia(Long id);
}
