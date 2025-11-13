package com.skrstop.framework.components.starter.mongodb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyCreateBy;
import com.skrstop.framework.components.starter.mongodb.annotation.property.PropertyCreateTime;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractCreateByTimeBaseEntity extends AbstractBaseEntity {

    @Serial
    private static final long serialVersionUID = -4024689623085796268L;

    /**
     * 创建人
     */
    @PropertyCreateBy
    public Long createBy;
    /**
     * 创建时间
     */
    @PropertyCreateTime
    @DateTimeFormat(pattern = DateFormatConst.NORM_DATETIME_MS_PATTERN)
    @JsonFormat(pattern = DateFormatConst.NORM_DATETIME_MS_PATTERN)
    public LocalDateTime createTime;

}
