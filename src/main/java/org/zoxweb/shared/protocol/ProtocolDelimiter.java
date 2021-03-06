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
package org.zoxweb.shared.protocol;

import org.zoxweb.shared.util.GetNameValue;

/**
 * This enum has a set of punctuation marks and implements 
 * the getName and getValue methods of the GetNameValue class.
 * @author mzebib
 *
 */
public enum ProtocolDelimiter
	implements GetNameValue<String>
{
	COLON(":"),
	COLON_PATH_ABEMPTY("://"),
	CRLF("\r\n"),
	CRLFCRLF("\r\n\r\n"),
	DOUBLE_QUOTE("\""),
	EQUAL("="),
	QUESTION_MARK("?"),
	SEMICOLON(";"),
	SPACE(" "),
	
	
	;
	
	private final String value;
	private final byte bytes[];
	
	/**
	 * This constructor builds ProtocolDelimiter 
	 * object based on given inputs.
	 * @param val
	 */
	ProtocolDelimiter(String val)
	{
		value = val;
		bytes = value.getBytes();
	}
	
	/**
	 * This method returns string name.
	 */
	public final String getName() 
	{
		return name();
	}
	
	/**
	 * This method returns string value.
	 */
	public String toString()
	{
		return value;
	}

	/**
	 * This method returns string value.
	 */
	public final String getValue() 
	{
		return value;
	}
	
	/**
	 * This method returns byte array.
	 * @return
	 */
	public final byte[] getBytes()
	{
		return bytes;
	}
		
}
