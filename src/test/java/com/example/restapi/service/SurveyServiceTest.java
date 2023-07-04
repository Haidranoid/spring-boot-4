package com.example.restapi.service;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTIONS_URL = "/surveys/Survey1/questions";
    String str = """
            {
                "id": "Question1",
                "description": "Most Popular Cloud Platform Today",
                "options": [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                ],
                "correctAnswer": "AWS"
            }
            """;

    @Test
    void retrieveSpecificSurveyQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(SPECIFIC_QUESTION_URL, String.class);
        String expectedResponse =
                """
                        {
                        "id":"Question1",
                        "description":"Most Popular Cloud Platform Today",
                        "correctAnswer":"AWS"
                        }
                        """;
        // Status of response is it 200
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
        //assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void retrieveAllSurveyQuestion_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GENERIC_QUESTIONS_URL, String.class);
        String expectedResponse =
                """
                        [
                        {"id":"Question1"},
                        {"id":"Question2"},
                        {"id":"Question3"}]
                        """;
        // Status of response is it 200
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
        //assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void addNewSurveyQuestion_basicScenario() {
        String requestBody = """
                {
                 "description": "Your favorite language",
                 "options": [
                    "Java",
                    "Python",
                    "Javascript",
                    "Haskell"
                 ],
                 "correctAnswer":"Java"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(GENERIC_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);

        // Status of response is it 200
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        // Delete
        restTemplate.delete(locationHeader);
    }
}
