package antifraud.models.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonSerialize
@Data
public class TransactionResponse {
    private String result;

    private String info;

    public TransactionResponse(String result, boolean card, boolean ip, int diffRegion, int diffIp) {
        this.result = result;
        System.out.println(result);
        System.out.println(card);
        System.out.println(ip);
        System.out.println(diffIp);
        System.out.println(diffRegion);
        if(!result.equals("ALLOWED") || card || ip || diffRegion >= 3 || diffIp >= 3){

            if(card || ip) this.result = "PROHIBITED";
            List<String> reasons = new ArrayList<>();
            if((result.equals("MANUAL_PROCESSING")
                    && !(card || ip)) || (result.equals("PROHIBITED"))) reasons.add("amount");
            if(card) reasons.add("card-number");
            if(ip) reasons.add("ip");
            if(diffIp >= 3) {
                reasons.add("ip-correlation");
                if(diffIp == 3 && result.equals("ALLOWED")){
                    this.result = "MANUAL_PROCESSING";
                } else if (diffIp > 3) {
                    this.result = "PROHIBITED";
                }
            }
            if(diffRegion >= 3) {
                reasons.add("region-correlation");
                if(diffRegion == 3 && result.equals("ALLOWED")){
                    this.result = "MANUAL_PROCESSING";
                } else if (diffRegion > 3) {
                    this.result = "PROHIBITED";
                }
            }
            this.info = reasons.stream().collect(Collectors.joining(", "));
        } else{
            this.info = "none";
        }

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
