package com.jphoebe.framework.components.starter.mongodb.wrapper;

import com.jphoebe.framework.components.core.common.request.ClientPageQuery;
import com.jphoebe.framework.components.starter.mongodb.service.impl.SuperServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

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

    private static final long serialVersionUID = -4290844828041972684L;

    public PageQuery descUpdateTime() {
        this.getDescs().add(SuperServiceImpl.UPDATE_TIME);
        return this;
    }

    public PageQuery descCreateTime() {
        this.getDescs().add(SuperServiceImpl.CREATE_TIME);
        return this;
    }

    public PageQuery ascUpdateTime() {
        this.getAscs().add(SuperServiceImpl.UPDATE_TIME);
        return this;
    }

    public PageQuery ascCreateTime() {
        this.getAscs().add(SuperServiceImpl.CREATE_TIME);
        return this;
    }

}
