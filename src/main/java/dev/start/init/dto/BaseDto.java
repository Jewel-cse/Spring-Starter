package dev.start.init.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public class BaseDto {
    protected String publicId;

    private short version;

    @NonNull
    private boolean active = true;

    private Integer stateId = 0;

    private Integer actionId = 0;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private String machineName;

    private String machineIp;
}
