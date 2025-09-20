package com.tayatu.QuestionMicroservice.controller;

import com.tayatu.QuestionMicroservice.model.Question;
import com.tayatu.QuestionMicroservice.model.QuestionWrapper;
import com.tayatu.QuestionMicroservice.model.ResonseObject;
import com.tayatu.QuestionMicroservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    Environment environment;

    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable("category") String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
       return questionService.addQuestion(question);
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Integer>> generateQuiz(@RequestParam String category, @RequestParam Integer numQ){
        return questionService.getQuestionsForQuiz(category, numQ);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestion(@RequestBody List<Integer> questionIds){
        System.out.println(environment.getProperty("local.server.port"));
        return  questionService.getQuestion(questionIds);
    }

    @GetMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<ResonseObject> responses){
        return questionService.calculateScore(responses);
    }
}