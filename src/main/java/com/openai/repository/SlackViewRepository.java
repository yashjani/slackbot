package com.openai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openai.entity.SlackView;

public interface SlackViewRepository extends JpaRepository<SlackView, String> {

}
