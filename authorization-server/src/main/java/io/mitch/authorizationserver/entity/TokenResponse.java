package io.mitch.authorizationserver.entity;

import org.codehaus.jackson.annotate.JsonProperty;

public class TokenResponse {
    @JsonProperty
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
