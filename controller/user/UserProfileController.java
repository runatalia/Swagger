package com.example.userservice.controller.user;


import com.example.userservice.dto.request.UserProfileRequestDto;
import com.example.userservice.dto.response.FullUserResponseDto;
import com.example.userservice.dto.response.PageDto;
import com.example.userservice.dto.response.UserProfileResponseDto;
import com.example.userservice.dto.response.UserResponseForFrontDto;
import com.example.userservice.exception.ResponseExceptionHandler;
import com.example.userservice.service.UserProfileService;
import com.example.userservice.validation.ValidImage;
import com.nimbusds.jose.shaded.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RequiredArgsConstructor
@Slf4j
@RestController
@Validated
@Tag(name = "User Profile Controller.", description = "Work with User Profile.")
@RequestMapping("/users/profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Operation(summary = "Get user profile", description = "Return user profile by id from PostgreSQL", operationId = "get User Profile Service by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseExceptionHandler.class)))) ,
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FullUserResponseDto getUserProfileServiceById(@PathVariable Long id) {
        return userProfileService.getUser(id);
    }

    @Operation(summary = "Get all user profile", description = "Return all user profile from PostgreSQL and Keycloak")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDto<UserResponseForFrontDto> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {

        return  userProfileService.findAllProfileUsers(page, size);
    }

    @Operation(summary = "Create user profile", description = "Create only profile based keycloak." +
            "Swagger uses a json file for testing(file.json). In postman we do the usual json upload")  //upload two file: file.json and MultipartFile 
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void createProfile(@RequestPart  @Parameter(schema =@Schema(type = "string", format = "binary"))
                                  @Valid UserProfileRequestDto userProfileRequestDto,
                              @RequestPart(value = "file") MultipartFile file) {
        userProfileService.createUserProfile(userProfileRequestDto, file);
    }

    @Operation(summary = "Update user profile by id", description = "Update full profile in postgres by id and update email in keycloak by old email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PutMapping(value = "/{id}",consumes = {"multipart/form-data"}) //upload file and string: string convert to JSON and MultipartFile 
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponseDto updateUserById(@PathVariable Long id,
                                               @Parameter(name = "str", required = true, schema = @Schema(implementation = UserProfileRequestDto.class)) @RequestPart String str,
                                               @RequestParam(value = "file", required = true)   @ValidImage MultipartFile file) {
        Gson g = new Gson();
        UserProfileRequestDto dto = g.fromJson(str, UserProfileRequestDto.class);
        return userProfileService.saveOrUpdateUserProfile(id, dto, file);
    }

    @Operation(summary = "Save or update user profile by id", description = "Update or save full profile in postgres by id (without email).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was updated/saved",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping(value = "/{id}", consumes = { MediaType.MULTIPART_MIXED_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void saveOrUpdateProfile(@PathVariable @Valid Long id,
                                    @RequestPart @Valid UserProfileRequestDto userProfileRequestDto,
                                    @RequestPart("file") @ValidImage MultipartFile file) {
        userProfileService.saveOrUpdateUserProfile(id, userProfileRequestDto, file);
    }

    @Operation(summary = "Delete user profile by id", description = "Delete only profile in postgres by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was deleted",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
    }
}
