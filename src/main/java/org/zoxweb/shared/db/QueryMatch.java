/*
 * Copyright (c) 2012-May 27, 2014 ZoxWeb.com LLC.
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
package org.zoxweb.shared.db;

import org.zoxweb.shared.util.GetNVConfig;
import org.zoxweb.shared.util.GetName;
import org.zoxweb.shared.util.SetNameValue;
import org.zoxweb.shared.util.SharedStringUtil;
import org.zoxweb.shared.util.SharedUtil;
import org.zoxweb.shared.util.Const.RelationalOperator;

/**
 * This class declares query criteria including: name, value, and 
 * relational operator.
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class QueryMatch<V>
	implements SetNameValue<V>, QueryMarker
{
	private String name;
	private V value;
	private RelationalOperator operator;
	
	


	/**
	 * This is the default constructor.
	 */
	public QueryMatch()
	{
		
	}
	
	/**
	 * This constructor instantiates QueryMatch object based on name, value, and operator
	 * parameters.
	 * @param name
	 * @param value
	 * @param operator
	 */
	
	public QueryMatch(String name, V value, RelationalOperator operator)
	{
		setName(name);
		setValue(value);
		setOperator(operator);
	}
	
	
	public QueryMatch(RelationalOperator operator, V value, String ...names)
	{
		setName(SharedUtil.toCanonicalID('.', (Object[])names));
		setValue(value);
		setOperator(operator);
	}
	
	
	public QueryMatch(RelationalOperator operator, V value, GetName ...names)
	{
		StringBuilder name = new StringBuilder();
		for (int i=0; i < names.length ; i++)
		{
			if (names[i] != null)
			{
				if ( name.length() > 0)
				{
					name.append('.');
				}
				name.append(names[i].getName());
			}
		}
		
		
		setName(name.toString());
		setValue(value);
		setOperator(operator);
	}
	
	
	public QueryMatch(RelationalOperator operator, V value, GetNVConfig ...gnvs)
	{
		StringBuilder name = new StringBuilder();
		for (int i=0; i < gnvs.length ; i++)
		{
			if (gnvs[i] != null)
			{
				if ( name.length() > 0)
				{
					name.append('.');
				}
				name.append(gnvs[i].getNVConfig().getName());
			}
		}
		
		
		setName(name.toString());
		setValue(value);
		setOperator(operator);
	}
	
	
	
	/**
	 * This method returns the name.
	 * @return
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * This method sets the name.
	 * @param name
	 */
	@Override
	public void setName(String name) 
	{
		if(SharedStringUtil.isEmpty(name))
		{
			throw new NullPointerException("Null name or empty name");
		}
		this.name = name;
		
	}
		
	/**
	 * This method returns the relational operator.
	 * @return
	 */
	public RelationalOperator getOperator() 
	{
		return operator;
	}
	
	/**
	 * This method sets the relational operator.
	 * @param operator
	 */
	public void setOperator(RelationalOperator operator)
	{
		this.operator = operator;
	}

	/**
	 * This method returns the value.
	 * @return
	 */
	@Override
	public V getValue() 
	{
		return value;
	}
	
	public String toString()
	{
		return getName() + ":" + getOperator() + ":" + getValue();
	}
	
	/**
	 * This method sets the value.
	 * @param value
	 */
	@Override
	public void setValue(V value) 
	{
		this.value = value;
	}

	public boolean isCanonicalName()
	{
		return getName().indexOf('.') != -1;
	}

}
