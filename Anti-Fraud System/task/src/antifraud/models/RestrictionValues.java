package antifraud.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
@ToString
@EqualsAndHashCode
public class RestrictionValues {
    @Id
    String id;
    @Column
    Long value;

    public RestrictionValues(String id, Long allowedValue) {
        this.id = id;
        this.value = allowedValue;
    }

    public RestrictionValues() {

    }
}
