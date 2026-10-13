package com.skrstop.framework.components.util.db.mongodb;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 蒋时华
 */
public class MongoUtilTest {

    @Test
    public void testMongoUtilInstance() {
        MongoUtil mongoUtil = new MongoUtil();
        Assert.assertNotNull(mongoUtil);
    }

    @Test
    public void testMongoConfigConst() {
        Assert.assertEquals("host", MongoConfigConst.HOST);
        Assert.assertEquals("connectTimeout", MongoConfigConst.CONNECT_TIMEOUT);
        Assert.assertEquals("socketTimeout", MongoConfigConst.SOCKET_TIMEOUT);
        Assert.assertEquals("maxWaitTime", MongoConfigConst.MAX_WAIT_TIME);
        Assert.assertEquals("connectionsPerHost", MongoConfigConst.CONNECTIONS_PER_HOST);
        Assert.assertEquals("socketKeepAlive", MongoConfigConst.SOCKET_KEEP_ALIVE);
        Assert.assertEquals("threadsAllowedToBlockForConnectionMultiplier",
                MongoConfigConst.THREADS_ALLOWED_TO_BLOCK_FOR_CONNECTION_MULTIPLIER);
    }

}
