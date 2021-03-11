package com.mobcoder.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    private List<SecurityScheme> basicScheme() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new BasicAuth("basicAuth"));
        return schemeList;
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mobcoder.exam"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(basicScheme())
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(

                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("500 : Internal Server Error into Provider Import microservice")
                                        .responseModel(new ModelRef("Error"))
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(403)
                                        .message("API Request Forbidden!")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Request API Not Found!")
                                        .build()

                        ));
    }


    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")};
        return scopes;
    }

    public SecurityScheme securityScheme() {
        GrantType grantType = new ClientCredentialsGrant("http://localhost:8081/lpb/api/oauth/token");

        SecurityScheme oauth = new OAuthBuilder().name("basicAuth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Exam Poc REST API",
                "Exam Poc service APIs.",
                "API 2.0",
                "https://www.mycompany.co/privacy/",
                new Contact("Umesh Kumar", "https://www.mycompany.co/", "umesh.mobcoder@gmail.com"),
                "License of API", "https://www.mycompany.co/legal", Collections.emptyList());
    }
}
