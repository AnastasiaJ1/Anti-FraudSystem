package antifraud.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
@ToString
@EqualsAndHashCode
public class SuspiciousIp {
    private static Long count = 1L;
    @Id
    Long id;
    String ip;

    public SuspiciousIp() {
    }

    public SuspiciousIp(String ip) {
        this.ip = ip;
        this.id = count;
        count++;
    }
}
