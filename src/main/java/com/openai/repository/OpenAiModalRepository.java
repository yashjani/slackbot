package com.openai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openai.entity.OpenAiModal;

public interface OpenAiModalRepository extends JpaRepository<OpenAiModal, String>{

}
