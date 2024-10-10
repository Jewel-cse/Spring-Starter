package dev.start.init.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dev.start.init.constants.CacheConstants;
import dev.start.init.entity.user.Privilege;
import dev.start.init.entity.user.Role;
import dev.start.init.entity.user.User;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of service used to query user details during login.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private MessageSource messages;
    private RoleRepository roleRepository;

    /**
     * Locates the user based on the usernameOrEmail. In the actual implementation, the search may be
     * case-sensitive, or case-insensitive depending on how the implementation instance is configured.
     * In this case, the <code>UserDetails</code> object that comes back may have a usernameOrEmail
     * that is of a different case than what was actually requested.
     *
     * @param usernameOrEmail the usernameOrEmail identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *     GrantedAuthority
     */

    @Override
    @Cacheable(key = "{ #root.methodName, #usernameOrEmail }", value = CacheConstants.USER_DETAILS)
    public UserDetails loadUserByUsername(final String usernameOrEmail) {
        // Ensure that usernameOrEmail is not empty or null.
        if (StringUtils.isNotBlank(usernameOrEmail)) {
            User storedUser =
                    UserUtils.isEmail(usernameOrEmail)
                            ? userRepository.findByEmail(usernameOrEmail)
                            : userRepository.findByUsername(usernameOrEmail);
            if (Objects.isNull(storedUser)) {
                LOG.warn("No record found for storedUser with usernameOrEmail {}", usernameOrEmail);
                throw new UsernameNotFoundException(
                        "User with usernameOrEmail " + usernameOrEmail + " not found");
            }
            return UserDetailsBuilder.buildUserDetails(storedUser);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}

/*
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
              " ", " ", true, true, true, true,
              getAuthorities(Arrays.asList(
                roleRepository.findByName("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
          user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
          true, getAuthorities(user.getRoles()));
    }

}
*
* */