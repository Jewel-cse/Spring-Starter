package dev.start.init.config;

import dev.start.init.entity.user.Privilege;
import dev.start.init.entity.user.Role;
import dev.start.init.entity.user.User;
import dev.start.init.enums.PrivilegeType;
import dev.start.init.enums.RoleType;
import dev.start.init.repository.user.PrivilegeRepository;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RolePrivilegeDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ.getName());
        Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE.getName());
        Privilege deletePrivilege = createPrivilegeIfNotFound(PrivilegeType.DELETE.getName());
        Privilege editPrivilege = createPrivilegeIfNotFound(PrivilegeType.EDIT.getName());

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege, editPrivilege);
        List<Privilege> systemUserPrivileges = Arrays.asList(readPrivilege, writePrivilege, editPrivilege);
        List<Privilege> userPrivileges = Arrays.asList(readPrivilege);


        createRoleIfNotFound(RoleType.ROLE_ADMIN.getName(), adminPrivileges);
        createRoleIfNotFound(RoleType.ROLE_SYSTEM_USER.getName(), systemUserPrivileges);
        createRoleIfNotFound(RoleType.ROLE_USER.getName(), userPrivileges);

        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN.getName());
        Role systemUserRole = roleRepository.findByName(RoleType.ROLE_SYSTEM_USER.getName());
        Role userRole = roleRepository.findByName(RoleType.ROLE_USER.getName());

        if (adminRole != null && userRepository.findByUsername("jewel") == null) {
            saveUser(adminRole,"jewel");
        }
        if (systemUserRole != null && userRepository.findByUsername("harun") == null) {
            saveUser(systemUserRole,"harun");
        }
        if (userRole != null && userRepository.findByUsername("sayem") == null) {
            saveUser(userRole,"sayem");
        }

        alreadySetup = true;
    }

    private void saveUser(Role role,String name) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName("Rana");
        user.setUsername(name);
        user.setPassword(passwordEncoder.encode(name));
        user.setEmail(name+"cseru@gmail.com");
        user.setRoles(Arrays.asList(role));
        user.setDateRegistered(LocalDateTime.now());
        //enabling some use columns
        user.setActive(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
