/*
 * Copyright (c) 2012-Oct 14, 2015 ZoxWeb.com LLC.
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
package org.zoxweb.server.shiro;


import java.util.HashSet;
import java.util.Iterator;

import java.util.logging.Logger;

import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * @author mnael
 *
 */
public class ZWEhCacheManager 
extends EhCacheManager 
{
	
	private static final transient Logger log = Logger.getLogger(ZWEhCacheManager.class.getName());
	
	private static final HashSet<EhCacheManager> CACHE_SET = new HashSet<EhCacheManager>();
	
	public ZWEhCacheManager()
	{
		super();
		synchronized(CACHE_SET)
		{
			CACHE_SET.add(this);
		}
		
		log.info("Created set size: " + CACHE_SET.size());
		
	}
	

	
	
	public static void destroyAll()
	{
		synchronized(CACHE_SET)
		{
			log.info("Started destroy all " + CACHE_SET.size() + " to be destryed.");
		
			CACHE_SET.iterator();
			
			Iterator<EhCacheManager> it = CACHE_SET.iterator();
			while(it.hasNext())
			{
				try
				{
					EhCacheManager ecm = it.next();
					ecm.destroy();
					log.info("Destroyed:" + ecm);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			CACHE_SET.clear();
			log.info("Finished destroy all left size: " + CACHE_SET.size());
		}
	}
	
}
