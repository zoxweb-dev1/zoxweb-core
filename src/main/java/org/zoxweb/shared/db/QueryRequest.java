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
package org.zoxweb.shared.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zoxweb.shared.util.SetCanonicalID;
import org.zoxweb.shared.util.SharedStringUtil;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
@SuppressWarnings("serial")
public class QueryRequest
implements Serializable,SetCanonicalID
{
	private String canonical_id;
	private int batch_size;
	private List<String> field_names;
	private List<QueryMarker> query;
	

	/**
	 * @see org.zoxweb.shared.util.SetCanonicalID#getCanonicalID()
	 */
	@Override
	public String getCanonicalID()
	{
		// TODO Auto-generated method stub
		return canonical_id;
	}

	/**
	 * Set the request canonical ID, the canonical id can be the class name
	 */
	@Override
	public void setCanonicalID(String id)
	{
		canonical_id = id;
	}

	/**
	 * If the batch size is > 0 than we have batched query
	 * 
	 * @return
	 */
	public int getBatchSize()
	{
		return batch_size;
	}

	public void setBatchSize(int batch_size)
	{
		this.batch_size = batch_size;
	}

	/**
	 * List if field names than the caller is interested in if null all the fields a required
	 * 
	 * @return
	 */
	public List<String> getFieldNames() 
	{
		return field_names;
	}

	public void setFieldNames(List<String> fieldNames)
	{
		this.field_names = fieldNames;
	}
	
	
	public void setFieldNames(String ...fieldNames)
	{
		if (fieldNames != null)
		{
			field_names = new ArrayList<String>();
			for (String fn : fieldNames)
			{
				if (!SharedStringUtil.isEmpty(fn))
					field_names.add(fn);
			}
		}
		else
		{
			this.field_names = null;
		}
	}

	public List<QueryMarker> getQuery()
	{
		return query;
	}

	public void setQuery(List<QueryMarker> query)
	{
		this.query = query;
	}
	
	
	public void setQuery(QueryMarker ...markers)
	{
		if (markers != null)
		{
			query = new ArrayList<QueryMarker>();
			if (markers != null)
			{
				for (QueryMarker qm: markers)
				{
					if (qm != null)
					{
						query.add(qm);
					}
				}
			}
		}
		else
		{
			query = null;
		}
	}

	/**
	 * @see org.zoxweb.shared.util.CanonicalID#toCanonicalID()
	 */
	@Override
	public String toCanonicalID()
	{
		// TODO Auto-generated method stub
		return canonical_id;
	}

}
