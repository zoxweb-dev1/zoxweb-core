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

import java.util.List;

import org.zoxweb.shared.data.DataConst.DocumentStatus;
import org.zoxweb.shared.util.ArrayValues;
import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.NVConfig;
import org.zoxweb.shared.util.NVConfigEntity;
import org.zoxweb.shared.util.NVConfigEntityLocal;
import org.zoxweb.shared.util.NVConfigManager;
import org.zoxweb.shared.util.NVPair;
import org.zoxweb.shared.util.SharedUtil;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
abstract public class DocumentInfoDAO
	extends TimeStampDAO
{
	
	public enum Params
		implements GetNVConfig
	{
		DOCUMENT_STATUS(NVConfigManager.createNVConfig("document_status", "Document status", "DocumentStatus", true, true, DocumentStatus.class)),
		//API_CONFIG_INFO(NVConfigManager.createNVConfigEntity("api_config_info", "The APIConfigInfolocator","APIConfigInfo", true, false, APIConfigInfoDAO.NVC_API_CONFIG_INFO_DAO)),
		API_CONFIG_INFO_REF_ID(NVConfigManager.createNVConfig("api_config_info_ref_id", "The APIConfigLocator", "APIConfigInfoReferenceID", true, false, String.class)),
		IS_LINK(NVConfigManager.createNVConfig("is_link", "True is it is symbolink link", "SymbolicLink", true, false, Boolean.class)),
		HASH_SHA_256(NVConfigManager.createNVConfig("hash_sha_256", "The SHA-256 hash", "HashSha256", true, false, String.class)),
		PROPERTIES(NVConfigManager.createNVConfig("properties", "Document unique properties", "DocumentExtraProperties", false, true, true, false, String[].class, null)),
		
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
	
	public static final NVConfigEntity NVC_DOCUMENT_INFO_DAO = new NVConfigEntityLocal(
																							"document_info_dao", 
																							null, 
																							"DocumentInfoDAO", 
																							true, 
																							false, 
																							false, 
																							false,
																							DocumentInfoDAO.class, 
																							SharedUtil.extractNVConfigs(Params.values()), 
																							null, 
																							false, 
																							TimeStampDAO.NVC_TIME_STAMP_DAO
																						);//,SharedUtil.extractNVConfigs( new Params[]{Params.REFERENCE_ID, Params.NAME, Params.LENGTH}));
	
	
	/**
	 * 
	 * @param nvce
	 */
	protected DocumentInfoDAO(NVConfigEntity nvce)
	{
		super(nvce);
	}
//	
//	/**
//	 * Set the URL location where the file is stored.
//	 * @param location
//	 */
//	public void setAPIConfigInfo(APIConfigInfo aci) 
//	{
//		setValue(Params.API_CONFIG_INFO, aci);
//	}
//	
//	/**
//	 * Get the URL location where the file is stored.
//	 * @return
//	 */
//	public APIConfigInfo getAPIConfigInfo() 
//	{
//		return lookupValue( Params.API_CONFIG_INFO);
//	}
	
	/**
	 * Gets the document status.
	 * @return
	 */
	public DocumentStatus getStatus()
	{
		return lookupValue(Params.DOCUMENT_STATUS);
	}
	
	/**
	 * Sets the document status.
	 * @param status
	 */
	public void setStatus(DocumentStatus status)
	{
		setValue(Params.DOCUMENT_STATUS, status);
	}	
	
	/**
	 * Get the URL location where the document is stored.
	 * @return
	 */
	public String getAPIConfigInfoReferenceID() 
	{
		return lookupValue( Params.API_CONFIG_INFO_REF_ID);
	}
	
	/**
	 * Set the URL location where the document is stored.
	 * @param location
	 */
	public void setAPIConfigInfoReferenceID(String aci) 
	{
		setValue(Params.API_CONFIG_INFO_REF_ID, aci);
	}
	
	/**
	 * Get the document properties.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayValues<NVPair> getProperties()
	{
		return (ArrayValues<NVPair>) lookup(Params.PROPERTIES.getNVConfig().getName());
	}
	
	/**
	 * Set the document properties.
	 * @param properties
	 */
	public void setProperties(ArrayValues<NVPair> properties)
	{
		@SuppressWarnings("unchecked")
		ArrayValues<NVPair> arrayValues = (ArrayValues<NVPair>) lookup(Params.PROPERTIES.getNVConfig().getName());
		arrayValues.add(properties.values(), true);
		//setValue( Params.PROPERTIES, fileProps);
	}
	
	/**
	 * Sets the document properties.
	 * @param properties
	 */
	public void setProperties(List<NVPair> properties)
	{
		@SuppressWarnings("unchecked")
		ArrayValues<NVPair> arrayValues = (ArrayValues<NVPair>) lookup(Params.PROPERTIES.getNVConfig().getName());
		arrayValues.add(properties.toArray(new NVPair[0]), true);
		//setValue( Params.PROPERTIES, fileProps);
	}
	
	/**
	 * Check if link.
	 * @return
	 */
	public boolean isLink()
	{
		return lookupValue(Params.IS_LINK);
	}
	
	/**
	 * Sets if link.
	 * @param link
	 */
	public void setLink(boolean link)
	{
		setValue(Params.IS_LINK, link);
	}
	
	/**
	 * Gets the HashSHA256.
	 * @return
	 */
	public String getHashSHA256()
	{
		return lookupValue(Params.HASH_SHA_256);
	}
	
	/**
	 * Sets the HashSHA256.
	 * @param sha256
	 */
	public void setHashSHA256(String sha256)
	{
		setValue(Params.HASH_SHA_256, sha256);
	}
	
}