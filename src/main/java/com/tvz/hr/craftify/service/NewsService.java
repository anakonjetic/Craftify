package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.FilterNewsDTO;
import com.tvz.hr.craftify.service.dto.NewsDTO;
import com.tvz.hr.craftify.service.dto.NewsPostPutDTO;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<NewsDTO> getAllNews();
    Optional<NewsDTO> getNewsById(Long id);
    NewsPostPutDTO createNews(NewsDTO newsDTO);
    NewsPostPutDTO updateNews(Long id, NewsDTO newsDTO);
    List<NewsDTO> getFilteredNews(FilterNewsDTO filterDTO);
    void deleteNews(Long id);
}
