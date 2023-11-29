package com.zoe.framework.components.util.db.mongodb;

/**
 * @author 蒋时华
 * @date 2020-07-02 11:24:15
 */
public interface MongoConfigConst {

    /*** 地址 */
    String HOST = "host";
    /*** 每个主机答应的连接数（每个主机的连接池大小），当连接池被用光时，会被阻塞住 ，默以为10 --int */
    String CONNECTIONS_PER_HOST = "connectionsPerHost";
    /*** 线程队列数，它以connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误 --int */
    String THREADS_ALLOWED_TO_BLOCK_FOR_CONNECTION_MULTIPLIER = "threadsAllowedToBlockForConnectionMultiplier";
    /*** 被阻塞线程从连接池获取连接的最长等待时间（ms） --int */
    String MAX_WAIT_TIME = "maxWaitTime";
    /*** 在建立（打开）套接字连接时的超时时间（ms），默以为0（无穷） --int */
    String CONNECT_TIMEOUT = "connectTimeout";
    /*** 套接字超时时间;该值会被传递给Socket.setSoTimeout(int)。默以为0（无穷） --int */
    String SOCKET_TIMEOUT = "socketTimeout";
    /*** 是否打开长连接. defaults to false --boolean */
    String SOCKET_KEEP_ALIVE = "socketKeepAlive";

}
