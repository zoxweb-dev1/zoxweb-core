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

import java.util.Comparator;

import org.zoxweb.shared.util.Const.RelationalOperator;


/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public interface Appointment
{
	
	public static final Comparator<Appointment> EQUAL_COMPARATOR = new AppointmentComparator();
	public static final Comparator<Appointment> EQUAL_LESS_COMPARATOR = new AppointmentComparator(RelationalOperator.LT);
	public static final Comparator<Appointment> EQUAL_MORE_COMPARATOR = new AppointmentComparator(RelationalOperator.GT);
	
	/**
	 * 
	 * Get the delay time in millis
	 * 
	 * @return
	 */
	public long getDelayInMillis();
	/**
	 * Set the delay time in millis
	 * @param delayInMillis
	 */
	public void setDelayInMillis( long delayInMillis);
	
	/**
	 * Get the expiration in real time
	 * @return
	 */
	public long getExpirationInMillis();
	
	
	public boolean cancel();
	
}
