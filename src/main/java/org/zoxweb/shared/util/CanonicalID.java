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
package org.zoxweb.shared.util;

import java.io.Serializable;

/**
 * Canonical ID is representation of the implementation object. It can be used to uniquely identify an object 
 * within its namespace or to represent the object properties or data in a standardized canonical form.
 * @author mnael
 *
 */
public interface CanonicalID  
	extends Serializable
{
	/**
	 * Convert the implementing object in its canonical form.
	 * @return text identification of the object
	 */
	public String toCanonicalID();
}
