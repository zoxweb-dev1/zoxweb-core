package org.zoxweb.server.api.provider.notification.smtp;

import org.zoxweb.shared.filters.ValueFilter;
import org.zoxweb.shared.util.SharedStringUtil;
import org.zoxweb.shared.util.SharedUtil;

/**
 * 
 * @author mzebib
 *
 */
@SuppressWarnings("serial")
public class SMTPMessageIDFilter 
	implements ValueFilter< String[], String> 
{

	
	/**
	 * This variable declares that only one instance of this class can be 
	 * created.
	 */
	public static final SMTPMessageIDFilter SINGLETON = new SMTPMessageIDFilter();
	
	
	/**
	 * The default constructor is declared private to prevent
	 * outside instantiation of this class.
	 */
	private SMTPMessageIDFilter()
	{
		
	}
	
	//Pending Issue: Support more than recipient email!
	public String validate(String[] v) 
			throws NullPointerException, IllegalArgumentException 
	{
		SharedUtil.checkIfNulls("Null parameter", (Object)v);
		if (v.length == 0 )
		{
			throw new IllegalArgumentException("Message id emtpy");
		}
		
		String ret = SharedStringUtil.valueAfterLeftToken(v[0], "<");
		ret = SharedStringUtil.valueBeforeRightToken(ret, ">");
		
		return ret;
	}

	public boolean isValid( String[] v) 
	{
		try
		{
			validate( v);
		}
		catch ( Exception e)
		{
			return false;
		}

		return true;
	}




	@Override
	public String toCanonicalID() 
	{
		return SMTPMessageIDFilter.class.getName();
	}




	

}
