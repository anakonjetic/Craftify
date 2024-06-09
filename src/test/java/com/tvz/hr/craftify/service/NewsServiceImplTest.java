package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.controller.SubscriptionController;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.News;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.NewsRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.FilterNewsDTO;
import com.tvz.hr.craftify.service.dto.NewsDTO;
import com.tvz.hr.craftify.service.dto.NewsPostPutDTO;
import com.tvz.hr.craftify.utilities.MapFromDTOHelper;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//@WebMvcTest(NewsServiceImpl.class)
@Import(SecurityConfiguration.class)
@Sql("/data.sql")
class NewsServiceImplTest {



    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UsersService userService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private NewsService newsService;

    @Mock
    private CategoryService categoryService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newsService = new NewsServiceImpl(newsRepository);
    }

    @Test
    void getAllNews_ReturnsListOfNewsDTOs() {
        News news1 = new News();
        News news2 = new News();
        List<News> newsList = Arrays.asList(news1, news2);

        when(newsRepository.findAll()).thenReturn(newsList);

        List<NewsDTO> result = newsService.getAllNews();

        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void getNewsById_ReturnsNewsDTO() {
        News news = new News();
        news.setId(1L);

        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        Optional<NewsDTO> result = newsService.getNewsById(1L);

        assertTrue(result.isEmpty());
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void getNewsById_ReturnsEmptyOptional_WhenNewsNotFound() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<NewsDTO> result = newsService.getNewsById(1L);

        assertFalse(result.isPresent());
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void createNews_SavesAndReturnsNewsPostPutDTO() {
        News news = new News();
        News savedNews = new News();
        savedNews.setId(1L);
        NewsDTO newsDTO = new NewsDTO();

        when(newsRepository.save(any(News.class))).thenReturn(savedNews);

        NewsPostPutDTO result = newsService.createNews(newsDTO);

        assertEquals(1L, result.getId());
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void updateNews_UpdatesAndReturnsNewsPostPutDTO() {
        News news = new News();
        news.setId(1L);
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);

        when(newsRepository.existsById(1L)).thenReturn(true);
        when(newsRepository.save(any(News.class))).thenReturn(news);

        NewsPostPutDTO result = newsService.updateNews(1L, newsDTO);

        assertEquals(1L, result.getId());
        verify(newsRepository, times(1)).existsById(1L);
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void updateNews_ThrowsEntityNotFoundException_WhenNewsNotFound() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);

        when(newsRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> newsService.updateNews(1L, newsDTO));
        verify(newsRepository, times(1)).existsById(1L);
        verify(newsRepository, times(0)).save(any(News.class));
    }

    @Test
    void deleteNews_DeletesNews() {
        when(newsRepository.existsById(1L)).thenReturn(true);

        newsService.deleteNews(1L);

        verify(newsRepository, times(1)).existsById(1L);
        verify(newsRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNews_ThrowsEntityNotFoundException_WhenNewsNotFound() {
        when(newsRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> newsService.deleteNews(1L));
        verify(newsRepository, times(1)).existsById(1L);
        verify(newsRepository, times(0)).deleteById(1L);
    }

    @Test
    void getFilteredNews_ReturnsFilteredNews() {
        FilterNewsDTO filterDTO = new FilterNewsDTO();
        filterDTO.setCategoryId(1L);
        Long categoryId1 = 1L;
        Long categoryId2 = 2L;
        News news1 = new News();
        news1.setCategory(categoryRepository.findById(categoryId1).orElse(null));
        News news2 = new News();
        news2.setCategory(categoryRepository.findById(categoryId2).orElse(null));
        List<News> newsList = Arrays.asList(news1, news2);

        when(newsRepository.findByCategoryId(1L)).thenReturn(newsList);

        List<NewsDTO> result = newsService.getFilteredNews(filterDTO);

        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void getFilteredNews_ReturnsAllNews_WhenCategoryIdIsNull() {
        FilterNewsDTO filterDTO = new FilterNewsDTO();
        List<News> newsList = Arrays.asList(new News(), new News());

        when(newsRepository.findAll()).thenReturn(newsList);

        List<NewsDTO> result = newsService.getFilteredNews(filterDTO);

        assertEquals(2, result.size());
        verify(newsRepository, times(1)).findAll();
        verify(newsRepository, times(0)).findByCategoryId(anyLong());
    }
}
