package antifraud.models.responses;

import antifraud.models.SuspiciousIp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SuspiciousIpAddResponse {
    Long id;
    String ip;

    public SuspiciousIpAddResponse(SuspiciousIp suspiciousIp) {
        this.id = suspiciousIp.getId();
        this.ip = suspiciousIp.getIp();
    }
}
