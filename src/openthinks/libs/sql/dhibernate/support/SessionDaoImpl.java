package openthinks.libs.sql.dhibernate.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import openthinks.libs.sql.dao.ConnectionManager;
import openthinks.libs.sql.dao.impl.BaseDaoImpl;

/**
 * SessionDaoImpl.java 以供Session内部使用的BaseDao的实现类
 * 
 * @author dmj
 * @version 17:00 2010/11/19
 */
class SessionDaoImpl extends BaseDaoImpl {
	/**
	 * 数据库连接对象,供该类中所有方法共享连接
	 */
	private Connection connection = null;
	private Boolean autoClose = true;
	private Lock lock = new ReentrantLock();

	/**
	 * @param autoClose
	 *            the autoClose to set
	 */
	public void setAutoClose(Boolean autoClose) {
		this.autoClose = autoClose;
	}

	/**
	 * @return the autoClose
	 */
	public Boolean isAutoClose() {
		return autoClose;
	}

	public SessionDaoImpl() {
		super();
	}

	/**
	 * 取得数据库连接<br>
	 * 重写BaseDaoImpl中的该方法,确保该类中的数据库连接只共用一个.<br>
	 * 
	 * @throws Exception
	 * @return Connection 连接对象
	 */
	@Override
	public Connection getConn() throws ClassNotFoundException, SQLException {
		lock.lock();
		try {
			if (connection == null)
				connection = ConnectionManager.getConnection(getConfigurator());
			return connection;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 设置数据连接
	 * 
	 * @param connection
	 *            Connection连接对象
	 */
	@Override
	public void setConn(Connection connection) {
		lock.lock();
		try {
			closeConnection(this.connection);
			this.connection = connection;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void closeConnection(Connection conn) {
		lock.lock();
		try {
			if (isAutoClose())
				super.closeConnection(conn);
			if (isUsePool()) {
				ConnectionManager.closeConnection(getConfigurator(), conn);
				this.connection = null;
			}
		} finally {
			lock.unlock();
		}

	}

}
