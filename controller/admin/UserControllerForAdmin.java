package com.example.userservice.controller.admin;

import com.example.userservice.dto.request.UserRequestForUserDto;
import com.example.userservice.dto.response.PageDto;
import com.example.userservice.dto.response.UserCredentialsInfoForAdminDto;
import com.example.userservice.exception.ResponseExceptionHandler;
import com.example.userservice.mapper.UserCredentialsMapper;
import com.example.userservice.service.UserCredentialsService;
import com.example.userservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserControllerForAdmin {
    private final UserCredentialsService userCredentialsService;
    private final UserCredentialsMapper userCredentialsMapper;
    private final UserProfileService userProfileService;

    @Operation(summary = "Find all user.", description = "Return all user from Keycloak")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDto<UserCredentialsInfoForAdminDto> findAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {

        Page<UserCredentialsInfoForAdminDto> userPage = new PageImpl<>(
                userCredentialsMapper.mapToListUserInfoForAdmin(userCredentialsService.findAll(page, size)));

        int countAllUsers = userCredentialsService.countOfAllUsers();
        return PageDto.build(userPage, page, size, countAllUsers);
    }

    @Operation(summary = "Find all user by name.", description = "Return all user from Keycloak by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping(params = {"firstName", "lastName"})
    @ResponseStatus(HttpStatus.OK)
    public List<UserCredentialsInfoForAdminDto> findAllByName(@RequestParam @NotBlank String firstName,
                                                              @RequestParam @NotBlank String lastName,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        return userCredentialsMapper.mapToListUserInfoForAdmin(
                userCredentialsService.findAllByFirstNameAndLastName(firstName, lastName, page, size));
    }

    @Operation(summary = "Find all user by email.", description = "Return all user from Keycloak by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserCredentialsInfoForAdminDto findByEmail(@PathVariable @Email String email) {
        return userCredentialsMapper.mapToUserInfoForAdmin(userCredentialsService.findUserByEmail(email));
    }

    @Operation(summary = "Delete user.", description = "Delete user by email from Keycloak")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Member.class)))) ,
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Email String email) {
        userCredentialsService.deleteUser(email);
    }

    @Operation(summary = "Update FIO.", description = "Update FIO in Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Member.class)))) ,
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PatchMapping("/updateFio")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody UserRequestForUserDto userRequestForUserDto) {
        userCredentialsService.updateUser(userRequestForUserDto);
    }

    @Operation(summary = "Update email.", description = "Update email in Keycloak and postgreSQL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok, successful operation",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @PatchMapping("/{email}/updateEmail")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateEmail(@PathVariable String email, @RequestBody @Valid String newEmail) {
        userProfileService.updateEmail(email, newEmail);
    }
}
