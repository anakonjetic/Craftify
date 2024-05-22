package com.tvz.hr.craftify.service;

public interface LikesAndFavoritesService {
    void addToFavorites(Long userId, Long projectId);
    void removeFromFavorites(Long userId, Long projectId);
    void userLikeAction(Long userId, Long projectId);
    void userDislikeAction(Long userId, Long projectId);
}
