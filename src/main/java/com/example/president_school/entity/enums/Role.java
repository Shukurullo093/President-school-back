package com.example.president_school.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.example.president_school.entity.enums.RolesPermissions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    TEACHER(
            Set.of(
                    TEACHER_READ,
                    TEACHER_CREATE,
                    TEACHER_UPDATE,
                    TEACHER_DELETE
            )
    ),
    ASSISTANT(
            Set.of(
                    ASSISTANT_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    STUDENT(
            Set.of(
                    STUDENT_READ,
                    STUDENT_CREATE,
                    STUDENT_UPDATE,
                    STUDENT_DELETE
            )
    );

    @Getter
    private final Set<RolesPermissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Admin";
            case TEACHER -> "O'qituvchi";
            case ASSISTANT -> "Assistant";
            case STUDENT -> "O'quvchi";
        };
    }
}
