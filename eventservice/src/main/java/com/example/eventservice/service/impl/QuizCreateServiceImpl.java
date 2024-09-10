package com.example.eventservice.service.impl;

import com.example.eventservice.model.event.entity.EventGamesEntity;
import com.example.eventservice.model.quiz.dto.request.QuizAnswerCreateRequest;
import com.example.eventservice.model.quiz.dto.request.QuizQuestionCreateRequest;
import com.example.eventservice.model.quiz.entity.QuizAnswerEntity;
import com.example.eventservice.model.quiz.entity.QuizQuestionsEntity;
import com.example.eventservice.model.quiz.mapper.QuizAnswerCreateRequestToQuizAnswerEntityMapper;
import com.example.eventservice.model.quiz.mapper.QuizQuestionCreateRequestToQuizQuestionsEntityMapper;
import com.example.eventservice.repository.EventGamesRepository;
import com.example.eventservice.repository.QuizAnswerRepository;
import com.example.eventservice.repository.QuizQuestionsRepository;
import com.example.eventservice.service.QuizCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizCreateServiceImpl implements QuizCreateService {
    private final QuizQuestionsRepository quizQuestionsRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final QuizQuestionCreateRequestToQuizQuestionsEntityMapper quizQuestionCreateRequestToQuizQuestionsEntityMapper = QuizQuestionCreateRequestToQuizQuestionsEntityMapper.initialize();
    private final QuizAnswerCreateRequestToQuizAnswerEntityMapper quizAnswerCreateRequestToQuizAnswerEntityMapper = QuizAnswerCreateRequestToQuizAnswerEntityMapper.initialize();
    private final EventGamesRepository eventGamesRepository;
    private final TextToSpeechService textToSpeechService;
    @Override
    public QuizQuestionsEntity createQuizQuestionsForEventGame(QuizQuestionCreateRequest quizQuestionCreateRequest) {
        Optional<EventGamesEntity> eventGames = eventGamesRepository.findById(quizQuestionCreateRequest.getEventgameId());

        if (eventGames.isPresent()) {
            final QuizQuestionsEntity quizQuestionsEntity = quizQuestionCreateRequestToQuizQuestionsEntityMapper.mapForSaving(quizQuestionCreateRequest);

            try {
                String file = textToSpeechService.textToSpeech(quizQuestionsEntity.getTitle());
                quizQuestionsEntity.setFile(file);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Error converting text to speech: " + e.getMessage());
            }

//            quizQuestionsEntity.setFile("file");
            quizQuestionsEntity.setCreatedAt(LocalDateTime.now());
            quizQuestionsEntity.setEventGameId(eventGames.get().getId());

            return quizQuestionsRepository.save(quizQuestionsEntity);
        }

        return null;
    }


    @Override
    public QuizAnswerEntity createQuizAnswerForEventGame(QuizAnswerCreateRequest quizAnswerCreateRequest) {
        Optional<QuizQuestionsEntity> quizQuestions = quizQuestionsRepository.findById(quizAnswerCreateRequest.getQuestionId());
        if (quizQuestions.isPresent()) {
            final QuizAnswerEntity quizAnswerEntity = quizAnswerCreateRequestToQuizAnswerEntityMapper.mapForSaving(quizAnswerCreateRequest);

            quizAnswerEntity.setCreatedAt(LocalDateTime.now());
            quizAnswerEntity.setQuestion(quizQuestions.get());

            return quizAnswerRepository.save(quizAnswerEntity);
        }
        return null;
    }
}
