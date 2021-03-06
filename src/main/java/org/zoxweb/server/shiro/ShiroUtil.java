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
package org.zoxweb.server.shiro;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.zoxweb.shared.data.shiro.ShiroNVEntityCRUDs;
import org.zoxweb.shared.data.shiro.ShiroTokenReplacement;
import org.zoxweb.server.shiro.authc.DomainPrincipalCollection;
import org.zoxweb.server.shiro.authc.DomainUsernamePasswordToken;
import org.zoxweb.shared.security.AccessException;
import org.zoxweb.shared.util.CRUD;
import org.zoxweb.shared.util.Const;
import org.zoxweb.shared.util.NVEntity;
import org.zoxweb.shared.util.SharedStringUtil;
import org.zoxweb.shared.util.SharedUtil;



public class ShiroUtil
{
	
	private static final Logger log = Logger.getLogger(Const.LOGGER_NAME);
	
	public static boolean login(String domain, String realm, String username, String password)
	{
		Subject subject = SecurityUtils.getSubject();	
		if (!subject.isAuthenticated())
		{
            UsernamePasswordToken token = new DomainUsernamePasswordToken(username, password, false, null, domain);
            try
            {
            	subject.login(token);
            	return true;
            }
            catch(ShiroException e )
            {
            	e.printStackTrace();
            }	
		}
		else
		{
			return true;
		}
		
		return false;
	}
	
	
	public static Subject loginSubject(String domain, String realm, String username, String password)
		throws ShiroException
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			
			if (!subject.isAuthenticated())
			{
	            UsernamePasswordToken token = new DomainUsernamePasswordToken(username, password, false, null, domain);
	            subject.login(token);
			}
			return subject;
		}
		catch (ShiroException e)
		{
			//e.printStackTrace();
			throw new AccessException(e.getMessage());
		}
	}
	
	public static String subjectUserID()
		throws AccessException
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			
			if (subject.isAuthenticated())
			{
				if (subject.getPrincipals() instanceof DomainPrincipalCollection)
				{
					return ((DomainPrincipalCollection)subject.getPrincipals()).getUserID();
				}	
			}
			
			throw new AccessException("Subject not authenticated");
		}
		catch (ShiroException e)
		{
			throw new AccessException(e.getMessage());
		}
	}
	
	
	public static  <V extends Realm> V getRealm(Class< ? extends Realm> c)
	{
		return getRealm(SecurityUtils.getSecurityManager(), c);
	}
	
	@SuppressWarnings("unchecked")
	public static  <V extends Realm> V getRealm(SecurityManager sm, Class< ? extends Realm> c)
	{
		if ( sm instanceof RealmSecurityManager )
		{
			
			Collection<Realm> realms = ((RealmSecurityManager)sm).getRealms();
			if ( realms != null)
			{
				for ( Realm realm : realms)
				{
					if (c.isAssignableFrom(realm.getClass()))
					{
						return (V) realm;
					}
				}
			}
		}
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public static  <V extends Realm> List<V> getAllRealms(SecurityManager sm, Class<? extends Realm> c)
	{
		List<V> ret = new ArrayList<V>();
		if ( sm instanceof RealmSecurityManager )
		{
			
			Collection<Realm> realms = ((RealmSecurityManager)sm).getRealms();
			if ( realms != null)
			{
				for ( Realm realm : realms)
				{
					if (c.isAssignableFrom(realm.getClass()))
					{
						ret.add((V) realm);
					}
				}
			}
		}
		
		return ret;
	}
	
	public static String subjectDomainID()
			throws AccessException
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			
			if (subject.isAuthenticated())
			{
				if (subject.getPrincipals() instanceof DomainPrincipalCollection)
				{
					return ((DomainPrincipalCollection)subject.getPrincipals()).getDomainID();
				}	
			}
			
			throw new AccessException("Subject not authenticated");
		}
		catch (ShiroException e)
		{
			throw new AccessException(e.getMessage());
		}
	}
	
	
	public static String subjectSessionID()
			throws AccessException
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			subject.getSession().getId().toString();
			
			if (subject.isAuthenticated())
			{
				return subject.getSession().getId().toString();
			}
			
			throw new AccessException("Subject not authenticated");
		}
		catch (ShiroException e)
		{
			throw new AccessException(e.getMessage());
		}
	}
	
	
	
	
	
	public static String subjectApplicationID()
		throws AccessException
	{
		try
		{
			Subject subject = SecurityUtils.getSubject();
			
			if (subject.isAuthenticated())
			{
				if (subject.getPrincipals() instanceof DomainPrincipalCollection)
				{
					return ((DomainPrincipalCollection)subject.getPrincipals()).getApplicationID();
				}	
			}
			
			throw new AccessException("Subject not authenticated");
		}
		catch (ShiroException e)
		{
			throw new AccessException(e.getMessage());
		}
	}	
	
	
	
	
	public static SecurityManager loadSecurityManager(String shiroInitFile)
	{
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(shiroInitFile);
        log.info("Class:"+ factory.getClass());
        return factory.getInstance();
	}
	
	
	public static SecurityManager loadSecurityManager(InputStream is)
	{
		Ini ini = new Ini();
		ini.load(is);
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(ini);
        log.info("Class:"+ factory.getClass());
        return factory.getInstance();
	}
	
	
	
	public static void checkPermission(String permission, ShiroTokenReplacement str)
			throws NullPointerException, AccessException
	{
		checkPermission( SecurityUtils.getSubject(), permission, str);
	}
	
	public static void checkPermission(Subject subject, String permission, ShiroTokenReplacement str)
		throws NullPointerException, AccessException
	{
		SharedUtil.checkIfNulls("Null parameters not allowed", subject, permission, str);
		
		
		permission = str.replace(permission, (String) subject.getPrincipal());
		{
			try
			{
				subject.checkPermission(SharedStringUtil.toLowerCase(permission));
			}
			catch ( ShiroException e)
			{
				
				throw new AccessException(e.getMessage());
			}
		}
	}
	


	
	
	public static void checkPermissions(String ... permissions) 
			throws NullPointerException, AccessException
	{
		checkPermissions (SecurityUtils.getSubject(), permissions);
		
	}
	
	public static void checkPermissions(Subject subject, String ... permissions)
			throws NullPointerException, AccessException
	{
		SharedUtil.checkIfNulls("Null parameters not allowed", subject, permissions);
		for ( String permission : permissions)
		{
			try
			{
				subject.checkPermission( SharedStringUtil.toLowerCase(permission));
			}
			catch ( ShiroException e)
			{
				throw new AccessException( e.getMessage());
			}
		}
	}
	
	
	
	public static boolean isPermitted(String permission) 
			throws NullPointerException, AccessException
	{
		return isPermitted(SecurityUtils.getSubject(), permission);
		
	}
	
	
	public static ShiroNVEntityCRUDs assignCRUDs(NVEntity nve, CRUD ...cruds)
	{
		return assignCRUDs(nve.getReferenceID(), cruds);
		
	}
	
	public static ShiroNVEntityCRUDs assignCRUDs(String refID, CRUD ...cruds)
	{
		refID = SharedStringUtil.trimOrNull(refID);
		SharedUtil.checkIfNulls("Null Parameter", refID, cruds);
		Set<CRUD> set = new ConcurrentSkipListSet<CRUD>();
		for(CRUD c : cruds)
		{
			if(c != null)
			{
				set.add(c);
			}
		}
		
		if(set.size() == 0)
		{
			throw new IllegalArgumentException("Emtpy crud array");
		}
		
		ShiroNVEntityCRUDs ret = new ShiroNVEntityCRUDs();
		ret.setReferenceID(refID);
		ret.setValue(ShiroNVEntityCRUDs.Params.CRUDS, new ArrayList<CRUD>(set));
		
		return ret;
		
	}
	
	
	
	
	public static boolean isPermitted(Subject subject, String permission)
		throws NullPointerException, AccessException
	{
		SharedUtil.checkIfNulls("Null parameters not allowed", subject, permission);
		return subject.isPermitted( SharedStringUtil.toLowerCase(permission));
	}
	
	
	public static Object lookupSessionAttribute(Object key)
	{
		return lookupSessionAttribute(SecurityUtils.getSubject(), key);
	}
	
	public static Object lookupSessionAttribute(Subject subject, Object key)
	{
		if (key != null)
		{
			Session session = subject.getSession();
			if (session != null)
			{
				return session.getAttribute( key);
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * Create subject based on parametrazied security manager
	 * @param sm
	 * @return
	 */
	public static Subject getSubject(SecurityManager sm) 
	 {
		// need to check with session context if the actual used it found 
        Subject subject = ThreadContext.getSubject();
        if (subject == null)
        {
            subject = (new Subject.Builder(sm)).buildSubject();
            ThreadContext.bind(subject);
        }
        return subject;
	 }
	
}
