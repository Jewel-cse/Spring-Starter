package dev.start.init.service.mfa;

import dev.start.init.enums.MultiFactorMethodType;

public interface MfaService {

    void enableMfaForUser(Long userId, MultiFactorMethodType methodType, String secretKey);

    void disableMfaForUser(Long userId, MultiFactorMethodType methodType);
}
