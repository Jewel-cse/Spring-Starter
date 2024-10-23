package dev.start.init.repository.user;

import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultiFactorAuthRepository extends JpaRepository<MultiFactorAuth, Long> {

    @Query("SELECT m FROM MultiFactorAuth m WHERE m.user.publicId = :publicId AND m.methodType = :methodType")
    Optional<MultiFactorAuth> findByUserPublicIdAndMethodType(@Param("publicId") String publicId, @Param("methodType") MultiFactorMethodType methodType);

    @Query("SELECT m FROM MultiFactorAuth m WHERE m.user.username = :username AND m.methodType = :methodType")
    Optional<MultiFactorAuth> findByUsernameAndMethodType(@Param("username") String username, @Param("methodType") MultiFactorMethodType methodType);

    @Query("SELECT m FROM MultiFactorAuth m WHERE m.user.id = :userId AND m.methodType = :methodType")
    Optional<MultiFactorAuth> findByUserIdAndMethodType(@Param("userId") Long userId, @Param("methodType") MultiFactorMethodType methodType);


    @Query("SELECT Mfa FROM MultiFactorAuth Mfa WHERE Mfa.user.id = :userId")
    Optional<MultiFactorAuth> findByUserId(Long userId);

    @Query("SELECT Mfa FROM MultiFactorAuth Mfa WHERE Mfa.user.username = :username")
    Optional<MultiFactorAuth> findByUsername(String username);

    @Query("SELECT Mfa FROM MultiFactorAuth Mfa WHERE Mfa.user.email = :email")
    Optional<MultiFactorAuth> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM MultiFactorAuth Mfa WHERE Mfa.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}

