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
package org.zoxweb;

import java.io.IOException;

import org.zoxweb.server.util.GSONUtil;
import org.zoxweb.shared.data.PhoneDAO;
import org.zoxweb.shared.filters.PhoneNumberFilterOLD;


/**
 * @author mzebib
 *
 */
public class PhoneTest 
{

	public static void main(String[] args) throws IOException
	{
		PhoneDAO phone = new PhoneDAO();
		
		phone.setPhoneType("Mobile");
		phone.setCountryCode("1");
		phone.setAreaCode("310");
		phone.setNumber("3027883");
		
		System.out.println(GSONUtil.toJSON(phone, true));
		
		
	
//		String str = "+1(310)   -302-7883";
//		String p = str.replaceAll("[\\s-()+]", "");
//		System.out.println(p);
//		
//		
//		
//		String ext = "3103027883x12";
//		String[] n = ext.split("[extx]+");
//		System.out.println(n[1]);
		
		
		String[] numbers = {"+1-310-3027883ext55", "+650-429-8422", "1-310-3027883x44", null, "", "+1-302988", ""};
		PhoneNumberFilterOLD filter = new PhoneNumberFilterOLD();
		PhoneDAO phone2 = new PhoneDAO();
				
		for(int i = 0; i < numbers.length; i++ )
		{
			try
			{
				phone2 = filter.validate(numbers[i]);
				System.out.println(filter.isValid(numbers[i]));
				System.out.println(GSONUtil.toJSON(phone2, true));
				System.out.println(phone2.toCanonicalID());
			}
			
			catch(Exception e)
			{
				System.err.println(numbers[i]);
				e.printStackTrace();
			}

		}
		
		
		
		
	}
	
}
