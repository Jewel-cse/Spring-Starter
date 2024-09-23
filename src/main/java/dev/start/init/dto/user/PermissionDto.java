package dev.start.init.dto.user;

import lombok.Data;

@Data
public class PermissionDto {
    private boolean canCreate;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canDelete;

    public PermissionDto(boolean canCreate, boolean canRead, boolean canUpdate, boolean canDelete) {
        this.canCreate = canCreate == true;
        this.canRead = canRead == true;
        this.canUpdate = canUpdate == true;
        this.canDelete = canDelete == true;
    }


    public PermissionDto(Integer canCreate, Integer canRead, Integer canUpdate, Integer canDelete) {

        this.canCreate = canCreate == 1;
        this.canRead = canRead == 1;
        this.canUpdate = canUpdate == 1;
        this.canDelete = canDelete == 1;
    }
}
