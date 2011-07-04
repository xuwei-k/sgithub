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
package com.github.api.v2.services

import java.util.List
import com.github.api.v2.schema.Organization
import com.github.api.v2.schema.Permission
import com.github.api.v2.schema.Repository
import com.github.api.v2.schema.Team
import com.github.api.v2.schema.User

/**
 * The Interface OrganizationService.
 */
abstract trait OrganizationService extends GitHubService {
  /**
   * Gets the organization.
   *
   * @param name
   *            the name
   *
   * @return the organization
   */
  def getOrganization(name: String): Organization

  /**
   * Gets the user organizations.
   *
   * @return the user organizations
   */
  def getUserOrganizations: List[Organization]

  /**
   * Update organization.
   *
   * @param organization
   *            the organization
   */
  def updateOrganization(organization: Organization): Unit

  /**
   * Gets the all organization repositories.
   *
   * @return the all organization repositories
   */
  def getAllOrganizationRepositories: List[Repository]

  /**
   * Gets the public repositories.
   *
   * @param organizationName
   *            the organization name
   *
   * @return the public repositories
   */
  def getPublicRepositories(organizationName: String): List[Repository]

  /**
   * Gets the public members.
   *
   * @param organizationName
   *            the organization name
   *
   * @return the public members
   */
  def getPublicMembers(organizationName: String): List[User]

  /**
   * Gets the owners.
   *
   * @param organizationName
   *            the organization name
   *
   * @return the owners
   */
  def getOwners(organizationName: String): List[User]

  /**
   * Gets the teams.
   *
   * @param organizationName
   *            the organization name
   *
   * @return the teams
   */
  def getTeams(organizationName: String): List[Team]

  /**
   * Creates the team.
   *
   * @param organizationName
   *            the organization name
   * @param teamName
   *            the team name
   * @param permission
   *            the permission
   * @param repoNames
   *            the repo names
   *
   * @return the team
   */
  def createTeam(organizationName: String, teamName: String, permission: Permission, repoNames: List[String]): Team

  /**
   * Gets the team.
   *
   * @param teamId
   *            the team id
   *
   * @return the team
   */
  def getTeam(teamId: String): Team

  /**
   * Update team.
   *
   * @param team
   *            the team
   */
  def updateTeam(team: Team): Unit

  /**
   * Delete team.
   *
   * @param teamId
   *            the team id
   */
  def deleteTeam(teamId: String): Unit

  /**
   * Gets the team members.
   *
   * @param teamId
   *            the team id
   *
   * @return the team members
   */
  def getTeamMembers(teamId: String): List[User]

  /**
   * Adds the team member.
   *
   * @param teamId
   *            the team id
   * @param userName
   *            the user name
   */
  def addTeamMember(teamId: String, userName: String): Unit

  /**
   * Removes the team member.
   *
   * @param teamId
   *            the team id
   * @param userName
   *            the user name
   */
  def removeTeamMember(teamId: String, userName: String): Unit

  /**
   * Gets the team repositories.
   *
   * @param teamId
   *            the team id
   *
   * @return the team repositories
   */
  def getTeamRepositories(teamId: String): List[Repository]

  /**
   * Adds the team repository.
   *
   * @param teamId
   *            the team id
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   */
  def addTeamRepository(teamId: String, userName: String, repositoryName: String): Unit

  /**
   * Removes the team repository.
   *
   * @param teamId
   *            the team id
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   */
  def removeTeamRepository(teamId: String, userName: String, repositoryName: String): Unit
}