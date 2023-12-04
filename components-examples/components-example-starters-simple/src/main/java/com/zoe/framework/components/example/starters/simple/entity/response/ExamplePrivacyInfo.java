package com.zoe.framework.components.example.starters.simple.entity.response;

import com.zoe.framework.components.starter.annotation.anno.aspect.PrivacyInfoValue;
import com.zoe.framework.components.starter.annotation.constant.PrivacyInfoType;
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

    @PrivacyInfoValue
    private String valueDefault;

    @PrivacyInfoValue(type = PrivacyInfoType.SET_NULL)
    private String valueSetNull;

    @PrivacyInfoValue(type = PrivacyInfoType.BANK_CARD)
    private String valueBankCard;

    @PrivacyInfoValue(type = PrivacyInfoType.ID_CARD)
    private String valueIdCard;

    @PrivacyInfoValue(type = PrivacyInfoType.PHONE)
    private String valuePhone;

    @PrivacyInfoValue(type = PrivacyInfoType.EMAIL)
    private String valueEmail;

    private Integer other1;
    private Long other2;
    private Boolean other3;
    private LocalDateTime other4;

}
