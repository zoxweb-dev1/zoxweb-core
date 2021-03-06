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

/**
 * The SimpleQueueInterface is a FIFO queue interface. It is used to store non null Objects only.
 */

public interface SimpleQueueInterface<O>
{
	/**
	 * Contract to clear the queue content.
	 */
	public void clear();

	

	/**
	 * @return the size of the queue.
	 */
	public int size();

	/**
	 * Contract to queue an object, the object can be null.
	 * 
	 * @param toQueue the object
	 * 
	 */
	public void queue(O toQueue) throws NullPointerException;
	

	/**
	 * Contract to dequeue the first object in the queue, null if queue is empty.
	 */
	public O dequeue();

	/**
	 * Check if the queue is empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty(); 
	
	/**
	 * @return the total number of object queued
	 */
	public long totalQueued();
	
	
	/**
	 * @return the total number of object dequeued
	 */
	public long totalDequeued();
	
	
	/**
	 * Return true if the queue contains o.
	 * @param o
	 * @return
	 */
	public boolean contains(O o);
} 
