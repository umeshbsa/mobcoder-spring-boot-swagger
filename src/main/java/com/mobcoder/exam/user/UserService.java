package com.mobcoder.exam.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.constant.Code;
import com.mobcoder.exam.constant.Errors;
import com.mobcoder.exam.constant.Key;
import com.mobcoder.exam.constant.Success;
import com.mobcoder.exam.utils.Validation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service(value = "userService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${oauth.server.enpoint}")
    private String tokenEndpoint;

    @Value("${oauth.scope}")
    private String scope;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("Data " + userName);
        User user = userRepo.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


    public ResponseEntity<BaseResponse> userSignUp(User user, String password) {
        Map<String, Object> usersListData = new HashMap<>();
        User userData = userRepo.findByUsername(user.getUsername());
        if (userData == null) {
            user.password = passwordEncoder.encode(password);
            userData = userRepo.save(user);
        }

        ProfileDto userProfile = BaseResponse.getProfileDtoData(userData);
        String accessToken = getAccessToken(userData.username, password);
        usersListData.put(Key.USER, userProfile);
        usersListData.put(Key.MESSAGE, Success.USER_DATA);
        usersListData.put(Key.ACCESS_TOKEN, accessToken);
        return Validation.getResponseValid(usersListData);
    }


    public ResponseEntity<BaseResponse> userSignIn(String email, String password) {
        User userData = userRepo.findByUsername(email);

        if (userData != null) {
            boolean isPasswordMatch = passwordEncoder.matches(password, userData.password);
            if (isPasswordMatch) {
                Map<String, Object> usersListData = new HashMap<>();
                ProfileDto userProfile = BaseResponse.getProfileDtoData(userData);
                String accessToken = getAccessToken(email, password);
                usersListData.put(Key.USER, userProfile);
                usersListData.put(Key.MESSAGE, Success.USER_DATA);
                usersListData.put(Key.ACCESS_TOKEN, accessToken);
                return Validation.getResponseValid(usersListData);
            } else {
                return Validation.getErrorValid(Errors.EMAIL_PASSWORD_WRONG, Code.EMAIL_PASSWORD_WRONG);
            }
        } else {
            return Validation.getErrorValid(Errors.USER_NOT_EXIST, Code.USER_NOT_EXIST);
        }

    }

    public void delete(long id) {
        userRepo.deleteById(id);
    }

    public String getAccessToken(String username, String password) {

        String uuid = UUID.randomUUID().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", getClientSecretBase64Encode());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username", username);
        params.add("password", password);
        params.add("scope", scope);
        params.add("grant_type", "password");
        params.add("uuid", uuid);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);
        String accessToken = "";

        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            ObjectMapper objectMapper = new ObjectMapper();
            TokenDto object = null;
            try {
                object = objectMapper.readValue(response.getBody(), TokenDto.class);
            } catch (IOException ignored) {

            }
            accessToken = Objects.requireNonNull(object).getAccessToken();
        }

        return accessToken;
    }

    public String getClientSecretBase64Encode() {
        String id_secret = clientId + ":" + clientSecret;
        byte[] encodedBytes = Base64.encodeBase64(id_secret.getBytes());
        return "Basic " + new String(encodedBytes);
    }

    public ResponseEntity<BaseResponse> forgotPassword(String email) {
        User userData = userRepo.findByUsername(email);
        if (userData != null) {
            Map<String, Object> usersListData = new HashMap<>();
            String newPassword = "654321";
            userData.password = passwordEncoder.encode(newPassword);
            userRepo.save(userData);
            usersListData.put(Key.MESSAGE, "Password change " + newPassword);
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.USER_NOT_EXIST, Code.USER_NOT_EXIST);
        }
    }

    public ResponseEntity<BaseResponse> changePassword(String email, String oldPassword, String newPassword) {

        if (oldPassword.equals(newPassword)) {
            return Validation.getErrorValid(Errors.OLD_NEW_MATCH, Code.CODE_OLD_NEW_PASSWORD_NOT_MATCH);
        } else {

            User dd = userRepo.findByUsername(email);
            if (dd != null) {
                boolean isPasswordMatch = passwordEncoder.matches(oldPassword, dd.password);
                if (isPasswordMatch) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dd.password = passwordEncoder.encode(newPassword);
                    userRepo.save(dd);
                    dataMap.put(Key.MESSAGE, "Password change successfully");
                    return Validation.getResponseValid(dataMap);
                } else {
                    return Validation.getErrorValid(Errors.OLD_PASSWORD_WRONG, Code.OLD_PASSWORD_WRONG);
                }
            } else {
                return Validation.getErrorValid(Errors.USER_NOT_EXIST, Code.USER_NOT_EXIST);
            }
        }
    }

    public ResponseEntity<BaseResponse> checkEmailExist(String email) {
        User dd = userRepo.findByUsername(email);
        Map<String, Object> dataMap = new HashMap<>();
        if (dd != null) {
            dataMap.put(Key.IS_EXIST, true);
            dataMap.put(Key.MESSAGE, "Already user exist.");
            return Validation.getResponseValid(dataMap);
        } else {
            dataMap.put(Key.IS_EXIST, false);
            dataMap.put(Key.MESSAGE, "User not exist ");
            return Validation.getResponseValid(dataMap);
        }
    }

    public ResponseEntity<BaseResponse> checkForAdminLogin(User user, String password) {
        Map<String, Object> usersListData = new HashMap<>();
        User userData = userRepo.findByUsername(user.getUsername());
        if (userData == null) {
            user.password = passwordEncoder.encode(password);
            user.isAdmin = true;
            userData = userRepo.save(user);
        }

        ProfileDto userProfile = BaseResponse.getProfileDtoData(userData);
        String accessToken = getAccessToken(userData.username, password);
        usersListData.put(Key.USER, userProfile);
        usersListData.put(Key.MESSAGE, Success.USER_DATA);
        usersListData.put(Key.ACCESS_TOKEN, accessToken);
        return Validation.getResponseValid(usersListData);
    }
}
