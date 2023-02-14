package antifraud.models.responses;

import antifraud.models.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@ToString
@EqualsAndHashCode
@JsonSerialize
public class UserResponse {
    private Integer id;
    private String name;
    private String username;
    private String role;

    public UserResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
