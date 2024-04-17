package com.skrstop.framework.components.starter.database.entity.version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractCreateTimeVersionBaseEntity extends AbstractVersionBaseEntity {

    private static final long serialVersionUID = -4024689623085796268L;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @TableId(type = IdType.ASSIGN_ID)
//    @TableId(type = IdType.AUTO)
//    public Long id;

    /**
     * 创建人
     */
    public Long createBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateFormatConst.NORM_DATETIME_MS_PATTERN)
    @JsonFormat(pattern = DateFormatConst.NORM_DATETIME_MS_PATTERN)
    public LocalDateTime createTime;

}
