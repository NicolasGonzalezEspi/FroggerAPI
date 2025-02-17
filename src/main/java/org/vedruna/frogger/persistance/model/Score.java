package org.vedruna.frogger.persistance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;  // Importar LocalTime
import com.fasterxml.jackson.annotation.JsonIgnore;




@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "record_scores")  // ✅ Se llama igual que en la base de datos
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "users_user_id", referencedColumnName = "user_id", unique = true)
    @JsonIgnore  // Evita referencia circular
    private User user;


    @Column(name = "record_scorescol")
    private LocalTime time;  // Cambiar de Integer a LocalTime
}
