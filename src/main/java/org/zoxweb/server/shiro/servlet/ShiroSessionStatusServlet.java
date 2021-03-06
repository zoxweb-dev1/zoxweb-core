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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.zoxweb.shared.http.HTTPMethod;
import org.zoxweb.shared.http.HTTPStatusCode;
import org.zoxweb.shared.util.Const;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
@SuppressWarnings("serial")
public class ShiroSessionStatusServlet
	extends ShiroBaseServlet
{
	
	private static final transient Logger log = Logger.getLogger(Const.LOGGER_NAME);
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		     throws ServletException,
		            IOException
	{
		
		Subject subject = SecurityUtils.getSubject();
		if (subject == null || !subject.isAuthenticated())
		{
			log.info("security check required and user not authenticated");
			if(subject != null && subject.getSession() != null)
			{
				try
				{
					subject.getSession().stop();
				}
				catch(InvalidSessionException e)
				{
					log.info("Error " + e);
				}
			}
			resp.sendError(HTTPStatusCode.UNAUTHORIZED.CODE);
			
			return;
		}
		log.info("Subject check " + subject.getPrincipal() + ":" + subject.getSession().getId());
		resp.setStatus(HTTPStatusCode.OK.CODE);
	}

	/**
	 * @see org.zoxweb.server.shiro.servlet.ShiroBaseServlet#isSecurityCheckRequired(org.zoxweb.shared.http.HTTPMethod, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean isSecurityCheckRequired(HTTPMethod httpMethod, HttpServletRequest req)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
