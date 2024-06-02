package com.openai.slack.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openai.entity.SlackUser;
import com.openai.repository.SlackUserRepository;
import com.openai.repository.SlackViewRepository;
import com.openai.repository.UserRequestDetailsRepository;
import com.openai.utils.Constants;
import com.openai.utils.TimeUtils;

@Service
public class SlackUserService {

	Logger logger = LoggerFactory.getLogger(SlackUserService.class);

	@Autowired
	SlackUserRepository slackUserRepository;

	@Autowired
    UserRequestDetailsRepository userRequestDetailsRepository;
	
	@Autowired
	SlackMessageServices messageServices;
	
	@Autowired
	SlackViewRepository slackViewRepository;
	
	@Autowired
	SlackOauthService slackOauthService;

	public SlackUser getSlackUser(String teamId, String userId) {
		logger.info("getSlackUser start: teamId {} userId {}", teamId, userId);
		SlackUser slackUser = slackUserRepository.findByTeamIdAndUserId(teamId, userId);
		if (slackUser == null) {
			logger.info("getSlackUser: creating user beacuse it does not exist");
			slackUser = new SlackUser();
			slackUser.setTeamId(teamId);
			slackUser.setUserId(userId);
			slackUser.setCreatedDate(new Date());
			slackUser.setUpdatedDate(new Date());
			slackUser.setPlan(Constants.FREE_PLAN);
			slackUser.setCount(100);
			slackUserRepository.save(slackUser);
		}
		logger.info("getSlackUser end: teamId {} userId {}", teamId, userId);
		return slackUser;
	}
	
	public boolean isMessagesTabFirstTime(SlackUser slackUser, String dmChannelId) {
		logger.info("getSlackUser start: dmChannelId{}", dmChannelId);
		boolean result = false;
		if(slackUser.getDmChannelId() == null) {
			slackUser.setDmChannelId(dmChannelId);
			slackUser = slackUserRepository.save(slackUser);
			result = true;
		}
		logger.info("getSlackUser start: result {}", result);
		return result;
	}

	public void updateUserAccess(List<String> userList, String teamId, String primeUserId) {
		logger.info("updateUserAccess start: userList {} teamId {} primeUserId {} ", userList.toString(), teamId,
				primeUserId);
		SlackUser primeUser = slackUserRepository.findByTeamIdAndUserId(teamId, primeUserId);
		List<SlackUser> existingUsers = slackUserRepository.findByTeamId(teamId);
		SlackUser slackUser = null;
		List<SlackUser> slackUserList = new ArrayList<>();
		Map<String, SlackUser> userMap = new HashMap<>();
		List<SlackUser> deleteUsers = new ArrayList<>();
		for (String userId : userList) {
			// If its primary dont touch
			if (primeUser.getUserId().equals(userId)) {
				continue;
			}
			slackUser = new SlackUser();
			slackUser.setUserId(userId);
			slackUser.setTeamId(teamId);
			slackUser.setValidTill(TimeUtils.addSevenDays());
			slackUser.setSubscriptionId(primeUser.getSubscriptionId());
			slackUser.setPrimary(false);
			slackUser.setCreatedDate(new Date());
			slackUser.setUpdatedDate(new Date());
			userMap.put(userId, slackUser);
			slackUserList.add(slackUser);
		}
		for (SlackUser existingUser : existingUsers) {
			if (!userMap.containsKey(existingUser.getUserId()) && !existingUser.isPrimary()) {
				deleteUsers.add(existingUser);
			}
		}
		slackUserRepository.deleteAll(deleteUsers);
		slackUserRepository.saveAll(slackUserList);
		logger.info("updateUserAccess end");
	}

	public List<SlackUser> getSubscribedUsersInTeam(String teamId, String userId) {
		logger.info("getSubscribedUsersInTeam start: teamId {} userId {}", teamId, userId);
		SlackUser slackUser = slackUserRepository.findByTeamIdAndUserId(teamId, userId);
		logger.info("getSubscribedUsersInTeam completed");
		return slackUserRepository.findByTeamIdAndSubscriptionId(teamId, slackUser.getSubscriptionId());
	}

	public Boolean updateSubscriptionDetails(String teamId, String userId, String subscriptionId, String plan) {
		logger.info("updateSubscriptionDetails start: teamId {} userId {} subscriptionId {}", teamId, userId,
				subscriptionId);
		SlackUser slackUser = slackUserRepository.findByTeamIdAndUserId(teamId, userId);
		slackUser.setSubscriptionId(subscriptionId);
		slackUser.setUpdatedDate(new Date());
		slackUser.setPrimary(true);
		slackUser.setPlan(plan);
		if(Constants.BASIC_PLAN.equalsIgnoreCase(plan)) {
			slackUser.setCount(1000);
		}else if(Constants.PROFESSIONAL_PLAN.equalsIgnoreCase(plan)) {
			slackUser.setCount(3000);
		}
		slackUserRepository.save(slackUser);
		logger.info("updateSubscriptionDetais completed");
		return true;
	}


}
