/*
 * Copyright (c) 2012-Dec 3, 2015 ZoxWeb.com LLC.
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
package org.zoxweb;



import org.zoxweb.server.shiro.ShiroUtil;
import org.zoxweb.shared.data.shiro.ShiroNVEntityCRUDs;
import org.zoxweb.shared.data.shiro.ShiroPermissionDAO;
import org.zoxweb.shared.data.shiro.ShiroRoleDAO;
import org.zoxweb.shared.util.CRUD;

/**
 * @author mzebib
 *
 */
public class ShiroRoleDAOTest 
{

	public static void main(String[] args)
	{
		ShiroRoleDAO role = new ShiroRoleDAO();
		role.setName("Role 1");
		role.setDomainID("zoxweb.com");
		
		int index = 0;
		
		ShiroPermissionDAO permission = new ShiroPermissionDAO();
		permission.setName("Permission 1");
		permission.setPermissionPattern("5418541");
		permission.setReferenceID("" + index++);
		
		role.getPermissions().add(permission);
		
		permission = new ShiroPermissionDAO();
		permission.setName("Permission 2");
		permission.setPermissionPattern("5418541");
		permission.setReferenceID("" + index++);
		
		role.getPermissions().add(permission);
		
		permission = new ShiroPermissionDAO();
		permission.setName("Permission 3");
		permission.setPermissionPattern("5418541");
		permission.setReferenceID("" + index++);
		
		role.getPermissions().add(permission);
		
		permission = new ShiroPermissionDAO();
		permission.setName("Permission 4");
		permission.setPermissionPattern("5418541");
		permission.setReferenceID("" + index++);
		
		role.getPermissions().add(permission);		
		role.getPermissions().add(permission);
		
		System.out.println("Permissions Size: " + role.getPermissions().size());
		System.out.println("Role: " + role);
		
		ShiroNVEntityCRUDs nvCRUDs = ShiroUtil.assignCRUDs("XXXXX", CRUD.READ, CRUD.READ, CRUD.UPDATE);
		
		System.out.println(nvCRUDs.getCRUDs());
		
		for(CRUD c : CRUD.values())
		{
			System.out.println(c + " permission " + nvCRUDs.isPermitted(c));
		}
		
		
		
		
	}

}
