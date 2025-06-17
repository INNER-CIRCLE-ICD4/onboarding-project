package survey.survey.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import survey.common.snowflake.Snowflake;

@MappedSuperclass
@Getter
public class BaseEntity {

    @Id
    private final Long id;

    private static final Snowflake snowflake = new Snowflake();

    public BaseEntity() {
        this.id = snowflake.nextId();
    }
}
