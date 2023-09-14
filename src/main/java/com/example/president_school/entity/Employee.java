package com.example.president_school.entity;

import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private Date birthDate;

    private String gender;

    private String phone;

    private String password;

    private Date joiningDate;

    @Enumerated(value = EnumType.STRING)
    private Science science;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String grade;

    @OneToOne
    private PersonImage image;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public Employee(String email, String phone, String password, Role role, Date joiningDate) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.joiningDate = joiningDate;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return role.getAuthorities();
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return phone;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
