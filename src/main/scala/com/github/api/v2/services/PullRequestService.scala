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
import com.github.api.v2.schema.PullRequest
import com.github.api.v2.schema.Issue

/**
 * The Interface PullRequestService.
 */
trait PullRequestService extends GitHubService {
  /**
   * Gets the pull requests.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the pull requests
   */
  def getPullRequests(userName: String, repositoryName: String): List[PullRequest]

  /**
   * Gets the pull requests.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param state
   *            the state
   *
   * @return the pull requests
   */
  def getPullRequests(userName: String, repositoryName: String, state: Issue.State): List[PullRequest]

  /**
   * Gets the pull request.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   *
   * @return the pull request
   */
  def getPullRequest(userName: String, repositoryName: String, issueNumber: Int): PullRequest

  /**
   * Creates the pull request.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param base
   *            the base
   * @param head
   *            the head
   * @param title
   *            the title
   * @param body
   *            the body
   *
   * @return the pull request
   */
  def createPullRequest(userName: String, repositoryName: String, base: String, head: String, title: String, body: String): PullRequest
}