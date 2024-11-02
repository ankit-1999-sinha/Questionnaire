package com.springboot.restApi.Questionnaire.Survey;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {


    // text blocks
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

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String specificQuestionUrl = "/surveys/Survey1/questions/Question1";
    private static String genericQuestionUrl = "/surveys/Survey1/questions";
    @Test
    void retrieveSpecificSurveyQuestion_Test() throws JSONException {
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(specificQuestionUrl, String.class);
        System.out.println(responseEntity.getHeaders());
        System.out.println(responseEntity.getBody());
        String expectedResponse = """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(expectedResponse.trim(), responseEntity.getBody());
        JSONAssert.assertEquals(expectedResponse,responseEntity.getBody(),true);
    }



    @Test
    void addNewSurveyQuestion_basicScenario(){
        String requestBody = """
            {
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");

        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody,httpHeaders);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(genericQuestionUrl, HttpMethod.POST,httpEntity,String.class);
        System.out.println(testRestTemplate);
        System.out.println(responseEntity.getHeaders());
        System.out.println(responseEntity.getBody());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);

        // to delete
        testRestTemplate.delete(locationHeader);
    }
}
