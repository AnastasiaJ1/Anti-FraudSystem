package antifraud.models.responses;

import antifraud.models.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@JsonSerialize
public class StatusResponse {
    private String status;

    public StatusResponse(User user){
        this.status = "User " + user.getUsername();
        if (user.getAccess().equals("LOCKED")) {
            this.status += " locked!";
        } else {
            this.status += " unlocked!";
        }
    }
}
