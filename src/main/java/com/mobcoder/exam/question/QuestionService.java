package com.mobcoder.exam.question;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Key;
import com.mobcoder.exam.constant.Success;
import com.mobcoder.exam.user.User;
import com.mobcoder.exam.user.UserRepo;
import com.mobcoder.exam.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "questionService")
public class QuestionService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuestionRepo questionRepo;

    public ResponseEntity<BaseResponse> createQuestion(Question question) {
        Map<String, Object> usersListData = new HashMap<>();
        Question saveData = questionRepo.save(question);
        usersListData.put(Key.QUESTION, saveData);
        usersListData.put(Key.MESSAGE, Success.QUESTION_CREATE);
        return Validation.getResponseValid(usersListData);
    }

    public ResponseEntity<BaseResponse> findAllQuestion(String email) {

        User user = userRepo.findByUsername(email);
        if (user != null) {
            Map<String, Object> usersListData = new HashMap<>();
            List<Question> questions = new ArrayList<>();
            Iterable<Question> data = questionRepo.findAll();
            data.forEach(questions::add);
            usersListData.put(Key.QUESTIONS, questions);
            usersListData.put(Key.MESSAGE, Success.QUESTION_ALL);
            usersListData.put(Key.COUNT, questions.size());
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.USER_NOT_EXIST, Code.USER_NOT_EXIST);
        }
    }

    public ResponseEntity<BaseResponse> findQuestionById(Long userId) {
        Optional<Question> question = questionRepo.findById(userId);
        if (question.isPresent()) {
            Map<String, Object> usersListData = new HashMap<>();
            Question userData = question.get();
            usersListData.put(Key.QUESTION, userData);
            usersListData.put(Key.MESSAGE, Success.QUESTION_ALL);
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.QUESTION_NOT_EXIST, Code.CODE_WRONG);
        }
    }

    public ResponseEntity<BaseResponse> editQuestion(
            Long questionId,
            String q,
            String a1,
            String a2,
            String a3,
            String a4,
            int finalAnswer) {

        Optional<Question> qq = questionRepo.findById(questionId);

        if (qq.isPresent()) {
            Question question = qq.get();

            Map<String, Object> usersListData = new HashMap<>();
            if (q != null && q.length() != 0) {
                question.question = q;
            }

            if (a1 != null && a1.length() != 0) {
                question.answer1 = a1;
            }

            if (a2 != null && a2.length() != 0) {
                question.answer2 = a2;
            }

            if (a3 != null && a3.length() != 0) {
                question.answer3 = a3;
            }

            if (a4 != null && a4.length() != 0) {
                question.answer4 = a4;
            }

            if (finalAnswer >= 0) {
                question.finalAnswer = finalAnswer;
            }

            question = questionRepo.save(question);
            usersListData.put(Key.QUESTION, question);
            usersListData.put(Key.MESSAGE, Success.QUESTION_UPDATE);
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.QUESTION_NOT_EXIST, Code.CODE_WRONG);
        }
    }

    public ResponseEntity<BaseResponse> deleteQuestion(Long questionId) {
        Optional<Question> question = questionRepo.findById(questionId);
        if (question.isPresent()) {
            questionRepo.delete(question.get());
            Map<String, Object> usersListData = new HashMap<>();
            usersListData.put(Key.QUESTION, question.get());
            usersListData.put(Key.MESSAGE, Success.QUESTION_DELETE);
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.QUESTION_NOT_EXIST, Code.CODE_WRONG);
        }
    }
}
