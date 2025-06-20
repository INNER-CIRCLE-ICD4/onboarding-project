package fastcampus.onboarding.form.repository;

import fastcampus.onboarding.form.entity.Form;
import fastcampus.onboarding.form.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.itemSeq IN :itemSeqs AND i.form = :form")
    List<Item> findByItemSeqInAndForm(@Param("itemSeqs") List<Integer> itemSeqs, @Param("form") Form form);

    List<Item> findByForm(Form form);
}
