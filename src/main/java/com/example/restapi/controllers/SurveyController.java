package com.example.restapi.controllers;

import com.example.restapi.service.SurveyService;
import com.example.restapi.survey.Question;
import com.example.restapi.survey.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.List;

@RestController
public class SurveyController {
    SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/surveys")
    public List<Survey> getSurveys() {
        return surveyService.findAll();
    }

    @GetMapping("/surveys/{id}")
    public ResponseEntity<Survey> getSurveys(@PathVariable String id) {
        Survey survey = surveyService.findById(id);
        if (survey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(survey);
    }

    @GetMapping("/surveys/{surveyId}/questions")
    public List<Question> getQuestions(@PathVariable String surveyId) {
        List<Question> questions = surveyService.findSurveyQuestions(surveyId);
        if (questions == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return questions;
    }

    @GetMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question getSingleQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        Question question = surveyService.getSingleQuestion(surveyId, questionId);

        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return question;
    }

    @PostMapping("/surveys/{surveyId}/questions")
    public ResponseEntity<Question> createSurveyQuestion(
            @PathVariable String surveyId,
            @RequestBody Question question
    ) {
        Question questionCreated = surveyService.createSurveryQuestion(surveyId, question);
        String questionId = questionCreated.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{questionId}")
                .buildAndExpand(questionId)
                .toUri();

        return ResponseEntity.created(location).body(questionCreated);
    }

    @DeleteMapping("/surveys/{surveyId}/questions/{questionId}")
    public ResponseEntity<Question> deleteSurveyQuestion(
            @PathVariable String surveyId,
            @PathVariable String questionId
    ) {
        String questionIdRemoved = surveyService.deleteSurveyQuestion(surveyId, questionId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/surveys/{surveyId}/questions/{questionId}")
    public ResponseEntity<Question> deleteSurveyQuestion(
            @PathVariable String surveyId,
            @PathVariable String questionId,
            @RequestBody Question question
    ) {
        surveyService.updateSurveyQuestion(surveyId, questionId, question);

        return ResponseEntity.noContent().build();
    }
}
