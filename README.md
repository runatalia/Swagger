# Swagger
- remove CORS from browser:
Login to command line as administrator
input:   cd C:\Program Files\Google\Chrome\Application
input:   chrome.exe  --disable-site-isolation-trials --disable-web-security --user-data-dir="PATH_TO_PROJECT_DIRECTORY"

Work with Swagger from Spring Boot:

- MultipartFile file with JSON (get String and convert to JSON):

    @Operation(summary = "Update user profile by id", description = "Update full profile in postgres by id and update email in keycloak by old email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PutMapping(value = "/{id}",consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponseDto updateUserById(@PathVariable Long id,
                                               @Parameter(name = "str", required = true, schema = @Schema(implementation = UserProfileRequestDto.class)) @RequestPart String str,
                                               @RequestParam(value = "file", required = true)   @ValidImage MultipartFile file) {
        Gson g = new Gson();
        UserProfileRequestDto dto = g.fromJson(str, UserProfileRequestDto.class);
        return userProfileService.saveOrUpdateUserProfile(id, dto, file);
    }
    
![image](https://user-images.githubusercontent.com/48579306/236304825-ffe6e5c9-3e9f-4ed4-a2aa-edd29fc49da9.png)



- MultipartFile file with JSON (get file.json):

 @Operation(summary = "Create user profile", description = "Create only profile based keycloak." +
            "Swagger uses a json file for testing(file.json). In postman we do the usual json upload")
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
    
    ![image](https://user-images.githubusercontent.com/48579306/236305736-4899fbec-f391-4632-ba11-aebc3af1da37.png)




