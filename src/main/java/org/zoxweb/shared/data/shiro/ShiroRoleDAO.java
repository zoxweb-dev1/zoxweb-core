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
package org.zoxweb.shared.data.shiro;

import java.util.List;

import org.zoxweb.shared.util.ArrayValues;
import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.NVConfig;
import org.zoxweb.shared.util.NVConfigEntity;
import org.zoxweb.shared.util.NVConfigEntity.ArrayType;
import org.zoxweb.shared.util.NVConfigEntityLocal;
import org.zoxweb.shared.util.NVConfigManager;
import org.zoxweb.shared.util.NVEntity;
import org.zoxweb.shared.util.SharedUtil;

@SuppressWarnings("serial")
public class ShiroRoleDAO
	extends ShiroDomainDAO
{
	
	public enum Params
		implements GetNVConfig
	{
		
		PERMISSIONS(NVConfigManager.createNVConfigEntity("permissions", "The permissions associated with the role.", "Permissions", false, true, ShiroPermissionDAO.class, ArrayType.REFERENCE_ID_MAP)),
	
		;
		
		private final NVConfig cType;
		
		Params(NVConfig c)
		{
			cType = c;
		}
		
		public NVConfig getNVConfig() 
		{
			return cType;
		}
	}
	
	public static final NVConfigEntity NVC_SHIRO_ROLE_DAO = new NVConfigEntityLocal("shiro_role_dao", "Shiro role dao object" , "ShiroRoleDAO", 
			false, true, false, false, ShiroRoleDAO.class, SharedUtil.extractNVConfigs(Params.values()), null, false, ShiroDomainDAO.NVC_SHIRO_DOMAIN_DAO);
	
	public ShiroRoleDAO()
	{
		super(NVC_SHIRO_ROLE_DAO);
	}
	
	public ShiroRoleDAO(String domainID, String roleName, String description)
	{
		this();
		setDomainID(domainID);
		setName( roleName);
		setDescription(description);
	}
	
//	public boolean equals(Object o)
//	{
//		if (this == o)
//		{
//			return true;
//		}
//			
//		if (o != null && o instanceof ShiroRoleDAO)
//		{
//			ShiroRoleDAO to = (ShiroRoleDAO) o;
////			if (SharedUtil.referenceIDToLong(this) == 0 && SharedUtil.referenceIDToLong(to) == 0)
////			{
////				return toCanonicalID().equals(to);
////			}
////			if (getReferenceID() == to.getReferenceID())
////			{
////				return true;
////			}
//			
//			if (to.getReferenceID() != null && getReferenceID() != null)
//			{
//				return getReferenceID().equals(to.getReferenceID());
//			}
//			
//		}
//		
//		return false;
//	}
	
	@Override
	public String toCanonicalID()
	{
		return SharedUtil.toCanonicalID(CAN_ID_SEP, getDomainID(), getName());
	}
	
	@SuppressWarnings("unchecked")
	public ArrayValues<NVEntity> getPermissions()
	{
		return (ArrayValues<NVEntity>) lookup(Params.PERMISSIONS);
	}
	
	public void setPermissions(ArrayValues<NVEntity> values)
	{
		getPermissions().add(values.values(), true);
	}
	
	public void setPermissions(List<NVEntity> values)
	{
		getPermissions().add(values.toArray(new NVEntity[0]), true);
	}
	
}