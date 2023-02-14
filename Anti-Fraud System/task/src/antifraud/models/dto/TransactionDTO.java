package antifraud.models.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;

@JsonSerialize
@Data
public class TransactionDTO {
    private Long amount;
    private String ip;
    private String number;
    private String region;
    private String date;

    public Long getAmount() {
        return amount;
    }

    public TransactionDTO(Long amount, String ip, String number) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TransactionDTO(Long amount) {
        this.amount = amount;
    }
}
