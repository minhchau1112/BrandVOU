package com.example.accountservice.security;

import com.example.accountservice.model.common.CustomError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    // ObjectMapper is used to convert Java objects into JSON strings
    private static final ObjectMapper mapper = new ObjectMapper();
    // Register a module to handle Java 8 date and time types
    static {
        mapper.registerModule(new JavaTimeModule());
    }
    // This method is invoked whenever an unauthenticated user tries to access a secured resource.
    @Override
    public void commence(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
                         final AuthenticationException authenticationException) throws IOException {
        // Set the content type of the response to JSON
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Set the response status to 401 (Unauthorized)
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        // Create a custom error object with error details
        final CustomError customError = CustomError.builder()
                .header(CustomError.Header.AUTH_ERROR.getName())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();
        // Convert the custom error object to a JSON string
        final String responseBody = mapper
                .writer(DateFormat.getDateInstance())
                .writeValueAsString(customError);
        // Write the JSON string to the HTTP response output stream
        httpServletResponse.getOutputStream()
                .write(responseBody.getBytes());

    }

}
