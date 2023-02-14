package antifraud.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class FeedbackDTO {
    private Long transactionId;
    private String feedback;
}
