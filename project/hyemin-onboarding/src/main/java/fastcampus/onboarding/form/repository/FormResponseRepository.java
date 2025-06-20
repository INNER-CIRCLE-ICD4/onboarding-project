package fastcampus.onboarding.form.repository;

import fastcampus.onboarding.form.entity.Form;
import fastcampus.onboarding.form.entity.FormResponse;
import fastcampus.onboarding.form.entity.ItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponse, Long> {
    List<FormResponse> findByForm(Form form);
}
