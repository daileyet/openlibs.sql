package openthinks.libs.sql.dhibernate.support;

import openthinks.libs.sql.dao.BaseDao;
import openthinks.libs.sql.lang.Configurator;

/**
 * 默认实现的Session类
 * 
 * @author dmj
 * @version 17:20 2010/11/19
 */
class DefaultSession extends AbstractSession {
	private final BaseDao baseDao;

	public DefaultSession() {
		baseDao = new SessionDaoImpl();
	}

	public DefaultSession(Configurator configurator) {
		baseDao = new SessionDaoImpl();
		baseDao.setConfigurator(configurator);
	}

	@Override
	public BaseDao getBaseDao() {
		return baseDao;
	}

	@Override
	protected void setAutoClose(Boolean autoClose) {
		super.setAutoClose(autoClose);
		((SessionDaoImpl) this.getBaseDao()).setAutoClose(autoClose);
	}
}
