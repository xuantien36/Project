package com.t3h.project.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.t3h.project.model.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news WHERE isFavorite = 1")
    List<News> getNewsFavorite();


    @Insert
    void insert(News item);

    @Insert
    void insertAll(News... news);

    @Delete
    void delete(News... news);

    @Query("DELETE FROM news")
    void deleteAll();

    @Query("UPDATE news SET isFavorite =1 WHERE id= :id")
    void setFavorite(long id);

    @Query("UPDATE news SET isFavorite =0 WHERE id= :id")
    void delFavorite(long id);
}
