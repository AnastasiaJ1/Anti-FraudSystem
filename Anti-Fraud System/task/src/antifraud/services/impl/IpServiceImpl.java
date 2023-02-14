package antifraud.services.impl;

import antifraud.models.*;
import antifraud.models.dto.SuspiciousIpDTO;
import antifraud.models.responses.SuspiciousIpAddResponse;
import antifraud.models.responses.SuspiciousIpDeleteResponse;
import antifraud.repositories.SuspiciousIpCRUDRepository;
import antifraud.services.IpService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IpServiceImpl implements IpService {
    private SuspiciousIpCRUDRepository repository;

    public IpServiceImpl(SuspiciousIpCRUDRepository repository) {
        this.repository = repository;
    }

    @Override
    public HttpStatus addIpStatus(SuspiciousIpDTO suspiciousIpDTO){
        if(suspiciousIpDTO.getIp() == null || suspiciousIpDTO.getIp().equals("") || !checkFormat(suspiciousIpDTO.getIp())){
            return HttpStatus.BAD_REQUEST;
        }
        if(repository.findByIp(suspiciousIpDTO.getIp()).isPresent()) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.OK;
    }
    @Override
    public SuspiciousIpAddResponse addIp(SuspiciousIpDTO suspiciousIpDTO) {
        SuspiciousIp ip = new SuspiciousIp(suspiciousIpDTO.getIp());
        repository.save(ip);
        return new SuspiciousIpAddResponse(ip);
    }

    @Override
    public SuspiciousIpDeleteResponse deleteIp(String ip) {
        SuspiciousIp suspiciousIp = repository.findByIp(ip).get();
        repository.delete(suspiciousIp);
        return new SuspiciousIpDeleteResponse(suspiciousIp);
    }

    @Override
    public List<SuspiciousIp> getListIp() {
        return repository.findAll()
                .stream()
                .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkFormat(String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip != null && ip.matches(PATTERN);
    }

    @Override
    public HttpStatus deleteIpStatus(String ip) {
        if(ip == null || ip.equals("") || !checkFormat(ip)){
            return HttpStatus.BAD_REQUEST;
        }
        if(!repository.findByIp(ip).isPresent()) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.OK;
    }
    @Override
    public boolean inBlackList(String ip){
        return repository.findByIp(ip).isPresent();
    }
}
