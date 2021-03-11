package com.mobcoder.exam.quiz;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Field;
import com.mobcoder.exam.question.Question;
import com.mobcoder.exam.user.ProfileDto;
import com.mobcoder.exam.user.User;
import com.mobcoder.exam.user.UserService;
import com.mobcoder.exam.utils.AppConstant;
import com.mobcoder.exam.utils.Validation;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(description = "admin start/end quiz", tags = {"Quiz"})
@RestController
public class QuizController {

    @Autowired
    private QuizService quizService;

    @ApiOperation(value = "only for admin - admin start/end quiz")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Quiz.class),
    })
    @RequestMapping(
            value = "/v1/quiz/startEnd",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> userSignUp(

            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token,

            @ApiParam(
                    name = "quizStatus",
                    type = "Boolean",
                    required = true)
            @RequestParam boolean quizStatus) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                return quizService.quizStartEnd(quizStatus);
            } else {
                return Validation.getErrorValid(Errors.ADMIN_QUESTION, Code.CODE_WRONG);
            }
        } catch (Exception e) {
            System.out.println("EEEEEE " + e.getMessage());
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

    @ApiOperation(value = "get quiz by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(value = "/v1/quiz/status",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> findQuestionById(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token) {
        try {
            return quizService.finsQuizStatus(1L);
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

}
