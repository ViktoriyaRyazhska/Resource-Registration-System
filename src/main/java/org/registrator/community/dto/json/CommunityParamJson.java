package org.registrator.community.dto.json;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class CommunityParamJson {
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9,]+$", message = "msg.batchops.wrongInput")
    private String login;
    @NotEmpty
    private String communityId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public CommunityParamJson() {
	}

	public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public CommunityParamJson(String login, String communityId) {
        this.login = login;
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return "CommunityParamJson [login=" + login + ", communityId=" + communityId + "]";
    }
}
