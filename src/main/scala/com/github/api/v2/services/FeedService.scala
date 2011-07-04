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

import com.github.api.v2.schema.Feed

/**
 * The Interface FeedService.
 */
abstract trait FeedService extends GitHubService {
  /**
   * Gets the public user feed.
   *
   * @param userName
   *            the user name
   * @param count
   *            the count
   *
   * @return the public user feed
   */
  def getPublicUserFeed(userName: String, count: Int): Feed

  /**
   * Gets the private user feed.
   *
   * @param userName
   *            the user name
   * @param count
   *            the count
   *
   * @return the private user feed
   */
  def getPrivateUserFeed(userName: String, count: Int): Feed

  /**
   * Gets the commit feed.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param branchName
   *            the branch name
   * @param count
   *            the count
   *
   * @return the commit feed
   */
  def getCommitFeed(userName: String, repositoryName: String, branchName: String, count: Int): Feed

  /**
   * Gets the network feed.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param count
   *            the count
   *
   * @return the network feed
   */
  def getNetworkFeed(userName: String, repositoryName: String, count: Int): Feed

  /**
   * Gets the wiki feed.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param count
   *            the count
   *
   * @return the wiki feed
   */
  def getWikiFeed(userName: String, repositoryName: String, count: Int): Feed

  /**
   * Gets the public timeline feed.
   *
   * @param count
   *            the count
   *
   * @return the public timeline feed
   */
  def getPublicTimelineFeed(count: Int): Feed

  /**
   * Gets the discussions feed.
   *
   * @param count
   *            the count
   *
   * @return the discussions feed
   */
  def getDiscussionsFeed(count: Int): Feed

  /**
   * Gets the discussions feed.
   *
   * @param topic
   *            the topic
   * @param count
   *            the count
   *
   * @return the discussions feed
   */
  def getDiscussionsFeed(topic: String, count: Int): Feed

  /**
   * Gets the job positions feed.
   *
   * @param count
   *            the count
   *
   * @return the job positions feed
   */
  def getJobPositionsFeed(count: Int): Feed

  /**
   * Gets the blog feed.
   *
   * @param count
   *            the count
   *
   * @return the blog feed
   */
  def getBlogFeed(count: Int): Feed
}