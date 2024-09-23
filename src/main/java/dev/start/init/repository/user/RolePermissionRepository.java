package dev.start.init.repository.user;

import dev.start.init.entity.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the Permission entity.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Find a permission by name.
     *
     * @param name the name of the permission
     * @return an Optional containing the Permission, if found
     */
    Optional<Permission> findByName(String name);

    /**
     * Check if a permission exists by name.
     *
     * @param name the name to check
     * @return true if the permission exists, false otherwise
     */
    boolean existsByName(String name);
}

