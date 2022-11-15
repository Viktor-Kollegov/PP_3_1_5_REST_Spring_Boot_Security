package ru.kata.spring.boot_security.demo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "first_name")
    @Size(min = 4, message = "Не меньше 4 символов")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 4, message = "Не меньше 4 символов")
    private String lastName;
    private int age;
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Не должен быть пустым")
    @Email(message = "Не соответствует формату")
    private String email;
    @Column(nullable = false)
    @NotEmpty(message = "Не должен быть пустым")
    private String password;
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id") ,
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles;
    @NotNull(message = "Нужно выбрать роль")
    private Long singleRoleId;

    public String getRolesAsString() {
        StringBuilder rolesString = new StringBuilder();
        for (Role r : roles) {
            rolesString.append(r.getName().substring(5))
                    .append(" ");
        }
        return rolesString.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
