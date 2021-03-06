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

import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.NVConfig;
import org.zoxweb.shared.util.NVConfigEntity;
import org.zoxweb.shared.util.NVConfigEntityLocal;
import org.zoxweb.shared.util.NVConfigManager;
import org.zoxweb.shared.util.SharedUtil;
import org.zoxweb.shared.util.TimeStampInterface;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
abstract public class TimeStampDAO
	extends SetNameDescriptionDAO
	implements TimeStampInterface
{
	
	public enum Params
		implements GetNVConfig
	{
		CREATION_TS(NVConfigManager.createNVConfig("creation_ts", "Creation timestamp (in millis).", "CreationTS", true, false, false, true, Date.class, null)),
		LAST_UPDATE_TS(NVConfigManager.createNVConfig("last_update_ts", "Last update timestamp (in millis).", "LastUpdateTS", true, false, false, true, Date.class, null)),
		LAST_READ_TS(NVConfigManager.createNVConfig("last_read_ts", "Last read timestamp (in millis).", "LastReadTS", true, false, false, true, Date.class, null)),
		
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
	
	public static final NVConfigEntity NVC_TIME_STAMP_DAO = new NVConfigEntityLocal(
																						"time_stamp_dao", 
																						null, 
																						"TimeStampDAO", 
																						true, 
																						false, 
																						false, 
																						false, 
																						TimeStampDAO.class, 
																						SharedUtil.extractNVConfigs(Params.values()), 
																						null, 
																						false, 
																						SetNameDescriptionDAO.NVC_NAME_DESCRIPTION_DAO
																					);
	
	/**
	 * This constructor instantiates TimeStampDAO based on given NVConfigEntity.
	 * @param nvce
	 */
	protected TimeStampDAO(NVConfigEntity nvce)
	{
		super(nvce);
	}
	
	/**
	 * Gets the time (in milliseconds) when the file was created or uploaded into a system or domain.
	 * @return
	 */
	public long getCreationTime() 
	{
		return lookupValue(Params.CREATION_TS);
	}
	
	/**
	 * Sets the time (in milliseconds) when the file was created or uploaded into a system or domain.
	 * @param ts
	 */
	public void setCreationTime(long ts) 
	{
		setValue(Params.CREATION_TS, ts);
	}
	
	/**
	 * Gets the last time (in milliseconds) the file was updated.
	 * @return
	 */
	public long getLastTimeUpdated() 
	{
		return lookupValue(Params.LAST_UPDATE_TS);
	}
	
	/**
	 * Sets the last time (in milliseconds) the file was updated.
	 * @param ts
	 */
	public void setLastTimeUpdated(long ts) 
	{
		setValue(Params.LAST_UPDATE_TS, ts);
	}
	
	/**
	 * Gets the last time (in milliseconds) the file was read.
	 * @return
	 */
	public long getLastTimeRead() 
	{
		return lookupValue(Params.LAST_READ_TS);
	}
	
	/**
	 * Sets the last time (in milliseconds) the file was read.
	 * @param ts
	 */
	public void setLastTimeRead(long ts) 
	{
		setValue(Params.LAST_READ_TS, ts);
	}
	
}