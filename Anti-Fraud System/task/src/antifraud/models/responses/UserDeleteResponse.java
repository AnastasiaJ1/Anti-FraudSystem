package antifraud.models.responses;

import antifraud.models.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@ToString
@EqualsAndHashCode
@JsonSerialize
public class UserDeleteResponse {
    String username;
    String status;

    public UserDeleteResponse(User user, String status){
        this.username = user.getUsername();
        this.status = status;
    }

}
