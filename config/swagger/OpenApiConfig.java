package com.example.userservice.config.swagger;


import com.example.userservice.config.KeycloakAdminConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition
public class OpenApiConfig {
    private static final String OAUTH_SCHEME_NAME = "oAuth";
    private static final String PROTOCOL_URL_FORMAT = "%s/realms/%s/protocol/openid-connect";

    @Bean
    public OpenAPI customOpenApi(KeycloakAdminConfig keycloakProperties) {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme(keycloakProperties)))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    private SecurityScheme createOAuthScheme(KeycloakAdminConfig properties) {
        OAuthFlows flows = createOAuthFlows(properties);

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows(KeycloakAdminConfig properties) {
        OAuthFlow flow = createAuthorizationCodeFlow(properties);

        return new OAuthFlows()
                .authorizationCode(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow(KeycloakAdminConfig properties) {
        var protocolUrl = String.format(PROTOCOL_URL_FORMAT, properties.getServerUrl(), properties.getRealm());

        return new OAuthFlow()
                .authorizationUrl(protocolUrl + "/auth")
                .tokenUrl(protocolUrl + "/token")
                .scopes(new Scopes().addString("openid", ""));
    }
}
