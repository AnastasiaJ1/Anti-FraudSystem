package antifraud.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class RoleDTO {
    private String username;
    private String role;

}
