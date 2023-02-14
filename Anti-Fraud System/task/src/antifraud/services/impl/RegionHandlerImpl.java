package antifraud.services.impl;

import antifraud.services.RegionHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class RegionHandlerImpl implements RegionHandler {
    Set<String> regions = Set.of("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA");
    public boolean checkRegion(String region){
        return region != null && regions.contains(region);
    }
}
