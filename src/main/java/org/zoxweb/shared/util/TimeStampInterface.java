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
package org.zoxweb.shared.util;

import java.io.Serializable;


/**
 * TimeStampInterface
 * 
 */
public interface TimeStampInterface
extends Serializable
{
	/**
	 * Get the time is millis when the file was created or uploaded into a system or domain.
	 * @return
	 */
	public long getCreationTime();
	
	/**
	 * Set the time is millis when the file was created or uploaded into a system or domain.
	 * @param ts
	 */
	public void setCreationTime(long ts);
	
	/**
	 * Get the last time the file was updated.
	 * @return
	 */
	public long getLastTimeUpdated();
	
	/**
	 * Set the last time the file was updated.
	 * @param ts
	 */
	public void setLastTimeUpdated(long ts);
	
	/**
	 * Get the last time the file was read.
	 * @return
	 */
	public long getLastTimeRead();
	
	/**
	 * Set the last time the file was read.
	 * @param ts
	 */
	public void setLastTimeRead(long ts);
	

}
