package antifraud.models.responses;

import antifraud.models.SuspiciousIp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SuspiciousIpDeleteResponse {
    String status;

    public SuspiciousIpDeleteResponse(SuspiciousIp suspiciousIp) {
        this.status = "IP " + suspiciousIp.getIp() + " successfully removed!";
    }
}
