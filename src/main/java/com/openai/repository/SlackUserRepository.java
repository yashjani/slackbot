package com.openai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openai.entity.SlackUser;

public interface SlackUserRepository extends JpaRepository<SlackUser, Long> {

	SlackUser findByTeamIdAndUserId(String teamId, String userId);

	List<SlackUser> findByTeamIdAndSubscriptionId(String teamId, String subscriptionId);
	
	List<SlackUser> findByUserIdIn(List<String> userIds);

	List<SlackUser> findByTeamId(String teamId);

}
