package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.NewsService;
import com.tvz.hr.craftify.service.dto.FilterNewsDTO;
import com.tvz.hr.craftify.service.dto.NewsDTO;
import com.tvz.hr.craftify.service.dto.NewsPostPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
//@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/all")
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        List<NewsDTO> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(newsDTO -> ResponseEntity.ok().body(newsDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<NewsPostPutDTO> createNews(@RequestBody NewsDTO newsDTO) {
        NewsPostPutDTO createdNews = newsService.createNews(newsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsPostPutDTO> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO) {
        NewsPostPutDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<NewsDTO>> getFilteredNews(@RequestBody FilterNewsDTO filterDTO) {
        if (filterDTO.getCategoryId() == null) {
            return ResponseEntity.badRequest().build();
        }
        List<NewsDTO> filteredNews = newsService.getFilteredNews(filterDTO);
        if (filteredNews.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(filteredNews);
        }
    }
}