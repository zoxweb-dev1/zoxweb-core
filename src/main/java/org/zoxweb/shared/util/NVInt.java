/*
 * Copyright (c) 2012-2014 ZoxWeb.com LLC.
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

/**
 * This class declares NVBase of integer type.
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class NVInt 
	extends NVBase<Integer> 
{
	/**
	 * Default constructor used for Java Bean Compiler.
	 */
	public NVInt()
	{
		
	}
	
	/**
	 * This constructor instantiates NVInt based on name and value.
	 * @param name
	 * @param val
	 */
	public NVInt(String name, int val)
	{
		super(name, val);
	}
}
