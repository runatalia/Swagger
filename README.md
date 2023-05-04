# Swagger
add to pom:
<code>
     <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.0.2</version>
        </dependency>
</code>
    

- remove CORS from browser:
Login to command line as administrator
input:   cd C:\Program Files\Google\Chrome\Application
input:   chrome.exe  --disable-site-isolation-trials --disable-web-security --user-data-dir="PATH_TO_PROJECT_DIRECTORY"

Work with Swagger from Spring Boot(examples is in https://github.com/runatalia/Swagger/blob/main/controller/user/UserProfileController.java):

- MultipartFile file with JSON (get String and convert to JSON):

    <code> @Operation(summary = "Update user profile by id", description = "Update full profile in postgres by id and update email in keycloak by old email.")
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
    </code>
    
![image](https://user-images.githubusercontent.com/48579306/236304825-ffe6e5c9-3e9f-4ed4-a2aa-edd29fc49da9.png)



- MultipartFile file with JSON (get file.json):


 <code>@Operation(summary = "Create user profile", description = "Create only profile based keycloak." +
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
    </code>
    
   
![image](https://user-images.githubusercontent.com/48579306/236308506-6e53a66f-95c3-4654-9aff-16fc5b711388.png)



