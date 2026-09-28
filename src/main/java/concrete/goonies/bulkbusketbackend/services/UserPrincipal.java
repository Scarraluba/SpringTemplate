package concrete.goonies.bulkbusketbackend.services;

import concrete.goonies.bulkbusketbackend.domain.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

import static java.util.Arrays.stream;

/**
 * Project: SecureCapture
 * Created: 2026/07/17 12:36
 * Author: Scarra Luba
 */
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
   private final User user;
   private final String permissions;

   @Override
   @NullMarked
   public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(permissions.split(",".trim())).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(user.getStatus(), String.valueOf(User.HouseholdStatus.ACTIVE));
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(user.getStatus(), String.valueOf(User.HouseholdStatus.ACTIVE));
    }
}
