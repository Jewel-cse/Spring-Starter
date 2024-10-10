package dev.start.init.service.user;

import dev.start.init.entity.user.Privilege;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface PrivilegeService {

    List<Privilege> getActivePrivilegeByRoleId(Integer roleId);
    Privilege updatePrivilege(Integer roleId, List<Privilege> privilegeList);
    //List<Privilege> getPrivilegeByUserId(String publicId);
}

