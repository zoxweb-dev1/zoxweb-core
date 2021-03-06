/*
 * Copyright (c) 2012-Jul 24, 2014 ZoxWeb.com LLC.
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
package org.zoxweb;

import org.zoxweb.shared.filters.NotNullOrEmpty;

/**
 * @author mzebib
 *
 */
public class NotNullOrEmptyTest 
{
	public static void main(String[] args)
	{
		NotNullOrEmpty nullOrEmptyFilter = NotNullOrEmpty.SINGLETON;
		
		String [] list = {" ", "example", null, "test"};
		
		System.out.println("Testing null or empty strings: ");
		for (int i = 0; i < list.length; i++)
		{
			
			try
			{
				
				System.out.println("\"" + nullOrEmptyFilter.validate(list[i]) + "\" Valid");
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("\"" + list[i] + "\" Invalid");
			}

		}
		
	}
	
}
