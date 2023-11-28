package com.jphoebe.framework.components.util.db.redis.jedis;

/**
 * @author 蒋时华
 * @date 2020-07-02 11:24:15
 */
public interface JedisConfigConst {

    /*** 地址，默认localhost */
    String HOST = "host";
    /*** 端口，默认6379 */
    String PORT = "port";
    /*** 超时，默认2000 */
    String TIMEOUT = "timeout";
    /*** 连接超时，默认timeout */
    String CONNECTION_TIMEOUT = "connectionTimeout";
    /*** 读取超时，默认timeout */
    String SO_TIMEOUT = "soTimeout";
    /*** 密码，默认无 */
    String PASSWORD = "password";
    /*** 数据库序号，默认0 */
    String DATABASE = "database";
    /*** 客户端名，默认"Auntec" */
    String CLIENT_NAME = "clientName";
    /*** SSL连接，默认false */
    String SSL = "ssl";
    /*** 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true */
    String BLOCK_WHEN_EXHAUSTED = "BlockWhenExhausted";
    /*** 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数) */
    String EVICTION_POLICY_CLASSNAME = "evictionPolicyClassName";
    /*** 是否启用pool的jmx管理功能, 默认true */
    String JMX_ENABLED = "jmxEnabled";
    /*** 是否启用后进先出, 默认true */
    String LIFO = "lifo";
    /*** 最大空闲连接数, 默认8个 */
    String MAX_IDLE = "maxIdle";
    /*** 最小空闲连接数, 默认0 */
    String MIN_IDLE = "minIdle";
    /*** 最大连接数, 默认8个 */
    String MAX_TOTAL = "maxTotal";
    /*** 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1 */
    String MAX_WAIT_MILLIS = "maxWaitMillis";
    /*** 逐出连接的最小空闲时间 默认1800000毫秒(30分钟) */
    String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
    /*** 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 */
    String NUM_TESTS_PER_EVICTION_RUN = "numTestsPerEvictionRun";
    /*** 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略) */
    String SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = "SoftMinEvictableIdleTimeMillis";
    /*** 在获取连接的时候检查有效性, 默认false */
    String TEST_ON_BORROW = "testOnBorrow";
    /*** 在空闲时检查有效性, 默认false */
    String TEST_WHILE_IDLE = "testWhileIdle";
    /*** 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1 */
    String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";

}
