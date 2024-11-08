package dev.start.init.dto.user;

import dev.start.init.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RoleDto implements Serializable {

    private Integer id;
    private String name;

    public RoleDto(Integer id, String name){
        this.name = name;
        this.id = id;

    }

    public RoleDto(RoleType roleType){
        this.name = roleType.getName();
        this.id = roleType.getId();
    }
}


