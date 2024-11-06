package pp.users.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String department;
    @Column
    private int salary;
    @Column(nullable = false)
    private byte age;
    @Column
    private String email;
    @Column(name = "enabled", nullable = false)
    private byte enabledByte;
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @EqualsAndHashCode.Exclude
    private Set<Role> authorities;

    public User() {
    }

    public User(Role role) {
        this.enabledByte = 1;
        this.authorities = new HashSet<>(List.of(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isAdmin() {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    public boolean isBoardMember() {
        return department.equals("board of directors");
    }

    @Override
    public String toString() { // StringBuilder внутри StringJoiner лучше ломбоковской конкатенации
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("name='" + name + "'")
                .add("lastName='" + lastName + "'")
                .add("department='" + department + "'")
                .add("salary=" + salary)
                .add("age=" + age)
                .add("email='" + email + "'")
                .add("enabled=" + enabledByte)
                .add("authorities=" + authorities)
                .toString();
    }

    @Override
    public boolean isEnabled() {
        return enabledByte == 1;
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
}
