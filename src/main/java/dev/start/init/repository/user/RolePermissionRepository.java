package dev.start.init.repository.user;

import dev.start.init.entity.user.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleId(Long roleId);
    List<RolePermission> findByRoleIdAndIsActive(Long roleId, Integer isActive);
    RolePermission findByRoleIdAndModuleIdAndDocumentId(Long roleId, String moduleId, String documentId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.roleId IN " +
            "(SELECT r.id FROM User u JOIN u.userRoles r WHERE u.publicId = :publicId) AND rp.isActive = :isActive")
    List<RolePermission> findByUserIdAndIsActive(@Param("publicId") String publicId, @Param("isActive") int isActive);
}


