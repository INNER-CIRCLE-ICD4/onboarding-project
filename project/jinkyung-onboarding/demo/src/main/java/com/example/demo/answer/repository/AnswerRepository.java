package com.example.demo.answer.repository;

import com.example.demo.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 추가적인 메소드가 필요하다면 여기에 정의할 수 있습니다.
    // 예: List<Answer> findByName(String name);
}
