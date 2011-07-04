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
import com.github.api.v2.schema.Key
import com.github.api.v2.schema.Organization
import com.github.api.v2.schema.Repository
import com.github.api.v2.schema.User

/**
 * The Interface UserService.
 */
abstract trait UserService extends GitHubService {
  /**
   * Search users by name.
   *
   * @param name
   *            the name
   *
   * @return the list< user>
   */
  def searchUsersByName(name: String): List[User]

  /**
   * Gets the user by email.
   *
   * @param email
   *            the email
   *
   * @return the user by email
   */
  def getUserByEmail(email: String): User

  /**
   * Gets the user by username.
   *
   * @param userName
   *            the user name
   *
   * @return the user by username
   */
  def getUserByUsername(userName: String): User

  /**
   * Gets the current user.
   *
   * @return the current user
   */
  def getCurrentUser: User

  /**
   * Update user.
   *
   * @param user
   *            the user
   */
  def updateUser(user: User): Unit

  /**
   * Gets the user followers.
   *
   * @param userName
   *            the user name
   *
   * @return the user followers
   */
  def getUserFollowers(userName: String): List[String]

  /**
   * Gets the user following.
   *
   * @param userName
   *            the user name
   *
   * @return the user following
   */
  def getUserFollowing(userName: String): List[String]

  /**
   * Follow user.
   *
   * @param userName
   *            the user name
   */
  def followUser(userName: String): Unit

  /**
   * Unfollow user.
   *
   * @param userName
   *            the user name
   */
  def unfollowUser(userName: String): Unit

  /**
   * Gets the watched repositories.
   *
   * @param userName
   *            the user name
   *
   * @return the watched repositories
   */
  def getWatchedRepositories(userName: String): List[Repository]

  /**
   * Gets the keys.
   *
   * @return the keys
   */
  def getKeys: List[Key]

  /**
   * Adds the key.
   *
   * @param title
   *            the title
   * @param key
   *            the key
   */
  def addKey(title: String, key: String): Unit

  /**
   * Removes the key.
   *
   * @param id
   *            the id
   */
  def removeKey(id: String): Unit

  /**
   * Gets the emails.
   *
   * @return the emails
   */
  def getEmails: List[String]

  /**
   * Adds the email.
   *
   * @param email
   *            the email
   */
  def addEmail(email: String): Unit

  /**
   * Removes the email.
   *
   * @param email
   *            the email
   */
  def removeEmail(email: String): Unit

  /**
   * Gets the user organizations.
   *
   * @param userName
   *            the user name
   *
   * @return the user organizations
   */
  def getUserOrganizations(userName: String): List[Organization]
}