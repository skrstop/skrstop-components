package com.skrstop.framework.components.example.starters.simple.entity.response;

import com.skrstop.framework.components.starter.annotation.anno.function.PrivacyInfoValue;
import com.skrstop.framework.components.starter.annotation.constant.PrivacyInfoType;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-04 10:27:12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ExamplePrivacyInfo {

    @PrivacyInfoValue(limitIntranet = true)
    private String valueDefault;

    @PrivacyInfoValue(type = PrivacyInfoType.SET_NULL, limitIntranet = true)
    private String valueSetNull;

    @PrivacyInfoValue(type = PrivacyInfoType.DEFAULT_BANK_CARD, limitIntranet = true)
    private String valueBankCard;

    @PrivacyInfoValue(type = PrivacyInfoType.DEFAULT_ID_CARD, limitIntranet = true)
    private String valueIdCard;

    @PrivacyInfoValue(type = PrivacyInfoType.DEFAULT_PHONE, limitIntranet = true)
    private String valuePhone;

    @PrivacyInfoValue(type = PrivacyInfoType.DEFAULT_EMAIL)
    private String valueEmail;

    @PrivacyInfoValue(type = "test", limitIntranet = true)
    private String custom;

    private Integer other1;
    private Long other2;
    private Boolean other3;
    private LocalDateTime other4;

}
