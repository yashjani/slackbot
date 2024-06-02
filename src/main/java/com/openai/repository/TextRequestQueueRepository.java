package com.openai.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.openai.entity.TextRequestQueue;

public interface TextRequestQueueRepository extends JpaRepository<TextRequestQueue, Long> {

	@Query("select count(*) from TextRequestQueue a where a.updatedDate > :updatedDate and a.userId = :userId")
	int findAllWithCreationDateTimeBefore(@Param("updatedDate") Date updatedDate, @Param("userId") String userId);
		
	List<TextRequestQueue> findByStatusAndRetryCountGreaterThan(String status, int retryCount);

}
