package com.openai.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.openai.entity.UserRequestDetails;

public interface UserRequestDetailsRepository extends JpaRepository<UserRequestDetails, Long>  {
	
	@Query("select count(*) from UserRequestDetails a where a.requestDate > :requestDate and a.userId = :userId")
	int findAllWithRequestDateTimeBefore(@Param("requestDate") Date requestDate, @Param("userId") String userId);
}
