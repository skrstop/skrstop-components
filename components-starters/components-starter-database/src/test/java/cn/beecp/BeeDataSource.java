package cn.beecp;

/**
 * 测试桩, 见 {@link BeeDataSourceConfig}。
 */
public class BeeDataSource extends BeeDataSourceConfig implements javax.sql.DataSource {

    /**
     * 测试开关: 模拟构造失败
     */
    public static boolean failNextConstruction = false;

    public BeeDataSource() {
        if (failNextConstruction) {
            throw new IllegalStateException("boom");
        }
    }

    public BeeDataSource(BeeDataSourceConfig config) {
        if (failNextConstruction) {
            throw new IllegalStateException("boom");
        }
    }

    @Override
    public java.sql.Connection getConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.sql.Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override
    public java.io.PrintWriter getLogWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        throw new UnsupportedOperationException();
    }
}
