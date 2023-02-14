package antifraud.services.impl;

import antifraud.models.*;
import antifraud.models.dto.CardDTO;
import antifraud.models.responses.CardAddResponse;
import antifraud.models.responses.CardDeleteResponse;
import antifraud.repositories.CardCRUDRepository;
import antifraud.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardCRUDRepository repository;
    @Override
    public HttpStatus addCardStatus(CardDTO cardDTO){
        if(cardDTO.getNumber() == null || cardDTO.getNumber().equals("") || !checkFormat(cardDTO.getNumber())){
            return HttpStatus.BAD_REQUEST;
        }
        if(repository.findByNumber(cardDTO.getNumber()).isPresent()) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.OK;
    }
    @Override
    public CardAddResponse addCard(CardDTO cardDTO) {
        Card card = new Card(cardDTO.getNumber());
        repository.save(card);
        return new CardAddResponse(card);
    }

    @Override
    public CardDeleteResponse deleteCard(String number) {
        Card card = repository.findByNumber(number).get();
        repository.delete(card);
        return new CardDeleteResponse(card);
    }

    @Override
    public List<Card> getListCard() {
        return repository.findAll()
                .stream()
                .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkFormat(String card) {
        return card != null && card.matches("[0-9]+") && card.length() == 16
                && LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(card);
    }

    @Override
    public HttpStatus deleteCardStatus(String number) {
        if(number == null || number.equals("") || !checkFormat(number)){
            return HttpStatus.BAD_REQUEST;
        }
        if(!repository.findByNumber(number).isPresent()) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.OK;
    }

    @Override
    public boolean inBlackList(String number){
        return repository.findByNumber(number).isPresent();
    }
}
