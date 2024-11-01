package com.springboot.restApi.Questionnaire.SurveyService;

import com.springboot.restApi.Questionnaire.Survey.Question;
import com.springboot.restApi.Questionnaire.Survey.Survey;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SurveyService {

    private static List<Survey> surveyList = new ArrayList<>();

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

        surveyList.add(survey);

    }

    public List<Survey> retrieveAllSurveys() {
        return surveyList;
    }

    public Survey retrieveSurveyById(String surveyId) {
        Predicate<? super Survey> predicate = survey -> survey.getId().equalsIgnoreCase(surveyId);
        Optional<Survey> optionalSurvey = surveyList.stream().filter(predicate).findFirst();
        if(optionalSurvey.isEmpty()) return null;
        return optionalSurvey.get();
    }

    public Question retrieveSurveyQuestionsById(String surveyId,String questionId) {
     List<Question> questions =   retrieveAllQuestionsFromSurvey(surveyId);
     Question ques = new Question();
     for(Question question : questions){
         if (question.getId().equalsIgnoreCase(questionId)){
             ques = question;
         }
     }
     return ques;

    }

    public List<Question> retrieveAllQuestionsFromSurvey(String surveyId) {

        Survey survey = retrieveSurveyById(surveyId);
        return survey.getQuestions();
    }

    public String addNewSurveyQuestion(String surveyId, Question question) {

        List<Question> questions = retrieveAllQuestionsFromSurvey(surveyId);
        SecureRandom secureRandom = new SecureRandom();
        String randomId = new BigInteger(32,secureRandom).toString();
        question.setId(randomId);
        questions.add(question);
        return question.getId();
    }

    public String deleteSurveyQuestionsById(String surveyId, String questionId) {
        List<Question> questions =   retrieveAllQuestionsFromSurvey(surveyId);
        if(questions == null) return null;

        Predicate<? super Question> predicate = q -> q.getId().equalsIgnoreCase(questionId);
        boolean removed = questions.removeIf(predicate);

        if(!removed) return null;
        return questionId;
    }

    public void updateSurveyQuestionsById(String surveyId, String questionId, Question question) {
        List<Question> questions = retrieveAllQuestionsFromSurvey(surveyId);
        questions.removeIf(q -> q.getId().equalsIgnoreCase(questionId));
        questions.add(question);
    }
}
