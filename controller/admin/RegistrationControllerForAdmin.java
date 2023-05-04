package com.example.userservice.controller.admin;

import com.example.userservice.dto.request.UserRegForAdminDto;
import com.example.userservice.service.UserCredentialsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/register")
public class RegistrationControllerForAdmin {
    private final UserCredentialsService userCredentialsService;

    @Operation(summary = "Register User.", description = "Register User in keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid UserRegForAdminDto dto) {
        userCredentialsService.register(dto);
    }
}
