package antifraud.services;

import antifraud.models.dto.FeedbackDTO;
import antifraud.models.Transaction;
import antifraud.models.dto.TransactionDTO;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface TransactionHandler {
String handleTransaction(TransactionDTO transactionDTO);


    void save(Transaction transaction);

    int checkRegionRestriction(Transaction transaction);

    int checkIpRestriction(Transaction transaction);

    HttpStatus getFeedbackStatus(FeedbackDTO feedbackDTO);

    Transaction putFeedback(FeedbackDTO feedbackDTO);

    List<Transaction> getHistory();

    HttpStatus checkHistoryNumber(String number);

    List<Transaction> getHistoryNumber(String number);
}
