package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void findByCategoryId() {
        List<News> news = newsRepository.findByCategoryId(2L);
        assertNotNull(news);
        assertFalse(news.isEmpty());
        assertEquals(news.getFirst().getTitle(),"New DIY Project: Transform Your Living Space with Upcycled Decor");
    }
}
