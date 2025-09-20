package com.tayatu.QuestionMicroservice.service;

import com.tayatu.QuestionMicroservice.model.Question;
import com.tayatu.QuestionMicroservice.model.QuestionWrapper;
import com.tayatu.QuestionMicroservice.model.ResonseObject;
import com.tayatu.QuestionMicroservice.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    public ResponseEntity<List<Question>> getAllQuestions() {

        try {
            return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(repo.findByCategory(category), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> addQuestion (Question question) {
        try {
            repo.save(question);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error creating", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQ) {
        List<Integer> questions = repo.findRandomQuestionsByCategory(categoryName, numQ);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestion(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();

        List<Question> questions = new ArrayList<>();

        for(Integer q_id: questionIds){
            questions.add(repo.findById(q_id).get());
        }

        for(Question question : questions){
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestionTitle(question.getQuestionTitle());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(question.getOption4());

            questionWrappers.add(questionWrapper);
        }

        return  new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateScore(List<ResonseObject> responses) {

        int score = 0;
        for (ResonseObject userResponse : responses) {
            Question correctQuestion = repo.findById(userResponse.getId()).get();

            if (userResponse.getResponse() != null &&
                    userResponse.getResponse().equalsIgnoreCase(correctQuestion.getRightAnswer())) {

                score++;
            }
        }
        return  new ResponseEntity<>(score, HttpStatus.OK);
    }
}
