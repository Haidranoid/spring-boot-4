package com.example.restapi.service;

import com.example.restapi.survey.Question;
import com.example.restapi.survey.Survey;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class SurveyService {
    private static List<Survey> surveys = new ArrayList<>();

    static {
        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        surveys.add(survey);
    }

    public List<Survey> findAll() {
        return surveys;
    }

    public Survey findById(String id) {
        return surveys.stream().filter(survey -> survey.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Question> findSurveyQuestions(String surveyId) {
        Survey survey = this.findById(surveyId);

        if (survey == null)
            return null;

        return survey.getQuestions();
    }

    public Question getSingleQuestion(String surveyId, String questionId) {
        List<Question> questions = this.findSurveyQuestions(surveyId);
        if (questions == null)
            return null;

        Question question = questions.stream().filter(qs -> qs.getId().equals(questionId)).findFirst().orElse(null);

        return question;
    }

    public Question createSurveryQuestion(String surveyId, Question question) {
        List<Question> questions = this.findSurveyQuestions(surveyId);

        if (questions == null)
            return null;

        question.setId(generateRandomId());
        questions.add(question);

        return question;
    }

    public String deleteSurveyQuestion(String surveyId, String questionId) {
        List<Question> questions = this.findSurveyQuestions(surveyId);

        if (questions == null)
            return null;

        boolean removed = questions.removeIf(qs -> qs.getId().equals(questionId));

        if (!removed)
            return null;

        return questionId;
    }

    private String generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        String random = new BigInteger(32, secureRandom).toString();

        return random;
    }

    public void updateSurveyQuestion(String surveyId, String questionId, Question question) {
        List<Question> questions = findSurveyQuestions(surveyId);
        questions.removeIf(q -> q.getId().equalsIgnoreCase(questionId));
        questions.add(question);
    }
}
