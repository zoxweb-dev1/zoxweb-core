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

import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.NVConfig;
import org.zoxweb.shared.util.NVConfigEntity;
import org.zoxweb.shared.util.NVConfigEntityLocal;
import org.zoxweb.shared.util.NVConfigManager;
import org.zoxweb.shared.util.NVPair;
import org.zoxweb.shared.util.SharedUtil;
import org.zoxweb.shared.util.NVConfigEntity.ArrayType;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class UserInfoDAO 
	extends UserInfoBaseDAO
{
	
	public enum Params
		implements GetNVConfig
	{
		LIST_OF_ALIASES(NVConfigManager.createNVConfig("list_of_aliases", "List of aliases", "ListOfAliases", false, true, String[].class)),
		LIST_OF_CREDIT_CARDS(NVConfigManager.createNVConfigEntity("list_of_credit_cards", "List of credit cards", "ListOfCreditCards", false, true, CreditCardDAO.NVC_CREDIT_CARD_DAO, ArrayType.LIST)),
		
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
	
	public static final NVConfigEntity NVC_USER_INFO_DAO = new NVConfigEntityLocal(
																						"user_info_dao", 
																						null , 
																						"UserInfoDAO", 
																						true, 
																						false, 
																						false, 
																						false, 
																						UserInfoDAO.class, 
																						SharedUtil.extractNVConfigs(Params.values()), 
																						null, 
																						false, 
																						UserInfoBaseDAO.NVC_USER_INFO_BASE_DAO
																					);
																				
	
	/**
	 * This is the default constructor.
	 */
	public UserInfoDAO()
	{
		super(NVC_USER_INFO_DAO);
	}
	
	/**
	 * Gets the list of aliases.
	 * @return
	 */
	public List<NVPair> getListOfAliases() 
	{
		return lookupValue(Params.LIST_OF_ALIASES);
	}
	
	/**
	 * Sets the list of aliases.
	 * @param list
	 */
	public void setListOfAliases(List<NVPair> list)
	{
		setValue(Params.LIST_OF_ALIASES, list);
	}
	
	/**
	 * Gets the list of credit cards.
	 * @return
	 */
	public List<CreditCardDAO> getListOfCreditCards() 
	{
		return lookupValue(Params.LIST_OF_CREDIT_CARDS);
	}
	
	/**
	 * Sets the list of credit cards.
	 * @param list
	 */
	public void setListOfCreditCards(List<CreditCardDAO> list)
	{
		setValue(Params.LIST_OF_CREDIT_CARDS, list);
	}
	
}
