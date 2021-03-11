package com.mobcoder.exam.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobcoder.exam.user.ProfileDto;
import com.mobcoder.exam.user.User;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -7837906303510648555L;
    public Object responseData;
    public Integer status;
    public long time;
    public ErrorResponse error;

    public BaseResponse(Object responseData) {
        super();
        this.responseData = responseData;
        this.status = 1;
        this.time = new Date().getTime();
    }

    public BaseResponse(ErrorResponse error) {
        super();
        this.error = error;
        this.status = 0;
        this.time = new Date().getTime();
    }

    public static ProfileDto getProfileDtoData(User userData) {
        ProfileDto userProfile = new ProfileDto();
        userProfile.userId = userData.id;
        userProfile.isAdmin = userData.isAdmin;
        userProfile.email = userData.username;
        return userProfile;
    }
}