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

import java.util.zip.ZipInputStream
import com.github.api.v2.schema.Key
import com.github.api.v2.schema.Language
import com.github.api.v2.schema.Repository
import com.github.api.v2.schema.User
import com.github.api.v2.schema.Repository.Visibility
import java.util.{Map => jMap, List => jList}

/**
 * The Interface RepositoryService.
 */
trait RepositoryService extends GitHubService {
  /**
   * Search repositories.
   *
   * @param query
   *            the query
   *
   * @return the list< repository>
   */
  def searchRepositories(query: String): jList[Repository]

  /**
   * Search repositories.
   *
   * @param query
   *            the query
   * @param language
   *            the language
   *
   * @return the list< repository>
   */
  def searchRepositories(query: String, language: Language): jList[Repository]

  /**
   * Search repositories.
   *
   * @param query
   *            the query
   * @param pageNumber
   *            the page number
   *
   * @return the list< repository>
   */
  def searchRepositories(query: String, pageNumber: Int): jList[Repository]

  /**
   * Search repositories.
   *
   * @param query
   *            the query
   * @param language
   *            the language
   * @param pageNumber
   *            the page number
   *
   * @return the list< repository>
   */
  def searchRepositories(query: String, language: Language, pageNumber: Int): jList[Repository]

  /**
   * Gets the repository.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the repository
   */
  def getRepository(userName: String, repositoryName: String): Repository

  /**
   * Update repository.
   *
   * @param repository
   *            the repository
   */
  def updateRepository(repository: Repository): Unit

  /**
   * Gets the repositories.
   *
   * @param userName
   *            the user name
   *
   * @return the repositories
   */
  def getRepositories(userName: String): jList[Repository]

  /**
   * Watch repository.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   */
  def watchRepository(userName: String, repositoryName: String): Unit

  /**
   * Unwatch repository.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   */
  def unwatchRepository(userName: String, repositoryName: String): Unit

  /**
   * Fork repository.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the repository
   */
  def forkRepository(userName: String, repositoryName: String): Repository

  /**
   * Creates the repository.
   *
   * @param name
   *            the name
   * @param description
   *            the description
   * @param homePage
   *            the home page
   * @param visibility
   *            the visibility
   *
   * @return the repository
   */
  def createRepository(name: String, description: String, homePage: String, visibility: Repository.Visibility): Repository

  /**
   * Delete repository.
   *
   * @param repositoryName
   *            the repository name
   */
  def deleteRepository(repositoryName: String): Unit

  /**
   * Change visibility.
   *
   * @param repositoryName
   *            the repository name
   * @param visibility
   *            the visibility
   */
  def changeVisibility(repositoryName: String, visibility: Repository.Visibility): Unit

  /**
   * Gets the deploy keys.
   *
   * @param repositoryName
   *            the repository name
   *
   * @return the deploy keys
   */
  def getDeployKeys(repositoryName: String): jList[Key]

  /**
   * Adds the deploy key.
   *
   * @param repositoryName
   *            the repository name
   * @param title
   *            the title
   * @param key
   *            the key
   *
   * @return the list< key>
   */
  def addDeployKey(repositoryName: String, title: String, key: String): jList[Key]

  /**
   * Removes the deploy key.
   *
   * @param repository
   *            the repository
   * @param id
   *            the id
   */
  def removeDeployKey(repository: String, id: String): Unit

  /**
   * Gets the collaborators.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the collaborators
   */
  def getCollaborators(userName: String, repositoryName: String): jList[String]

  /**
   * Adds the collaborator.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param collaboratorName
   *            the collaborator name
   */
  def addCollaborator(userName: String, repositoryName: String, collaboratorName: String): Unit

  /**
   * Removes the collaborator.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param collaboratorName
   *            the collaborator name
   */
  def removeCollaborator(userName: String, repositoryName: String, collaboratorName: String): Unit

  /**
   * Gets the pushable repositories.
   *
   * @return the pushable repositories
   */
  def getPushableRepositories: jList[Repository]

  /**
   * Gets the contributors.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the contributors
   */
  def getContributors(userName: String, repositoryName: String): jList[User]

  /**
   * Gets the watchers.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the watchers
   */
  def getWatchers(userName: String, repositoryName: String): jList[String]

  /**
   * Gets the forks.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the forks
   */
  def getForks(userName: String, repositoryName: String): jList[Repository]

  /**
   * Gets the language breakdown.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the language breakdown
   */
  def getLanguageBreakdown(userName: String, repositoryName: String): jMap[Language, java.lang.Long]

  /**
   * Gets the tags.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the tags
   */
  def getTags(userName: String, repositoryName: String): jMap[String, String]

  /**
   * Gets the branches.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the branches
   */
  def getBranches(userName: String, repositoryName: String): jMap[String, String]

  /**
   * Gets the repository archive.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param branchName
   *            the branch name
   *
   * @return the repository archive
   */
  def getRepositoryArchive(userName: String, repositoryName: String, branchName: String): ZipInputStream
}