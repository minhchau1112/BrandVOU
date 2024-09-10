package com.example.eventservice.repository;

import com.example.eventservice.model.quiz.dto.request.RowQuestionResponse;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuizQuestionsRepository extends JpaRepository<QuizQuestionsEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM QuizQuestionsEntity qq WHERE qq.eventGameId = :eventGameId")
    void deleteAllQuizQuestionOfEventGame(@Param("eventGameId") Long eventGameId);
    @Query("SELECT qq FROM QuizQuestionsEntity qq WHERE qq.eventGameId= :eventgameId")
    Page<QuizQuestionsEntity> getQuizQuestionsOfEventGame(@Param("eventgameId") Long eventgameId, Pageable pageable);

    @Query("SELECT new com.example.eventservice.model.quiz.dto.request.RowQuestionResponse( " +
            "q.title, " +
            "MAX(CASE WHEN a.isCorrect = true THEN a.answerText ELSE '' END), " +
            "MAX(CASE WHEN a.isCorrect = false THEN a.answerText ELSE '' END), " +
            "MAX(CASE WHEN a.isCorrect = false THEN a.answerText ELSE '' END), " +
            "MAX(CASE WHEN a.isCorrect = false THEN a.answerText ELSE '' END)) " +
            "FROM QuizQuestionsEntity q " +
            "JOIN QuizAnswerEntity a ON q.id = a.question.id " +
            "JOIN EventGamesEntity eg ON eg.id = q.eventGameId " +
            "WHERE eg.event.id = :eventid AND eg.game.id = :gameid " +
            "GROUP BY q.title")
    List<RowQuestionResponse> findQuizByEventGameId(@Param("eventid") Long eventid, @Param("gameid") Long gameid);



}
