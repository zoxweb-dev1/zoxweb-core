/*
 * Copyright (c) 2012-2015 ZoxWeb.com LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.zoxweb.server.shiro.servlet;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.zoxweb.shared.util.Const;


/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public abstract class ShiroBaseWebListener extends  EnvironmentLoaderListener {

	private static final transient Logger log = Logger.getLogger(Const.LOGGER_NAME);
	
	protected abstract void init( WebSecurityManager wsm);
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		log.info("Initialized");
		init(initEnvironment(sce.getServletContext()).getWebSecurityManager());
		

	}	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		super.contextDestroyed(sce);

	}
}
