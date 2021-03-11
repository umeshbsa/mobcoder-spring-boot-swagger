package com.mobcoder.exam.quiz;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Key;
import com.mobcoder.exam.constant.Success;
import com.mobcoder.exam.question.Question;
import com.mobcoder.exam.user.ProfileDto;
import com.mobcoder.exam.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "quizService")
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    public ResponseEntity<BaseResponse> quizStartEnd(boolean quizStatusAPI) {
        Quiz q = null;
        Optional<Quiz> quizData = quizRepo.findById(1L);
        q = quizData.orElseGet(Quiz::new);
        q.quizStatus = quizStatusAPI;
        q.id = 1L;
        Map<String, Object> usersListData = new HashMap<>();
        q = quizRepo.save(q);
        usersListData.put(Key.QUIZ, q);
        if (quizStatusAPI) {
            usersListData.put(Key.MESSAGE, Success.QUIZ_START);
        } else {
            usersListData.put(Key.MESSAGE, Success.QUIZ_END);
        }
        return Validation.getResponseValid(usersListData);
    }

    public ResponseEntity<BaseResponse> finsQuizStatus(long l) {
        Map<String, Object> usersListData = new HashMap<>();
        Optional<Quiz> quizData = quizRepo.findById(l);
        if (quizData.isPresent()) {
            usersListData.put(Key.QUIZ, quizData.get());
            if (quizData.get().quizStatus) {
                usersListData.put(Key.MESSAGE, Success.QUIZ_START);
            } else {
                usersListData.put(Key.MESSAGE, Success.QUIZ_END);
            }
        }
        return Validation.getResponseValid(usersListData);
    }
}
