package com.example.userservice.controller.user;


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
public class PasswordController {
    private final UserCredentialsService userCredentialsService;

    @Operation(summary = "Update password.", description = "Update password by email in keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PutMapping("/users/{email}/updatePassword")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@PathVariable String email,
                               @RequestBody @Valid String newPassword) {
        userCredentialsService.updatePassword(email, newPassword);
    }

    @Operation(summary = "Set temporary password.", description = "Set temporary password in keycloak as administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PutMapping("/admin/users/{email}/temporaryPassword")
    @ResponseStatus(HttpStatus.OK)
    public void setTemporaryPassword(@PathVariable String email,
                                     @RequestBody String temporaryPassword) {
        userCredentialsService.setTemporaryPassword(email, temporaryPassword);
    }

    @Operation(summary = "Send password to user email.", description = "Send password to user email for change old password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PutMapping("/users/{email}/forgotPassword")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@PathVariable String email) {
        userCredentialsService.forgotPassword(email);
    }
}
