package antifraud.services;

import antifraud.models.SuspiciousIp;
import antifraud.models.responses.SuspiciousIpAddResponse;
import antifraud.models.dto.SuspiciousIpDTO;
import antifraud.models.responses.SuspiciousIpDeleteResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface IpService {
    HttpStatus addIpStatus(SuspiciousIpDTO suspiciousIpDTO);

    public SuspiciousIpAddResponse addIp(SuspiciousIpDTO suspiciousIpDTO);
    public SuspiciousIpDeleteResponse deleteIp(String ip);
    public List<SuspiciousIp> getListIp();

    public boolean checkFormat(String ip);

    HttpStatus deleteIpStatus(String ip);

    boolean inBlackList(String ip);
}
