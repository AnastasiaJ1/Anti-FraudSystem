package antifraud.models;

import antifraud.models.dto.UserDTO;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
@ToString
@EqualsAndHashCode
public class User {
    private static Integer count = 1;
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String usernameLowerCase;
    @Column
    private String role;
    @Column
    private String access;

    public User(UserDTO user){
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.id = count;
        count++;
        if (user.getUsername() != null) this.usernameLowerCase = user.getUsername().toLowerCase();
        this.access = "LOCKED";
        this.role = "MERCHANT";
        if(count == 1) this.role = "ADMINISTRATOR";
    }

    public User() {

    }

    public boolean userIsValid(){
        return name != null && !name.equals("")
                && username != null && !username.equals("")
                && password != null && !password.equals("");
    }



}
