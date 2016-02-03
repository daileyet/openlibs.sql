package com.openthinks.libs.sql.entity.key;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Entity key generator
 * 
 * @author minjdai
 * 
 */
public abstract class IdGenerator {

	private final static Logger logger = Logger.getLogger(IdGenerator.class);
	static Map<Class<?>, IdGenerator> generatorMap = new ConcurrentHashMap<Class<?>, IdGenerator>();
	static {
		generatorMap.put(Object.class, new DefaultIdGenerator());
	}

	public static void register(Class<?> type, IdGenerator generator) {
		generatorMap.put(type, generator);
	}

	public static IdGenerator getGenerator(Class<?> type) {
		IdGenerator generator = generatorMap.get(type);
		if (generator == null) {
			logger.log(Level.WARN, "Can not find this type:" + type + " IdGenerator, use default IdGenerator");
			generator = generatorMap.get(Object.class);
		}
		return generator;
	}

	public abstract String generator();

	static class DefaultIdGenerator extends IdGenerator {
		@Override
		public String generator() {
			return UUID.randomUUID().toString();
		}
	}

}
