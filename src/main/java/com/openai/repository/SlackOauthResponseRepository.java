package com.openai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openai.entity.SlackOauthModal;

public interface SlackOauthResponseRepository extends JpaRepository<SlackOauthModal, Long>{
	
	SlackOauthModal findByTeamIdAndUserId(String teamId, String userId);

	SlackOauthModal findByTeamId(String teamId);

}
