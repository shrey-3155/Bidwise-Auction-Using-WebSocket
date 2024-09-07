package com.online.auction.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.auction.model.Permission.ADMIN_CREATE;
import static com.online.auction.model.Permission.ADMIN_DELETE;
import static com.online.auction.model.Permission.ADMIN_READ;
import static com.online.auction.model.Permission.ADMIN_UPDATE;
import static com.online.auction.model.Permission.USER_CREATE;
import static com.online.auction.model.Permission.USER_DELETE;
import static com.online.auction.model.Permission.USER_READ;
import static com.online.auction.model.Permission.USER_UPDATE;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER(
            Set.of(
                    USER_READ,
                    USER_CREATE,
                    USER_UPDATE,
                    USER_DELETE
            )),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
