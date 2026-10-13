package com.skrstop.framework.components.util.entity;

import com.skrstop.framework.components.util.entity.list.UniversalStructureList;
import com.skrstop.framework.components.util.entity.tree.UniversalStructureTree;
import com.skrstop.framework.components.util.entity.tree.UniversalStructureTreeProperties;
import com.skrstop.framework.components.util.entity.value.PropertiesValueList;
import com.skrstop.framework.components.util.entity.value.PropertiesValueObj;
import com.skrstop.framework.components.util.entity.value.PropertiesValueStr;
import com.skrstop.framework.components.util.entity.value.ValueItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * @author 蒋时华
 */
public class UniversalStructureTest {

    @Test
    public void testInterfaceDefaults() {
        // 接口默认方法
        IUniversalStructure structure = new IUniversalStructure() {
        };
        Assert.assertEquals("", structure.getType());
        Assert.assertEquals("", structure.getName());
        Assert.assertEquals("", structure.getAlias());
    }

    @Test
    public void testProperties() {
        PropertiesValueStr str = PropertiesValueStr.builder().value("v").build();
        Assert.assertEquals("v", str.getValue());
        str.setValue("v2");
        Assert.assertNotNull(new PropertiesValueStr());
        Assert.assertNotNull(new PropertiesValueStr("v3"));

        PropertiesValueList list = PropertiesValueList.builder()
                .value(Collections.singletonList(ValueItem.builder().key("k").build()))
                .build();
        Assert.assertEquals(1, list.getValue().size());
        list.setValue(Collections.emptyList());
        Assert.assertNotNull(new PropertiesValueList());
        Assert.assertNotNull(new PropertiesValueList(Collections.emptyList()));

        PropertiesValueObj obj = PropertiesValueObj.builder()
                .value(new LinkedHashMap<>())
                .build();
        Assert.assertTrue(obj.getValue().isEmpty());
        obj.setValue(new LinkedHashMap<>());
        Assert.assertNotNull(new PropertiesValueObj());
        Assert.assertNotNull(new PropertiesValueObj(new LinkedHashMap<>()));

        ValueItem item = ValueItem.builder().key("k").name("n").build();
        Assert.assertEquals("k", item.getKey());
        Assert.assertEquals("n", item.getName());
        item.setKey("k2");
        item.setName("n2");
    }

    @Test
    public void testUniversalStructureProperties() {
        UniversalStructureProperties properties = UniversalStructureProperties.builder()
                .key("k")
                .name("n")
                .alias("a")
                .description("d")
                .dataType((byte) 1)
                .unitType("u")
                .unitTitle("ut")
                .valueStr(PropertiesValueStr.builder().value("v").build())
                .valueList(PropertiesValueList.builder().build())
                .valueObj(PropertiesValueObj.builder().build())
                .defaultValue("dv")
                .optionalValueStr(PropertiesValueStr.builder().build())
                .optionalValueList(PropertiesValueList.builder().build())
                .optionalValueObj(PropertiesValueObj.builder().build())
                .extra(Collections.emptyMap())
                .extend("e")
                .scope("s")
                .build();
        Assert.assertEquals("k", properties.getKey());
        Assert.assertEquals("n", properties.getName());
        Assert.assertEquals("a", properties.getAlias());
        Assert.assertEquals("d", properties.getDescription());
        Assert.assertEquals((byte) 1, properties.getDataType().byteValue());
        Assert.assertEquals("u", properties.getUnitType());
        Assert.assertEquals("ut", properties.getUnitTitle());
        Assert.assertNotNull(properties.getValueStr());
        Assert.assertNotNull(properties.getValueList());
        Assert.assertNotNull(properties.getValueObj());
        Assert.assertEquals("dv", properties.getDefaultValue());
        Assert.assertNotNull(properties.getOptionalValueStr());
        Assert.assertNotNull(properties.getOptionalValueList());
        Assert.assertNotNull(properties.getOptionalValueObj());
        Assert.assertTrue(properties.getExtra().isEmpty());
        Assert.assertEquals("e", properties.getExtend());
        Assert.assertEquals("s", properties.getScope());
    }

    @Test
    public void testTree() {
        UniversalStructureTreeProperties props = new UniversalStructureTreeProperties();
        props.setKey("k");
        props.setHasChildren(true);
        props.setChildProperties(Collections.emptyList());
        Assert.assertEquals("k", props.getKey());
        Assert.assertTrue(props.isHasChildren());
        Assert.assertTrue(props.getChildProperties().isEmpty());

        UniversalStructureTree tree = UniversalStructureTree.builder()
                .type("t")
                .name("n")
                .alias("a")
                .tree(Collections.emptyList())
                .build();
        Assert.assertEquals("t", tree.getType());
        Assert.assertEquals("n", tree.getName());
        Assert.assertEquals("a", tree.getAlias());
        Assert.assertTrue(tree.getTree().isEmpty());
        tree.setType("t2");
        tree.setName("n2");
        tree.setAlias("a2");
        tree.setTree(null);
    }

    @Test
    public void testList() {
        UniversalStructureList list = UniversalStructureList.builder()
                .type("t")
                .name("n")
                .alias("a")
                .list(Collections.emptyList())
                .build();
        Assert.assertEquals("t", list.getType());
        Assert.assertEquals("n", list.getName());
        Assert.assertEquals("a", list.getAlias());
        Assert.assertTrue(list.getList().isEmpty());
        list.setType("t2");
    }

}
