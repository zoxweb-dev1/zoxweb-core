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
package org.zoxweb.shared.filters;

import org.zoxweb.shared.util.SharedUtil;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class ReplacementFilter implements ValueFilter<String, String> 
{

	private String replace, replacement;
	
	/**
	 * 
	 * @param toReplace
	 * @param replacement
	 */
	public ReplacementFilter(String toReplace, String replacement)
	{
		SharedUtil.checkIfNulls("params can't be null", toReplace, replacement);
		this.replace = toReplace;
		this.replacement = replacement;
	}
	
	/**
	 * 
	 * @param v
	 */
	@Override
	public String validate(String v) throws NullPointerException,
			IllegalArgumentException 
	{
		
		
		if ( v != null)
		{
			v = v.replace(replace, replacement);
		}
		
		
		return v;
	}

	/**
	 * 
	 * @param v
	 */
	@Override
	public boolean isValid(String v) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toCanonicalID() 
	{
		// TODO Auto-generated method stub
		return "static:ValueFilter:ReplacementFilter";
	}

}
