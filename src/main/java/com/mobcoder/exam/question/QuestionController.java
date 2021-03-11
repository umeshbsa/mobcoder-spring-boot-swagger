package com.mobcoder.exam.question;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Field;
import com.mobcoder.exam.utils.AppConstant;
import com.mobcoder.exam.utils.Validation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@ApiOperation(value = "Only for Admin")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @ApiOperation(value = "Create question - Only for admin")
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
                    name = "question",
                    type = "String",
                    required = true)
            @RequestParam String question,

            @ApiParam(
                    name = "answer1",
                    type = "String",
                    required = true)
            @RequestParam String answer1,

            @ApiParam(
                    name = "answer2",
                    type = "String",
                    required = true)
            @RequestParam String answer2,

            @ApiParam(
                    name = "answer3",
                    type = "String",
                    required = true)
            @RequestParam String answer3,

            @ApiParam(
                    name = "answer4",
                    type = "String",
                    required = true)
            @RequestParam String answer4,


            @ApiParam(
                    name = "finalAnswer",
                    type = "Integer",
                    required = true)
            @RequestParam int finalAnswer) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                if (question == null || question.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_TITLE);
                } else if (answer1 == null || answer1.length() == 0
                        || answer2 == null || answer2.length() == 0
                        || answer3 == null || answer3.length() == 0
                        || answer4 == null || answer4.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_QUESTION);
                } else if (finalAnswer <= 0) {
                    return Validation.getFieldValid(Field.FIELD_ANSWER);
                } else {
                    Question user = new Question();
                    user.question = question;
                    user.answer1 = answer1;
                    user.answer2 = answer2;
                    user.answer3 = answer3;
                    user.answer4 = answer4;
                    user.finalAnswer = finalAnswer;
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
                    name = "questionId",
                    type = "Integer",
                    required = true)
            @RequestParam Long questionId,

            @ApiParam(
                    name = "question",
                    type = "String",
                    required = true)
            @RequestParam String question,

            @ApiParam(
                    name = "answer1",
                    type = "String",
                    required = true)
            @RequestParam String answer1,

            @ApiParam(
                    name = "answer2",
                    type = "String",
                    required = true)
            @RequestParam String answer2,

            @ApiParam(
                    name = "answer3",
                    type = "String",
                    required = true)
            @RequestParam String answer3,

            @ApiParam(
                    name = "answer4",
                    type = "String",
                    required = true)
            @RequestParam String answer4,


            @ApiParam(
                    name = "finalAnswer",
                    type = "Integer",
                    required = true)
            @RequestParam int finalAnswer) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && auth.getName().equals(AppConstant.ADMIN_EMAIL)) {
                if (question == null || question.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_TITLE);
                } else if (answer1 == null || answer1.length() == 0
                        || answer2 == null || answer2.length() == 0
                        || answer3 == null || answer3.length() == 0
                        || answer4 == null || answer4.length() == 0) {
                    return Validation.getFieldValid(Field.FIELD_QUESTION);
                } else if (finalAnswer <= 0) {
                    return Validation.getFieldValid(Field.FIELD_ANSWER);
                } else {
                    return questionService.editQuestion(questionId, question, answer1, answer2, answer3,
                            answer4, finalAnswer);
                }
            } else {
                return Validation.getErrorValid(Errors.ADMIN_QUESTION, Code.CODE_WRONG);
            }
        } catch (Exception ignored) {
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }


    @ApiOperation(value = "Delete question - Only for admin")
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
