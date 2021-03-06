/*
 * Copyright (c) 2012-2015 ZoxWeb.com LLC.
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
package org.zoxweb.client.data.crypto;

import org.zoxweb.shared.crypto.CryptoConst;
import org.zoxweb.shared.crypto.CryptoInterface;
import org.zoxweb.shared.security.AccessSecurityException;
import org.zoxweb.shared.util.SharedStringUtil;
import org.zoxweb.shared.util.SharedUtil;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public class CryptoClient
implements CryptoInterface
{
	final public static CryptoInterface SINGLETON = new CryptoClient();
	
	protected CryptoClient()
	{
		
	}

	/**
	 *
	 */
	@Override
	public byte[] hash(String mdAlgo, byte[]... tokens)
			throws NullPointerException, AccessSecurityException
	{
		CryptoConst.MDType mdType = CryptoConst.MDType.lookup(mdAlgo);
		SharedUtil.checkIfNulls("MD type not found", mdType);
		StringBuilder sb = new StringBuilder();
		switch(mdType)
		{
		case MD5:
			for (byte[] array : tokens)
			{
				sb.append(SharedStringUtil.toString(array));
			}
			return SharedStringUtil.hexToBytes(hashMD5(sb.toString()));
		case SHA_256:
			for (byte[] array : tokens)
			{
				sb.append(SharedStringUtil.toString(array));
			}
			return SharedStringUtil.hexToBytes(hashSHA256(sb.toString()));
			
			default:
				throw new AccessSecurityException("Digest not supported " + mdType);
		}
		
		
	
	}

	/**
	 * @see org.zoxweb.shared.crypto.CryptoInterface#hash(java.lang.String, java.lang.String[])
	 */
	@Override
	public byte[] hash(String mdAlgo, String... tokens)
			throws AccessSecurityException
	{
		CryptoConst.MDType mdType = CryptoConst.MDType.lookup(mdAlgo);
		SharedUtil.checkIfNulls("MD type not found", mdType);
		StringBuilder sb = new StringBuilder();
		switch(mdType)
		{
		case MD5:
			for (String str : tokens)
			{
				sb.append(str);
			}
			return SharedStringUtil.hexToBytes(hashMD5(sb.toString()));
		case SHA_256:
			for (String str : tokens)
			{
				sb.append(str);
			}
			return SharedStringUtil.hexToBytes(hashSHA256(sb.toString()));
			
			default:
				throw new AccessSecurityException("Digest not supported " + mdType);
		}
	}
	
	
	public static native String hashSHA256(String ptext)
    /*-{
     	var bitArray = $wnd.sjcl.hash.sha256.hash(ptext);
     	return $wnd.sjcl.codec.hex.fromBits(bitArray);  
     }-*/;
	
	
	public static native String hashMD5(String ptext)
    /*-{
     	return $wnd.hex_md5(ptext);
     }-*/;
	

}
