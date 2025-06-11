package com.example.demo.item.Repository;

import com.example.demo.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemaQuestionRepository extends JpaRepository<Item.ItemQuestion, Long> {

}
