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
package org.zoxweb.server.shiro.authc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.zoxweb.server.crypto.CryptoUtil;
import org.zoxweb.shared.crypto.PasswordDAO;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public class PasswordDAOCredentialsMatcher
	implements CredentialsMatcher
{

	/**
	 * @see org.apache.shiro.authc.credential.CredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo)
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
	{
		if(!token.getPrincipal().equals(info.getPrincipals().getPrimaryPrincipal()))
		{
			return false;
		}
		
		try 
		{
			
			if (token instanceof DomainUsernamePasswordToken && ((DomainUsernamePasswordToken)token).isAutoAuthenticationEnabled())
			{
				return true;
			}
			
			PasswordDAO passwordDAO = (PasswordDAO) info.getCredentials();
			String password = null;
			
			
			if (token.getCredentials() instanceof char[])
			{
				password = new String ((char[])token.getCredentials());
			}
			else if (token.getCredentials() instanceof byte[])
			{
				password = new String((byte[])token.getCredentials(), "UTF-8");
			}
			else if(token.getCredentials() instanceof String)
			{
				password = (String) token.getCredentials();
			}
			

			return CryptoUtil.isPasswordValid(passwordDAO, password);
		}
		catch (Exception e )
		{
			e.printStackTrace();
		}
		
		
		// TODO Auto-generated method stub
		return false;
	}

}
