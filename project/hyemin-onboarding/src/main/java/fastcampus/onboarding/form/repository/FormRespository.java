package fastcampus.onboarding.form.repository;

import fastcampus.onboarding.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRespository extends JpaRepository<Form, Integer> {

}
