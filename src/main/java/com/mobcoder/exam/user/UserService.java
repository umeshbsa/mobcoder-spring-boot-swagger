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


    public ResponseEntity<BaseResponse> deleteUser(String email) {
        User userDatao = userRepo.findByUsername(email);
        if (userDatao != null) {
            userRepo.delete(userDatao);
            Map<String, Object> usersListData = new HashMap<>();
            usersListData.put(Key.MESSAGE, Success.USER_DELETE);
            return Validation.getResponseValid(usersListData);
        } else {
            return Validation.getErrorValid(Errors.USER_NOT_EXIST, Code.USER_NOT_EXIST);
        }
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

}
