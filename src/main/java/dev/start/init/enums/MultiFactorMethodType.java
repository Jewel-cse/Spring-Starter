package dev.start.init.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MultiFactorMethodType {
    SMS, EMAIL, GOOGLE_AUTHENTICATOR, WHATSAPP_SMS
}
