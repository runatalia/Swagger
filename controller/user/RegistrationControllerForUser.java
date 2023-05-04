package com.example.userservice.controller.user;

import com.example.userservice.dto.request.UserRegForUserDto;
import com.example.userservice.exception.ResponseExceptionHandler;
import com.example.userservice.service.UserCredentialsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/user/register")
public class RegistrationControllerForUser {
    private final UserCredentialsService userCredentialsService;

    @Operation(summary = "Register user.", description = "Register user in keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid UserRegForUserDto dto) {
        userCredentialsService.register(dto);
    }

}
