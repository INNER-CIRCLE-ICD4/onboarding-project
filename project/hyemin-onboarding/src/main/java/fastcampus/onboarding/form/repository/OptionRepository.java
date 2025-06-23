package fastcampus.onboarding.form.repository;

import fastcampus.onboarding.form.entity.Item;
import fastcampus.onboarding.form.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByOptionSeqInAndItem(List<Long> optionSeqs, Item item);
}
