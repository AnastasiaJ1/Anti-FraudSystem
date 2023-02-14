package antifraud.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@JsonSerialize
@Entity
@ToString
@Table(name = "tr")
public class Transaction {
    private static Long count = 1L;
    @Id
    @Column
    private Long transactionId;
    @Column
    private Long amount;
    @Column
    private String ip;
    @Column
    private String number;
    @Column
    private String region;
    @Column
    private Date date;

    @Column
    private String result;

    @Column
    private String feedback;

    public String getResult() {
        return result;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Transaction() {

    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(date);
    }

    public Date checkDateF() {
        return date;
    }

    public Transaction(Long amount, String ip, String number, String region, Date date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.transactionId = count;
        this.result = "ALLOWED";
        count++;
        this.feedback = "";
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long id) {
        this.transactionId = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
