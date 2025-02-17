package org.vedruna.frogger.persistance.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 60)
    private String password;
    
    @Column(name = "email", nullable = false, unique = true, length = 90)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Roles_rol_id", referencedColumnName = "rol_id")
    private Rol userRol;

    @ManyToMany
    @JoinTable(name = "user_follows_user", 
                joinColumns = @JoinColumn(name = "user_who_follows"), 
                inverseJoinColumns = @JoinColumn(name = "user_to_follow"))
    private List<User> usersIFollow;

    @ManyToMany(mappedBy="usersIFollow")
    private List<User> followers;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Score recordScore;

    // Agregar el método getter para recordScore
    public Score getRecordScore() {
        return recordScore;
    }

    // Métodos de UserDetails (necesarios para Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
