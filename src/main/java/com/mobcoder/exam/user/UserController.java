package com.mobcoder.exam.user;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Field;
import com.mobcoder.exam.utils.Validation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "create user profile")
    @RequestMapping(
            value = "/v1/user/create",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse> userSignUp(

            @ApiParam(
                    name = "email",
                    type = "String",
                    required = true)
            @RequestParam String email,

            @ApiParam(
                    name = "password",
                    type = "String",
                    required = true)
            @RequestParam String password) {

        try {
            if (email == null || email.length() == 0) {
                return Validation.getFieldValid(Field.FIELD_EMAIL);
            }  else if (password == null || password.length() == 0) {
                return Validation.getFieldValid(Field.FIELD_PASSWORD);
            } else {

                int passwordLength = password.length();
                if (passwordLength >= 6 && passwordLength <= 10) {
                    User user = new User();
                    user.username = email;
                    return userService.userSignUp(user, password);
                } else {
                    Validation.getErrorValid(Errors.PASSWORD_6_10, Code.PASSWORD_6_10);
                }
            }
        } catch (Exception e) {

            System.out.println("EEEEEE "+e.getMessage());
        }
        return Validation.getErrorValid(Errors.ERROR_WRONG, Code.CODE_WRONG);
    }

}
