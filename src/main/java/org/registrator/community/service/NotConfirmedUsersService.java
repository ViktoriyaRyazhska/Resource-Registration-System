package org.registrator.community.service;

import java.util.List;

import org.registrator.community.dto.json.UsersDataNotConfJson;
import org.registrator.community.entity.User;

public interface NotConfirmedUsersService {

	public void sendConfirmEmailFirstTime(String userEmail, String baseLink);

	public Boolean confirmEmail(String token);

	public String actionsWithNotConfirmedUsers(UsersDataNotConfJson usersDataNotConfJson);
	
	public String sendConfirmEmailAgain(List<User> userList);

    String deleteNotConfirmedUsers(List<User> userList);


}
