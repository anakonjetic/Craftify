package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.News;
import com.tvz.hr.craftify.repository.NewsRepository;
import com.tvz.hr.craftify.service.dto.FilterNewsDTO;
import com.tvz.hr.craftify.service.dto.NewsDTO;
import com.tvz.hr.craftify.service.dto.NewsPostPutDTO;
import com.tvz.hr.craftify.utilities.MapFromDTOHelper;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService{

    NewsRepository newsRepository;

    @Override
    public List<NewsDTO> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return newsList.stream()
                .map(MapToDTOHelper::mapToNewsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NewsDTO> getNewsById(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        return newsOptional.map(MapToDTOHelper::mapToNewsDTO);
    }

    @Override
    public NewsPostPutDTO createNews(NewsDTO newsDTO) {
        News news = MapFromDTOHelper.mapNewsDTOToNews(newsDTO);
        News savedNews = newsRepository.save(news);
        return MapToDTOHelper.mapToNewsPostPutDTO(savedNews);
    }

    @Override
    public NewsPostPutDTO updateNews(Long id, NewsDTO newsDTO) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News with id " + id + " not found");
        }
        newsDTO.setId(id);
        News news = MapFromDTOHelper.mapNewsDTOToNews(newsDTO);
        News updatedNews = newsRepository.save(news);
        return MapToDTOHelper.mapToNewsPostPutDTO(updatedNews);
    }

    @Override
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News with id " + id + " not found");
        }
        newsRepository.deleteById(id);
    }

    @Override
    public List<NewsDTO> getFilteredNews(FilterNewsDTO filterDTO) {
        if (filterDTO.getCategoryId() != null) {
            List<News> newsList = newsRepository.findByCategoryId(filterDTO.getCategoryId());
            return newsList.stream()
                    .map(MapToDTOHelper::mapToNewsDTO)
                    .collect(Collectors.toList());
        } else {
            List<News> allNews = newsRepository.findAll();
            return allNews.stream()
                    .map(MapToDTOHelper::mapToNewsDTO)
                    .collect(Collectors.toList());
        }
    }
}
