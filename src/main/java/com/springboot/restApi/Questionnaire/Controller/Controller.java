package com.springboot.restApi.Questionnaire.Controller;

import com.springboot.restApi.Questionnaire.Survey.Question;
import com.springboot.restApi.Questionnaire.Survey.Survey;
import com.springboot.restApi.Questionnaire.SurveyService.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class Controller {

   private SurveyService surveyService;

    public Controller(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys (){
       return surveyService.retrieveAllSurveys();
    }

    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSurveyById (@PathVariable String surveyId){
             Survey survey =   surveyService.retrieveSurveyById(surveyId);
             if(survey == null){
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
             }
             return survey;
    }

    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllQuestionsFromSurveys(@PathVariable String surveyId){
        return   surveyService.retrieveAllQuestionsFromSurvey(surveyId);

    }

    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveQuestionsFromSurveysById (@PathVariable String surveyId,@PathVariable String questionId){
        Question question =   surveyService.retrieveSurveyQuestionsById(surveyId,questionId);
        if(question == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return question;
    }

    @RequestMapping(value="/surveys/{surveyId}/questions",method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId, @RequestBody Question question){
         String questionId =  surveyService.addNewSurveyQuestion(surveyId,question);
         URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionID}").buildAndExpand(questionId).toUri();
          return ResponseEntity.created(location).build();

    }

    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}",method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteQuestionsFromSurveysById (@PathVariable String surveyId, @PathVariable String questionId){
        surveyService.deleteSurveyQuestionsById(surveyId,questionId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}",method = RequestMethod.PUT)
    public ResponseEntity<Object> updateQuestionsFromSurveysById (@PathVariable String surveyId, @PathVariable String questionId, @RequestBody Question question){
        surveyService.updateSurveyQuestionsById(surveyId,questionId,question);
        return ResponseEntity.ok().build();
    }


}
