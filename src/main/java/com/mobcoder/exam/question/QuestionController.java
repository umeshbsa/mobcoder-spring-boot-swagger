package com.mobcoder.exam.question;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Field;
import com.mobcoder.exam.user.ProfileDto;
import com.mobcoder.exam.utils.AppConstant;
import com.mobcoder.exam.utils.Validation;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "create, update, delete and get question data", tags = {"Question"})
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @ApiOperation(value = "Create question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(
            value = "/v1/question/create",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> createQuestion(

            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token,

            @ApiParam(
                    value = "Your question.",
                    name = "question",
                    type = "String",
                    required = true)
            @RequestParam String question,

            @ApiParam(
                    value = "Your 1st answer.",
                    name = "answera",
                    type = "String",
                    required = true)
            @RequestParam String answera,

            @ApiParam(
                    value = "Your 2nd answer.",
                    name = "answerb",
                    type = "String",
                    required = true)
            @RequestParam String answerb,

            @ApiParam(
                    value = "Your 3rd answer.",
                    name = "answerc",
                    type = "String",
                    required = true)
            @RequestParam String answerc,

            @ApiParam(
                    value = "Your 4th answer.",
                    name = "answerd",
                    type = "String",
                    required = true)
            @RequestParam String answerd,


            @ApiParam(
                    value = "Your right answer. Answer can only a, b, c, d.",
                    name = "rightAnswer",
                    type = "String",
                    required = true)
            @RequestParam String rightAnswer) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                if (question == null || question.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_TITLE);
                } else if (answera == null || answera.length() == 0
                        || answerb == null || answerb.length() == 0
                        || answerc == null || answerc.length() == 0
                        || answerd == null || answerd.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_QUESTION);
                } /*else if (!rightAnswer.contains("abcd")) {
                    return Validation.getFieldValid(Field.FIELD_ANSWER);
                } */else {
                    Question user = new Question();
                    user.question = question;
                    user.answera = answera;
                    user.answerb = answerb;
                    user.answerc = answerc;
                    user.answerd = answerd;
                    user.finalAnswer = rightAnswer;
                    return questionService.createQuestion(user);
                }
            } else {
                return Validation.getErrorValid(Errors.ADMIN_QUESTION, Code.CODE_WRONG);
            }
        } catch (Exception e) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

    @ApiOperation(value = "get all question")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(
            value = "/v1/question/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> findAllQuestion(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return questionService.findAllQuestion(auth.getName());
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

    @ApiOperation(value = "get question by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(value = "/v1/question/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> findQuestionById(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token,

            @ApiParam(
                    name = "questionId",
                    type = "Integer",
                    required = true)
            @PathVariable(value = "questionId") Long questionId) {
        try {
            return questionService.findQuestionById(questionId);
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

    @ApiOperation(value = "Update question - Only for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(value = "/v1/question/update", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> updateQuestion(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token,

            @ApiParam(
                    value = "Your questionId.",
                    name = "questionId",
                    type = "Integer",
                    required = true)
            @RequestParam Long questionId,

            @ApiParam(
                    value = "Your question.",
                    name = "question",
                    type = "String",
                    required = true)
            @RequestParam String question,

            @ApiParam(
                    value = "Your 1st answer.",
                    name = "answera",
                    type = "String",
                    required = true)
            @RequestParam String answera,

            @ApiParam(
                    value = "Your 2nd answer.",
                    name = "answerb",
                    type = "String",
                    required = true)
            @RequestParam String answerb,

            @ApiParam(
                    value = "Your 3rd answer.",
                    name = "answerc",
                    type = "String",
                    required = true)
            @RequestParam String answerc,

            @ApiParam(
                    value = "Your 4th answer.",
                    name = "answerd",
                    type = "String",
                    required = true)
            @RequestParam String answerd,

            @ApiParam(
                    value = "Your right answer. Answer can only a, b, c, d.",
                    name = "rightAnswer",
                    type = "String",
                    required = true)
            @RequestParam String rightAnswer) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                if (question == null || question.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_TITLE);
                } else if (answera == null || answera.length() == 0
                        || answerb == null || answerb.length() == 0
                        || answerc == null || answerc.length() == 0
                        || answerd == null || answerd.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_QUESTION);
                }else if (!rightAnswer.contains("abcd")) {
                    return Validation.getFieldValid(Field.FIELD_ANSWER);
                } else {
                    return questionService.editQuestion(questionId, question, answera, answerb, answerc,
                            answerd, rightAnswer);
                }
            } else {
                return Validation.getErrorValid(Errors.ADMIN_QUESTION, Code.CODE_WRONG);
            }
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

    @ApiOperation(value = "Delete question - Only for admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Question.class),
    })
    @RequestMapping(value = "/v1/question/delete",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse> deleteQuestion(
            @ApiParam(
                    name = "access_token",
                    type = "String",
                    required = true)
            @RequestParam String access_token,

            @ApiParam(
                    name = "questionId",
                    type = "Integer",
                    required = true)
            @PathVariable(value = "questionId") Long questionId
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                return questionService.deleteQuestion(questionId);
            } else {
                return Validation.getErrorValid(Errors.ADMIN_QUESTION, Code.CODE_WRONG);
            }
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }
}
