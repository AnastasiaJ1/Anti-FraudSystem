package antifraud.services;

import antifraud.models.Card;
import antifraud.models.responses.CardAddResponse;
import antifraud.models.dto.CardDTO;
import antifraud.models.responses.CardDeleteResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface CardService {
    HttpStatus addCardStatus(CardDTO cardDTO);

    public CardAddResponse addCard(CardDTO cardDTO);
    public CardDeleteResponse deleteCard(String number);
    public List<Card> getListCard();

    boolean checkFormat(String card);

    HttpStatus deleteCardStatus(String number);

    boolean inBlackList(String number);
}
