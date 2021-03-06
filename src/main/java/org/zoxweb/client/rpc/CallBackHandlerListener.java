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

package org.zoxweb.client.rpc;

public interface CallBackHandlerListener 
{
	public void callBackInitiated();
	public void callBackEnded();
	
	
	
	/**
	 * This method is called when a exception is returned by the server.
	 * An implementation example:
	 * <code>
	 * 	AccessException ae = (AccessException) caught;
	 *	if ( ae.getURLRedirect() != null)
	 *	{
	 *		Window.Location.replace( ae.getURLRedirect());
	 *		return;
	 *	}
	 *	else if ( ae.isReloadRequired())
	 * 	{
	 *		Window.Location.reload();
	 *		return;
	 *	}
	 * return true
	 * </code>
	 * @param caught the exception that is returned by the server 
	 * @return true if the error processing to be handled by the caller false to stop error handling propagation
	 */
	public boolean callBackEndedWithException(Throwable caught);
	
}
