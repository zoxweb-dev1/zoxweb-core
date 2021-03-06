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
package org.zoxweb.server.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zoxweb.server.task.RunnableTask;
import org.zoxweb.server.task.TaskEvent;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
abstract public class SubjectRunnableTask
extends RunnableTask
{
	final protected Subject subject;
	
	protected SubjectRunnableTask()
	{
		this(SecurityUtils.getSubject());
	}
	
	protected SubjectRunnableTask(Subject subject)
	{
		this.subject = subject;
		
	}
	public void executeTask(TaskEvent event) 
	{
		this.te = event;
		if (subject != null)
			subject.execute(this);
		else
			run();
		
	}


	

}
