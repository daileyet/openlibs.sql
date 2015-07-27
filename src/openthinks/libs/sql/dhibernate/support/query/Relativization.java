/**
 * Licensed to the Apache Software Foundation (ASF) under one 
 * or more contributor license agreements. See the NOTICE file 
 * distributed with this work for additional information 
 * regarding copyright ownership. The ASF licenses this file 
 * to you under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations 
 * under the License. 
 * 
 * @Title: Relativization.java 
 * @Package sql.dhibernate.support.query.impl 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-12-2
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query;

/**
 * identity which can be relationship
 * 
 * @author minjdai
 * 
 */
public interface Relativization {

	/**
	 * get relationship sql
	 * 
	 * @return
	 */
	public abstract StringBuffer toSQL();

	/**
	 * the parameters for placeholder in generated sql
	 * 
	 * @return
	 */
	public Object[] parameters();

}