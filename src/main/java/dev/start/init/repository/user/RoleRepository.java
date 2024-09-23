package dev.start.init.repository.user;

import dev.start.init.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the role.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Repository
//@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Merges an existing role with the role instance given.
     *
     * @param role the role
     * @return the persisted role with assigned id
     */
    //Role merge(Role role);

    /**
     * Gets role associated with required name.
     *
     * @param name name of role.
     * @return Role found.
     */
    Role findByName(String name);
}


