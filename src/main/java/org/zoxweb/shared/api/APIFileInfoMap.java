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
package org.zoxweb.shared.api;

import org.zoxweb.shared.data.FileInfoDAO;




public interface APIFileInfoMap
{
	
	/**
	 * This method returns the original file information.
	 * @return
	 */
	public FileInfoDAO getOriginalFileInfo();


	/**
	 * This method sets the original file information.
	 * @param info
	 */
	public void setOriginalFileInfo(FileInfoDAO info) throws NullPointerException, IllegalArgumentException;
	

	/**
	 * This method returns the remote file information.
	 * @return
	 */
	public FileInfoDAO getRemoteFileInfo();
	

	/**
	 * This method sets the remote file information.
	 * @param info
	 */
	public void setRemoteFileInfo(FileInfoDAO info) throws NullPointerException, IllegalArgumentException;
	
	
	
}
