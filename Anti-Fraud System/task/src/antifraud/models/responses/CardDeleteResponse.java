package antifraud.models.responses;

import antifraud.models.Card;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CardDeleteResponse {
    String status;

    public CardDeleteResponse(Card card) {
        this.status = "Card " + card.getNumber() + " successfully removed!";
    }
}
