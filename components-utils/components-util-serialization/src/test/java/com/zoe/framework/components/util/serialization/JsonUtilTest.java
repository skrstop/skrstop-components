package com.zoe.framework.components.util.serialization;

import com.zoe.framework.components.util.serialization.json.JsonUtil;
import org.junit.Test;

import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-11-30 14:00:53
 */
public class JsonUtilTest {

    @Test
    public void test() {
        String json = "{\"after\":{\"sort_rank\":22,\"enable_state\":1,\"promotion_id\":3,\"draw\":12,\"team_id\":25575,\"rank_type\":200,\"team_name\":\"FC琉球\",\"points\":28,\"loss\":16,\"goals_lost\":56,\"goals_diff\":-19,\"rank_code\":22,\"_id\":\"test\",\"source_code\":1001,\"win\":6,\"game_id\":1771984000,\"goals\":37,\"operate_state\":0},\"db\":\"sportdata_jdddata\",\"op\":\"u\",\"opTsMs\":1701770859000,\"table\":\"s_snapshot_game_team_rank\"}";
        String s2 = JsonUtil.format(json);
        System.out.println(s2);
        String s3 = JsonUtil.formatJsonStr(json);
        System.out.println(s3);

        List<String> strings = JsonUtil.listAllProperties(json);
        System.out.println("aaa");
    }

}
