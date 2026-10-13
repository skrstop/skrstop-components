package cn.beecp;

/**
 * 测试桩: 模块依赖的 beecp 4.x 无 cn.beecp 包, 而 dynamic-datasource 4.5.0 的
 * BeeCpDataSourceCreator 需要 cn.beecp API(与 beecp 3.x 兼容), 仅为覆盖 creator 逻辑提供最小实现。
 */
public class BeeDataSourceConfig {

    private String username;
    private String password;
    private String jdbcUrl;
    private String driverClassName;
    private String poolName;
    private String defaultCatalog;
    private String defaultSchema;
    private Boolean defaultReadOnly;
    private Boolean defaultAutoCommit;
    private Integer defaultTransactionIsolationCode;
    private String defaultTransactionIsolationName;
    private Boolean fairMode;
    private Integer initialSize;
    private Integer maxActive;
    private Integer borrowSemaphoreSize;
    private Long maxWait;
    private Long idleTimeout;
    private Long holdTimeout;
    private String connectionTestSql;
    private Integer connectionTestTimeout;
    private Long connectionTestIntegererval;
    private Long idleCheckTimeIntegererval;
    private Boolean forceCloseUsingOnClear;
    private Long delayTimeForNextClear;
    private String connectionFactoryClassName;
    private String xaConnectionFactoryClassName;
    private java.util.Properties connectProperties;
    private String poolImplementClassName;
    private Boolean enableJmx;

    public void setUsername(String username) {this.username = username;}

    public void setPassword(String password) {this.password = password;}

    public void setJdbcUrl(String jdbcUrl) {this.jdbcUrl = jdbcUrl;}

    public void setDriverClassName(String driverClassName) {this.driverClassName = driverClassName;}

    public void setPoolName(String poolName) {this.poolName = poolName;}

    public void setDefaultCatalog(String defaultCatalog) {this.defaultCatalog = defaultCatalog;}

    public void setDefaultSchema(String defaultSchema) {this.defaultSchema = defaultSchema;}

    public void setDefaultReadOnly(Boolean defaultReadOnly) {this.defaultReadOnly = defaultReadOnly;}

    public void setDefaultAutoCommit(Boolean defaultAutoCommit) {this.defaultAutoCommit = defaultAutoCommit;}

    public void setDefaultTransactionIsolationCode(Integer defaultTransactionIsolationCode) {this.defaultTransactionIsolationCode = defaultTransactionIsolationCode;}

    public void setDefaultTransactionIsolationName(String defaultTransactionIsolationName) {this.defaultTransactionIsolationName = defaultTransactionIsolationName;}

    public void setFairMode(Boolean fairMode) {this.fairMode = fairMode;}

    public void setInitialSize(Integer initialSize) {this.initialSize = initialSize;}

    public void setMaxActive(Integer maxActive) {this.maxActive = maxActive;}

    public void setBorrowSemaphoreSize(Integer borrowSemaphoreSize) {this.borrowSemaphoreSize = borrowSemaphoreSize;}

    public void setMaxWait(Long maxWait) {this.maxWait = maxWait;}

    public void setIdleTimeout(Long idleTimeout) {this.idleTimeout = idleTimeout;}

    public void setHoldTimeout(Long holdTimeout) {this.holdTimeout = holdTimeout;}

    public void setConnectionTestSql(String connectionTestSql) {this.connectionTestSql = connectionTestSql;}

    public void setConnectionTestTimeout(Integer connectionTestTimeout) {this.connectionTestTimeout = connectionTestTimeout;}

    public void setConnectionTestIntegererval(Long connectionTestIntegererval) {this.connectionTestIntegererval = connectionTestIntegererval;}

    public void setIdleCheckTimeIntegererval(Long idleCheckTimeIntegererval) {this.idleCheckTimeIntegererval = idleCheckTimeIntegererval;}

    public void setForceCloseUsingOnClear(Boolean forceCloseUsingOnClear) {this.forceCloseUsingOnClear = forceCloseUsingOnClear;}

    public void setDelayTimeForNextClear(Long delayTimeForNextClear) {this.delayTimeForNextClear = delayTimeForNextClear;}

    public void setConnectionFactoryClassName(String connectionFactoryClassName) {this.connectionFactoryClassName = connectionFactoryClassName;}

    public void setXaConnectionFactoryClassName(String xaConnectionFactoryClassName) {this.xaConnectionFactoryClassName = xaConnectionFactoryClassName;}

    public void setConnectProperties(java.util.Properties connectProperties) {this.connectProperties = connectProperties;}

    public void setPoolImplementClassName(String poolImplementClassName) {this.poolImplementClassName = poolImplementClassName;}

    public void setEnableJmx(Boolean enableJmx) {this.enableJmx = enableJmx;}

    void copyTo(BeeDataSourceConfig target) {
        target.username = this.username;
        target.password = this.password;
        target.jdbcUrl = this.jdbcUrl;
        target.driverClassName = this.driverClassName;
        target.poolName = this.poolName;
    }
}
