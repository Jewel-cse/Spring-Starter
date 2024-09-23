package dev.start.init.dto;

import java.time.LocalDateTime;

public class BaseDto {
    protected String publicId;

    private short version;

    private boolean active;

    private Integer stateId = 0;

    private Integer actionId = 0;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private String machineName;

    private String machineIp;
}
