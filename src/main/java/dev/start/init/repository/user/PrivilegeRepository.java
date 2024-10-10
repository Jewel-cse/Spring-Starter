package dev.start.init.repository.user;

import dev.start.init.entity.user.Privilege;
import dev.start.init.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);

    List<Privilege> findPrivilegesByRoles(Role role);

}
