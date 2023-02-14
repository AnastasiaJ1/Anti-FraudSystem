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
public class Card {
    private static Long count = 1L;
    @Id
    @Column
    Long id;
    @Column
    String number;

    public Card(String number) {
        this.number = number;
        this.id = count;
        count++;
    }

    public Card() {

    }
}
