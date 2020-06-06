package controllers.DTO;

import domain.enums.UserRole;
import domain.enums.UserStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserDTO {

    public final int id;

    public String firstName;

    public String lastName;

    public String username;

    public UserStatus status;

    public UserRole role;

    public List<MultipartFile> mpf;

    public UserDTO(int id) {
        this.id = id;
    }

}
