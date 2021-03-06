/*
 * Copyright (c) 2012-2016 ZoxWeb.com LLC.
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
package org.zoxweb.shared.data;

import java.util.Date;

import org.zoxweb.shared.util.CRUD;
import org.zoxweb.shared.util.Const.ResourceType;
import org.zoxweb.shared.util.NVConfigEntity.ArrayType;
import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.NVConfig;
import org.zoxweb.shared.util.NVConfigEntity;
import org.zoxweb.shared.util.NVConfigEntityLocal;
import org.zoxweb.shared.util.NVConfigManager;
import org.zoxweb.shared.util.NVEntity;
import org.zoxweb.shared.util.SharedUtil;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class UUIDInfoDAO 
	extends TimeStampDAO 
{
	
	public enum Params
		implements GetNVConfig
	{
//		boolean mandatory,
//		  boolean editable,
//		  boolean isUnique,
//		  boolean isHidden,
		ACCESS_COUNTER(NVConfigManager.createNVConfig("access_counter", "The access count(-1) never expire, 0 expired, > 0 still valid","AccessCounter", false, false, false, false, Long.class, null)),
		EXPIRATION_TS(NVConfigManager.createNVConfig("expiration_ts", "Time in millis when the UUID will expire","Expiration", false, false, false, false, Date.class, null)),
		RESOURCE_ID(NVConfigManager.createNVConfig("resource_id", "Resource identifier usually a reference_id","ResourceID", true, false, false, false, String.class, null)),
		RESOURCE_CANONICAL_ID(NVConfigManager.createNVConfig("resource_canonical_id", "The resource canonical ID","ResourceCanonicalID", false, false, false, false, String.class, null)),
		CONTENT(NVConfigManager.createNVConfigEntity("content", "The folder content", "Content", false, true, NVEntity.class, ArrayType.NOT_ARRAY)),
		// till we support persistence maybe change to class name or NVEntity(might be a problem with resource real-time synchronization ??!)
		RESOURCE_TYPE(NVConfigManager.createNVConfig("resource_type", "Resource type","ResourceType", true, false, false, false, ResourceType.class, null)),
		RESOURCE_CRUD(NVConfigManager.createNVConfig("resource_crud", "CRUD permission for UUID","ResourceCRUD", true, false, false, false, CRUD.class, null)),
		SESSION_ID(NVConfigManager.createNVConfig("session_id", "The session id","SessionID", true, false, true, false, String.class, null)),
		
		UUID(NVConfigManager.createNVConfig("uuid", "UUID ","UUID", true, false, true, false, String.class, null)),
		
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
	
	public static final NVConfigEntity NVC_UUID_INFO_DAO = new NVConfigEntityLocal(
																						"uuid_info_dao", 
																						null, 
																						"UUIDInfoDAO", 
																						true, 
																						false, 
																						false, 
																						false, 
																						UUIDInfoDAO.class, 
																						SharedUtil.extractNVConfigs(Params.values()), 
																						null, 
																						false, 
																						TimeStampDAO.NVC_TIME_STAMP_DAO
																					);
	
	
	/**
	 * The default constructor.
	 */
	public UUIDInfoDAO() 
	{
		super(NVC_UUID_INFO_DAO);
	}
	
	/**
	 * Gets expiration time (in milliseconds, -1 for never expires).
	 * @return
	 */
	public long getExpirationTime() 
	{
		return lookupValue(Params.EXPIRATION_TS);
	}
	
	/**
	 * Sets expiration time (in milliseconds, -1 for never expires).
	 * @param ts
	 */
	public void setExpirationTime(long ts) 
	{
		setValue(Params.EXPIRATION_TS, ts);
	}
	
	/**
	 * Gets the access count (in milliseconds, -1 for never expires).
	 * @return
	 */
	public long getAccessCount()
	{
		return lookupValue(Params.ACCESS_COUNTER);
	}
	
	/**
	 * Sets the access count (in milliseconds, -1 for never expires).
	 * @param ts
	 */
	public synchronized void setAccessCount(long ac) 
	{
		setValue(Params.ACCESS_COUNTER, ac);
	}
	
	/**
	 * Gets UUID.
	 * @return
	 */
	public String getUUID() 
	{
		return lookupValue(Params.UUID);
	}
	
	/**
	 * Sets UUID.
	 * @param uuid
	 */
	public void setUUID(String uuid) 
	{
		setValue(Params.UUID, uuid);
	}
	
	/**
	 * Gets resource ID.
	 * @return
	 */
	public String getResourceID()
	{
		return lookupValue(Params.RESOURCE_ID);
	}
	
	/**
	 * Sets resource ID.
	 * @param resourceID
	 */
	public void setResourceID(String resourceID) 
	{
		setValue(Params.RESOURCE_ID, resourceID);
	}
	
	
	/**
	 * Gets resource canonical ID.
	 * @return
	 */
	public String getResourceCanonicalID()
	{
		return lookupValue(Params.RESOURCE_CANONICAL_ID);
	}
	
	/**
	 * Sets resource canonicalID.
	 * @param resourceID
	 */
	public void setResourceCanonicalID(String resourceID) 
	{
		setValue(Params.RESOURCE_CANONICAL_ID, resourceID);
	}
	
	
	
	/**
	 * Get content.
	 * @return
	 */
	public NVEntity getContent()
	{
		return lookupValue(Params.CONTENT);
	}
	
	/**
	 * Set content.
	 * @param resourceID
	 */
	public void setContent(NVEntity content) 
	{
		setValue(Params.CONTENT, content);
	}
	
	/**
	 * Gets resource type.
	 * @return
	 */
	public ResourceType getResourceType()
	{
		return lookupValue(Params.RESOURCE_TYPE);
	}
	
	/**
	 * Sets resource type.
	 * @param resourceType
	 */
	public void setResourceType(ResourceType resourceType) 
	{
		setValue(Params.RESOURCE_TYPE, resourceType);
	}
	
	/**
	 * Gets resource CRUD.
	 * @return
	 */
	public CRUD getResourceCRUD()
	{
		return lookupValue(Params.RESOURCE_CRUD);
	}
	
	/**
	 * Sets resource CRUD.
	 * @param crud
	 */
	public void setResourceCRUD(CRUD crud) 
	{
		setValue(Params.RESOURCE_CRUD, crud);
	}
	
	/**
	 * Gets session ID.
	 * @return
	 */
	public String getSessionID()
	{
		return lookupValue(Params.SESSION_ID);
	}
	
	/**
	 * Sets session ID.
	 * @param sessionID
	 */
	public void setSessionID(String sessionID) 
	{
		setValue(Params.SESSION_ID, sessionID);
	}

}