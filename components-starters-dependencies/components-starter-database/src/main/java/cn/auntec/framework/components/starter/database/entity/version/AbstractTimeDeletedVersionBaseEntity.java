package cn.auntec.framework.components.starter.database.entity.version;

import cn.auntec.framework.components.util.constant.StringPoolConst;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

/**
 * DO base entity by snow_flake id
 *
 * @author 蒋时华
 * @date 2019/6/26
 */
@Getter
@Setter
public abstract class AbstractTimeDeletedVersionBaseEntity extends AbstractTimeVersionBaseEntity {

    private static final long serialVersionUID = 3937442618809011749L;
    /**
     * 软删除标记
     */
    @TableLogic(value = StringPoolConst.ZERO, delval = StringPoolConst.ONE)
    public Boolean deleted;

}
