package domain.models;

import domain.enums.UserRole;
import org.springframework.util.DigestUtils;

import javax.persistence.*;

@Entity
@Table(name = "Admins")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private int id;

    @Column(name = "Username")
    private String username;

    @Column(name = "HashedPassword")
    private String hashedPassword;

    @Column(name = "UserRole")
    private UserRole userRole;

    public AuthUser(){}

    public AuthUser(String username, String password){
        this.username = username;
        this.hashedPassword = hashPass(password);
        this.userRole=UserRole.ADMIN;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;

        if(!obj.getClass().equals(this.getClass()))
            return false;
        //check class type and hash
        return obj.hashCode() == this.hashCode();
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    public boolean comparePassword(String password){
        String hashPass = hashPass(password);
        return this.hashedPassword.equals(hashPass);
    }

    public UserRole getUserRole() {
        return userRole;
    }

    private String hashPass(String pass){
        return DigestUtils.md5DigestAsHex(pass.getBytes());
    }
}
