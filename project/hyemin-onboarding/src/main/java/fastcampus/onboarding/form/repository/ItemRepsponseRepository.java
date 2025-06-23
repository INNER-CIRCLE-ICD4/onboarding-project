package fastcampus.onboarding.form.repository;

import fastcampus.onboarding.form.entity.FormResponse;
import fastcampus.onboarding.form.entity.ItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepsponseRepository extends JpaRepository<ItemResponse, Long> {
}
