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
 * @Title: Query.java 
 * @Package sql.dhibernate.support 
 * @Description: TODO
 * @author minjdai 
 * @date 2013-11-25
 * @version V1.0 
 */
package openthinks.libs.sql.dhibernate.support.query;

import java.util.List;

/**
 * @author minjdai
 * 
 */
public interface Query<T> {

	public Query<T> queryObject(Class<T> clz);

	public Query<T> addFilter(QueryFilter... filter);

	public List<T> execute();

}
