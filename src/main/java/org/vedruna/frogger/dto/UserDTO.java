package org.vedruna.frogger.dto;

import org.vedruna.frogger.persistance.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String rolName;
    private int followingCount;  // Número de personas a las que sigue
    private int followersCount;  // Número de seguidores
    private String bestScore;   // Su tiempo récord

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.rolName = user.getUserRol().getRolName();
        this.followingCount = user.getUsersIFollow().size();  // Calculamos cuántos sigue
        this.followersCount = user.getFollowers().size();  // Calculamos seguidores

        if (user.getRecordScore() != null && user.getRecordScore().getTime() != null) {
            this.bestScore = user.getRecordScore().getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } else {
            this.bestScore = null;
        }
    }
}
