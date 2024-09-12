package dev.start.init.repository.auth;

import dev.start.init.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by name.
     *
     * @param name the name of the role
     * @return an Optional containing the Role, if found
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role exists by name.
     *
     * @param name the name to check
     * @return true if the role exists, false otherwise
     */
    boolean existsByName(String name);
}

