package antifraud.repositories;

import antifraud.models.*;
import antifraud.models.dto.AccessDTO;
import antifraud.models.dto.RoleDTO;
import antifraud.models.responses.StatusResponse;
import antifraud.models.responses.UserDeleteResponse;
import antifraud.models.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepository {
    @Autowired
    private UserCRUDRepository userCRUDRepository;

    public User findUserByUsername(String username) {
        if (userCRUDRepository.findByUsernameLowerCase(username.toLowerCase()).isEmpty()) return null;
        return userCRUDRepository.findByUsernameLowerCase(username.toLowerCase()).get();
    }

    public boolean save(User user) {
        if (!userCRUDRepository.findByUsernameLowerCase(user.getUsername().toLowerCase()).isEmpty()) return false;
        if (userCRUDRepository.count() == 0){
            user.setRole("ADMINISTRATOR");
            user.setAccess("UNLOCKED");
        }
        userCRUDRepository.save(user);
        return true;
    }

    public List<UserResponse> getListUsers(){
        List<UserResponse> list = userCRUDRepository.findAll()
                .stream()
                .sorted((e1, e2) -> (int) (e1.getId() - e2.getId()))
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return list;
    }

    public UserDeleteResponse deleteUser(String username){
        User user = findUserByUsername(username);
        if(user == null) return null;
        userCRUDRepository.delete(user);
        return new UserDeleteResponse(user, "Deleted successfully!");
    }

    public UserResponse putRole(RoleDTO role) {
        User user = userCRUDRepository.findByUsernameLowerCase(role.getUsername().toLowerCase()).get();
        user.setRole(role.getRole());
        userCRUDRepository.save(user);
        return new UserResponse(user);
    }

    public HttpStatus putRoleStatus(RoleDTO role) {
        Optional<User> user = userCRUDRepository.findByUsernameLowerCase(role.getUsername().toLowerCase());
        if(user.isEmpty()) return HttpStatus.NOT_FOUND;
        if(!role.getRole().equals("SUPPORT") && !role.getRole().equals("MERCHANT")) return HttpStatus.BAD_REQUEST;
        if(user.get().getRole().equals(role.getRole())) return HttpStatus.CONFLICT;
        return HttpStatus.OK;
    }

    public HttpStatus changeAccessStatus(AccessDTO access) {
        Optional<User> user = userCRUDRepository.findByUsernameLowerCase(access.getUsername().toLowerCase());
        if(user.isEmpty()) return HttpStatus.NOT_FOUND;
        System.out.println(user);
        if(user.get().getRole().equals("ADMINISTRATOR")) return HttpStatus.BAD_REQUEST;
        return HttpStatus.OK;
    }

    public StatusResponse changeAccess(AccessDTO access) {
        User user = userCRUDRepository.findByUsernameLowerCase(access.getUsername().toLowerCase()).get();
        System.out.println(user);
        user.setAccess(access.getOperation() + "ED");
        System.out.println(user);
        userCRUDRepository.save(user);
        return new StatusResponse(user);
    }


}
