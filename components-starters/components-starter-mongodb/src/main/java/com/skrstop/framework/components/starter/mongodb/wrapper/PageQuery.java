package com.skrstop.framework.components.starter.mongodb.wrapper;

import com.skrstop.framework.components.core.common.request.ClientPageQuery;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;

/**
 * 分页工具
 *
 * @author 蒋时华
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@Validated
public class PageQuery extends ClientPageQuery {

    @Serial
    private static final long serialVersionUID = -4290844828041972684L;

}
