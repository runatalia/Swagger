package com.example.userservice.controller.user;


import com.example.userservice.dto.request.UserRequestForUserDto;
import com.example.userservice.service.UserCredentialsService;
import com.example.userservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "User controller.", description = "Work with full user.")
@RequestMapping("/users")
public class UserController {
    private final UserCredentialsService userCredentialsService;
    private final UserProfileService userProfileService;

    @Operation(summary = "Update and verify email.", description = "Update email in PostgreSQL and keyclok by old email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PatchMapping("/{email}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateEmail(@PathVariable String email, @RequestBody @Valid String newEmail) {
        userProfileService.updateEmail(email, newEmail);
        userCredentialsService.verifyEmail(newEmail);
    }

    @Operation(summary = "Update user.", description = "Update user in keyclok by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PatchMapping("/{email}/updateFio")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody @Valid UserRequestForUserDto userRequestForUserDto) {
        userCredentialsService.updateUser(userRequestForUserDto);
    }
}
