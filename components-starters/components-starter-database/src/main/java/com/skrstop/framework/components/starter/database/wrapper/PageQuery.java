package com.skrstop.framework.components.starter.database.wrapper;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skrstop.framework.components.core.common.request.ClientPageQuery;
import com.skrstop.framework.components.starter.database.entity.AbstractTimeBaseEntity;
import com.skrstop.framework.components.util.value.data.ArrayUtil;
import com.skrstop.framework.components.util.value.lambda.LambdaUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

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
        this.getDescs().add(LambdaUtil.convertToFieldName(AbstractTimeBaseEntity::getUpdateTime));
        return this;
    }

    public PageQuery descCreateTime() {
        this.getDescs().add(LambdaUtil.convertToFieldName(AbstractTimeBaseEntity::getCreateTime));
        return this;
    }

    public PageQuery ascUpdateTime() {
        this.getAscs().add(LambdaUtil.convertToFieldName(AbstractTimeBaseEntity::getUpdateTime));
        return this;
    }

    public PageQuery ascCreateTime() {
        this.getAscs().add(LambdaUtil.convertToFieldName(AbstractTimeBaseEntity::getCreateTime));
        return this;
    }

    public <T> Page<T> toPage(Class<T> clazz) {
        Page<T> page = new Page<T>(this.getPageNumber(), this.getPageSize());
        page.setOrders(this.createOrderItem());
        return page;
    }

    public Page toPage() {
        Page page = new Page(this.getPageNumber(), this.getPageSize());
        page.setOrders(this.createOrderItem());
        return page;
    }

    public <T> Page<T> toPage(Class<T> clazz, boolean calcCount) {
        Page<T> page = this.toPage(clazz);
        page.setSearchCount(calcCount);
        return page;
    }

    public Page toPage(boolean calcCount) {
        Page page = this.toPage();
        page.setSearchCount(calcCount);
        return page;
    }

    private List<OrderItem> createOrderItem() {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.addAll(OrderItem.ascs(ArrayUtil.toArray(this.getAscs(), String.class)));
        orderItemList.addAll(OrderItem.descs(ArrayUtil.toArray(this.getDescs(), String.class)));
        return orderItemList;
    }

}
