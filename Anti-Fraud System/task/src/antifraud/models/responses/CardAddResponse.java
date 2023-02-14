package antifraud.models.responses;

import antifraud.models.Card;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CardAddResponse {
    Long id;
    String number;

    public CardAddResponse(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
    }
}
