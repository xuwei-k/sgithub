/*
 * Copyright 2010 Nabeel Mukhtar 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */
package com.github.api.v2.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Team.
 */
public class Team extends SchemaEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9155892708485181542L;
	
	/** The id. */
	private String id;
	
	/** The name. */
	private String name;
	
	/** The permission. */
	private Permission permission;
	
	/** The repo names. */
	private List<String> repoNames = new ArrayList<String>();
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the permission.
	 * 
	 * @return the permission
	 */
	public Permission getPermission() {
		return permission;
	}

	/**
	 * Sets the permission.
	 * 
	 * @param permission
	 *            the new permission
	 */
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	/**
	 * Gets the repo names.
	 * 
	 * @return the repo names
	 */
	public List<String> getRepoNames() {
		return repoNames;
	}

	/**
	 * Sets the repo names.
	 * 
	 * @param repoNames
	 *            the new repo names
	 */
	public void setRepoNames(List<String> repoNames) {
		this.repoNames = repoNames;
	}
}
