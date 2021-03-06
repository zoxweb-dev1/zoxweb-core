/*
 * Copyright (c) 2012-Oct 29, 2015 ZoxWeb.com LLC.
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

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;

/**
 * This class swap the current subject with the subject to swap with the current execution thread.
 * It is via the constructor, if the toSwapWith is null nothing is done
 * @author mnael
 *
 */
public class SubjectSwap
	implements AutoCloseable
{
	private final SubjectThreadState  sts;
	
	public SubjectSwap(Subject toSwapWith)
	{
		if (toSwapWith != null)
		{
			sts = new SubjectThreadState(toSwapWith);
			sts.bind();
		}
		else
		{
			sts = null;
		}
	}

	/**
	 * Restore the previous subject context
	 */
	@Override
	public void close() 
	{
		if (sts != null)
			sts.restore();
	}
}
