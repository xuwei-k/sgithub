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

import com.github.api.v2.services.impl.CommitServiceImpl
import com.github.api.v2.services.impl.FeedServiceImpl
import com.github.api.v2.services.impl.GistServiceImpl
import com.github.api.v2.services.impl.IssueServiceImpl
import com.github.api.v2.services.impl.JobServiceImpl
import com.github.api.v2.services.impl.NetworkServiceImpl
import com.github.api.v2.services.impl.OAuthServiceImpl
import com.github.api.v2.services.impl.ObjectServiceImpl
import com.github.api.v2.services.impl.OrganizationServiceImpl
import com.github.api.v2.services.impl.PullRequestServiceImpl
import com.github.api.v2.services.impl.RepositoryServiceImpl
import com.github.api.v2.services.impl.UserServiceImpl

/**
 * A factory for creating GitHubService objects.
 */
object GitHubServiceFactory {
  /**
   * New instance.
   *
   * @return the git hub service factory
   */
  def newInstance: GitHubServiceFactory = {
    return new GitHubServiceFactory
  }
}

/**
 * Instantiates a new git hub service factory.
 */
class GitHubServiceFactory private {

  /**
   * Creates a new GitHubService object.
   *
   * @return the commit service
   */
  def createCommitService: CommitService = {
    return new CommitServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the gist service
   */
  def createGistService: GistService = {
    return new GistServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the issue service
   */
  def createIssueService: IssueService = {
    return new IssueServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the network service
   */
  def createNetworkService: NetworkService = {
    return new NetworkServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the object service
   */
  def createObjectService: ObjectService = {
    return new ObjectServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the repository service
   */
  def createRepositoryService: RepositoryService = {
    return new RepositoryServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the organization service
   */
  def createOrganizationService: OrganizationService = {
    return new OrganizationServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the user service
   */
  def createUserService: UserService = {
    return new UserServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @param clientId
   *            the client id
   * @param secret
   *            the secret
   *
   * @return the o auth service
   */
  def createOAuthService(clientId: String, secret: String): OAuthService = {
    return new OAuthServiceImpl(clientId, secret)
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the feed service
   */
  def createFeedService: FeedService = {
    return new FeedServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the pull request service
   */
  def createPullRequestService: PullRequestService = {
    return new PullRequestServiceImpl
  }

  /**
   * Creates a new GitHubService object.
   *
   * @return the job service
   */
  def createJobService: JobService = {
    return new JobServiceImpl
  }
}