/*
 * Copyright 2012 ZoxWeb.com LLC.
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
package org.zoxweb.shared.util;

import java.util.ArrayList;
import java.util.List;

import org.zoxweb.shared.filters.ValueFilter;

/**
 * The dynamic enum map class extends NVPairList is used as a
 * dynamic value filter.
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class DynamicEnumMap
	extends NVPairList
	implements ValueFilter<String, String>,
			   AccountID<String>,
			   UserID<String>,
			   SetDescription
{
	
	private boolean ignoreCase = true;
	private boolean staticEnum = false;
	public static final String NAME_PREFIX = "dynamic_enum_map";
	private String accountID;
	private String userID;
	private String description;
	
	/**
	 * The default constructor.
	 */
	public DynamicEnumMap()
	{
		//Call super constructor empty. Do not call super(name, value), 
		//Constructor must be called like this, otherwise an exception
		//will be thrown.
		super();
		value = new ArrayList<NVPair>();
	}
	
	/**
	 * This constructor instantiates DynamicEnumMap based on given name.
	 * @param name
	 */
	public DynamicEnumMap(String name)
	{
		super(name, new ArrayList<NVPair>());
	}
	
	/**
	 * This constructor instantiates DynamicEnumMap based on given enum class.
	 * @param enumClass
	 */
	public DynamicEnumMap(Class<?extends Enum<?>> enumClass)
	{
		this(enumClass.getName(), enumClass.getEnumConstants());
	}
	
	/**
	 * This constructor instantiates DynamicEnumMap based on given name and 
	 * list of NVPair.
	 * @param n
	 * @param v
	 */
	public DynamicEnumMap(String n, List<NVPair> v)
	{
		super(n, (ArrayList<NVPair>) v);
	}
	
	/**
	 * This constructor instantiates DynamicEnumMap based on given name and enums of Enum<?> type.
	 * @param name
	 * @param enums
	 */
	public DynamicEnumMap(String name, Enum<?>... enums)
	{
		super(name, new ArrayList<NVPair>());
	
		if (enums != null)
		{
			for (Enum<?> e: enums)
			{
				addEnumValue(e);
			}
		}
	}
	
	/**
	 * This constructor instantiates DynamicEnumMap based on name and enums of NVPair type.
	 * @param name
	 * @param enums
	 */
	public DynamicEnumMap(String name, NVPair... enums)
	{
		super(name, new ArrayList<NVPair>());
	
		if (enums != null)
		{
			for (NVPair nvp : enums)
			{
				addEnumValue(nvp);
			}
		}
	}
	
	/**
	 * This method is used to validate the given string.
	 * @param v
	 * @return
	 */
	public final String validate(String v) 
			throws NullPointerException, IllegalArgumentException 
	{
		if (v != null)
		{
			for (NVPair nvp : value)
			{
				if ((ignoreCase ? v.equalsIgnoreCase(nvp.getName()) : v.equals(nvp.getName())))
				{
					return nvp.getName();
				}
				else if ((ignoreCase ? v.equalsIgnoreCase(nvp.getValue()) : v.equals(nvp.getValue())))
				{
					return nvp.getName();
				}
			}
		}
		else
		{
			return null;
		}
		
		throw new IllegalArgumentException(v + " is not a valid value");
	}
	
	/**
	 * This method checks whether the given string is valid.
	 * @param v
	 * @return
	 */
	public boolean isValid(String v) 
	{
		if (v != null)
		{
			for (NVPair nvp : value)
			{
				if ((ignoreCase ? v.equalsIgnoreCase(nvp.getName()) : v.equals(nvp.getName())))
				{
					return true;
				}
				else if ((ignoreCase ? v.equalsIgnoreCase(nvp.getValue()) : v.equals(nvp.getValue())))
				{
					return true;
				}
			}
		}
		else
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method adds an enum type value.
	 * @param e
	 */
	public void addEnumValue(Enum<?> e)
	{
		SharedUtil.checkIfNulls("Null enum.", e);
		
		if (e instanceof GetName)
		{
			addEnumValue(new NVPair(e.name(), ((GetName) e).getName()));
		}
		else if (e instanceof GetValue) 
		{
			addEnumValue(new NVPair(e.name(), "" + ((GetValue<?>) e).getValue()));
		}
		else
		{
			addEnumValue(new NVPair(e.name(), (String)null));
		}
		
	}
	
	/**
	 * This method declared synchronized to avoid multi-threading and 
	 * is used add NVPair type enum value. 
	 * @param nvp
	 */
	public synchronized void addEnumValue(NVPair nvp)
	{
		if (!isValid( nvp.getName()))
		{
			value.add(nvp);
		}
	}
	
	/**
	 * This method is used to lookup and return NVPair based on enum name or value.
	 * @param enumNameOrValue
	 * @return
	 */
	public final NVPair lookup(String enumNameOrValue)
	{
		if (enumNameOrValue != null)
		{
			for (NVPair nvp : value)
			{
				if ((ignoreCase ? enumNameOrValue.equalsIgnoreCase(nvp.getName()) : enumNameOrValue.equals(nvp.getName())))
				{
					return nvp;
				}
				else if ((ignoreCase ? enumNameOrValue.equalsIgnoreCase(nvp.getValue()) : enumNameOrValue.equals(nvp.getValue())))
				{
					return nvp;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * This method returns the name.
	 * @return
	 */
	public String toString()
	{
		return name +':'+ SharedUtil.toCanonicalID(',', value.toArray());
	}

	/**
	 * This method returns the ignore case value.
	 * @return
	 */
	public boolean isIgnoreCase()
	{
		return ignoreCase;
	}

	/**
	 * This method sets the ignore case value.
	 * @param ignoreCase
	 */
	public void setIgnoreCase(boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	/**
	 * This method returns the string representation of the class.
	 * @return
	 */
	@Override
	public String toCanonicalID()
	{
		return getName();
	}

	/**
	 * This method returns the static enum value.
	 * @return
	 */
	public boolean isStatic()
	{
		return staticEnum;
	}

	/**
	 * This method sets the enum static value.
	 * @param staticEnum
	 */
	public void setStatic(boolean staticEnum)
	{
		this.staticEnum = staticEnum;
	}
	
	/**
	 * This method is declared synchronized to avoid multi-threading and
	 * is used to set the name.
	 * @param name
	 */
	public synchronized void setName(String name)
	{
		String oldName = this.name;
		
		SharedUtil.checkIfNulls("Null value", name);
		
		name = SharedStringUtil.filterString(name, NAME_PREFIX + ":", NAME_PREFIX);
		
		name = SharedStringUtil.trimOrNull(name);
		
		if (name == null)
		{
			throw new IllegalArgumentException("Invalid name");
		}
		
		this.name = SharedUtil.toCanonicalID(':', NAME_PREFIX, name);
		
		if (oldName != null)
		{
			DynamicEnumMapManager.SINGLETON.deleteDynamicEnumMap(oldName);
		}
		
		DynamicEnumMapManager.SINGLETON.addDynamicEnumMap(this);
	}

	/**
	 * This method returns the user ID.
	 * @return
	 */
	@Override
	public String getUserID() 
	{
		return userID;
	}

	/**
	 * This method sets the user ID.
	 * @param userID
	 */
	@Override
	public void setUserID(String userID)
	{
		this.userID = userID;	
	}

	/**
	 * This method returns the account ID.
	 * @return
	 */
	@Override
	public String getAccountID()
	{
		return accountID;
	}

	/**
	 * This method sets the account ID.
	 * @param accountID
	 */
	@Override
	public void setAccountID(String accountID) 
	{
		this.accountID = accountID;
	}

	/**
	 * This method returns the description.
	 * @return
	 */
	public String getDescription() 
	{
		return description;
	}

	/**
	 * This method sets the description.
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getDisplayName()
	{
		return SharedStringUtil.filterString(getName(), NAME_PREFIX + ":", NAME_PREFIX);
	}
}
