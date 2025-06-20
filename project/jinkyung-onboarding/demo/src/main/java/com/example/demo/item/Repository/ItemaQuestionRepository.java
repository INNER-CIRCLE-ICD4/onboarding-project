package com.example.demo.item.Repository;

import com.example.demo.item.Item;
import com.example.demo.itemQuestion.ItemQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemaQuestionRepository extends JpaRepository<ItemQuestion, Long> {

}
