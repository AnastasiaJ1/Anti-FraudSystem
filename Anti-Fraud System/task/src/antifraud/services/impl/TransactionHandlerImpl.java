package antifraud.services.impl;

import antifraud.models.dto.FeedbackDTO;
import antifraud.models.RestrictionValues;
import antifraud.models.Transaction;
import antifraud.models.dto.TransactionDTO;
import antifraud.repositories.RestrictionValuesRepository;
import antifraud.repositories.TransactionRepository;
import antifraud.services.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionHandlerImpl implements TransactionHandler {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestrictionValuesRepository valuesRepository;

    Set<String> responseValues = Set.of("ALLOWED", "MANUAL_PROCESSING", "PROHIBITED");

    private Long allowedValue = 200L;

    private Long manualValue = 1500L;

    @Override
    public String handleTransaction(TransactionDTO transactionDTO) {
        if(valuesRepository.findById("allowedValue").isEmpty()){
            valuesRepository.save(new RestrictionValues("allowedValue", allowedValue));
            valuesRepository.save(new RestrictionValues("manualValue", manualValue));
        }
        Long allowedValue = valuesRepository.findById("allowedValue").get().getValue();
        Long manualValue = valuesRepository.findById("manualValue").get().getValue();
        Long amount = transactionDTO.getAmount();
        if (amount == null || amount <= 0 || transactionDTO.getIp() == null || transactionDTO.getNumber() == null) return null;
        if (amount <= allowedValue) {
            return "ALLOWED";
        } else if (amount <= manualValue) {
            return "MANUAL_PROCESSING";
        } else {
            return "PROHIBITED";
        }
    }
    @Override
    public void save(Transaction transaction){
        repository.save(transaction);
    }
    @Override
    public int checkRegionRestriction(Transaction transaction){
        Date in = transaction.checkDateF();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault()).minusHours(1L);
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return repository.findByDateBetweenWithCardRegion(out, transaction.checkDateF(), transaction.getNumber());
    }
    @Override
    public int checkIpRestriction(Transaction transaction){
        Date in = transaction.checkDateF();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault()).minusHours(1L);
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return repository.findByDateBetweenWithCardIp(out, transaction.checkDateF(), transaction.getNumber());
    }

    @Override
    public HttpStatus getFeedbackStatus(FeedbackDTO feedbackDTO) {
        if(feedbackDTO.getFeedback() == null || feedbackDTO.getTransactionId() == null
                || !responseValues.contains(feedbackDTO.getFeedback())){
            return HttpStatus.BAD_REQUEST;
        }
        Optional<Transaction> transactionOptional = repository.findById(feedbackDTO.getTransactionId());
        if(transactionOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Transaction transaction = transactionOptional.get();
        if(!transaction.getFeedback().equals("")){
            return HttpStatus.CONFLICT;
        }
        if(transaction.getResult().equals(feedbackDTO.getFeedback())) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return HttpStatus.OK;
    }

    @Override
    public Transaction putFeedback(FeedbackDTO feedbackDTO) {
        Transaction transaction = repository.findById(feedbackDTO.getTransactionId()).get();
        String feedback = feedbackDTO.getFeedback();
        switch (feedback){
            case "ALLOWED":
                if(transaction.getResult().equals("MANUAL_PROCESSING")){
                    upAllowed(transaction.getAmount());
                } else if(transaction.getResult().equals("PROHIBITED")){
                    upAllowed(transaction.getAmount());
                    upManual(transaction.getAmount());
                }
                break;
            case "MANUAL_PROCESSING":
                if(transaction.getResult().equals("ALLOWED")){
                    downAllowed(transaction.getAmount());
                } else if(transaction.getResult().equals("PROHIBITED")){
                    upManual(transaction.getAmount());
                }
                break;
            case "PROHIBITED":
                if(transaction.getResult().equals("ALLOWED")){
                    downAllowed(transaction.getAmount());
                    downManual(transaction.getAmount());
                } else if(transaction.getResult().equals("MANUAL_PROCESSING")){
                    downManual(transaction.getAmount());
                }
                break;
        }
        transaction.setFeedback(feedbackDTO.getFeedback());
        repository.deleteById(transaction.getTransactionId());
        repository.save(transaction);
        return transaction;
    }

    private void downManual(Long amount) {
        Long manualValue = (long) Math.ceil(0.8 * (double)valuesRepository.findById("manualValue").get().getValue() - 0.2 * (double)amount);
        valuesRepository.save(new RestrictionValues("manualValue", manualValue));
    }

    private void downAllowed(Long amount) {
        Long allowedValue = (long) Math.ceil(0.8 * (double)valuesRepository.findById("allowedValue").get().getValue() - 0.2 * (double)amount);
        valuesRepository.save(new RestrictionValues("allowedValue", allowedValue));

    }

    private void upManual(Long amount) {
        Long manualValue = (long) Math.ceil(0.8 * (double)valuesRepository.findById("manualValue").get().getValue() + 0.2 * (double)amount);
        valuesRepository.save(new RestrictionValues("manualValue", manualValue));

    }

    private void upAllowed(Long amount) {
        Long allowedValue = (long) Math.ceil(0.8 * (double)valuesRepository.findById("allowedValue").get().getValue() + (double)0.2 * amount);
        valuesRepository.save(new RestrictionValues("allowedValue", allowedValue));

    }

    public void printAll() {
        repository.findAll().forEach(System.out::println);
    }
    @Override
    public List<Transaction> getHistory() {
        return repository.findAll()
                .stream()
                .sorted((o1, o2) -> o1.getTransactionId().compareTo(o2.getTransactionId()))
                .collect(Collectors.toList());
    }
    @Override
    public HttpStatus checkHistoryNumber(String number){
        if(repository.findByNumber(number).isEmpty()){
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.OK;
    }

    @Override
    public List<Transaction> getHistoryNumber(String number){
        return repository.findByNumber(number);
    }
}
