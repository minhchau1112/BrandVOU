package com.example.eventservice.repository;

import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswerEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM QuizAnswerEntity qa WHERE qa.question.id IN (SELECT qq.id FROM QuizQuestionsEntity qq WHERE qq.eventGameId = :eventgameId)")
    int deleteAllQuizAnswerOfEventGame(@Param("eventgameId") Long eventgameId);
    @Query("SELECT qa FROM QuizAnswerEntity qa JOIN QuizQuestionsEntity qq ON qq.id = qa.question.id WHERE qq.eventGameId= :eventgameId")
    Page<QuizAnswerEntity> getQuizAnswerOfEventGame(@Param("eventgameId") Long eventgameId, Pageable pageable);
}
