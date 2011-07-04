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
import com.github.api.v2.schema.Comment
import com.github.api.v2.schema.Issue
import com.github.api.v2.schema.Issue.State

/**
 * The Interface IssueService.
 */
abstract trait IssueService extends GitHubService {
  /**
   * Search issues.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param state
   *            the state
   * @param keyword
   *            the keyword
   *
   * @return the list< issue>
   */
  def searchIssues(userName: String, repositoryName: String, state: Issue.State, keyword: String): List[Issue]

  /**
   * Gets the issues.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param label
   *            the label
   *
   * @return the issues
   */
  def getIssues(userName: String, repositoryName: String, label: String): List[Issue]

  /**
   * Gets the issues.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param state
   *            the state
   *
   * @return the issues
   */
  def getIssues(userName: String, repositoryName: String, state: Issue.State): List[Issue]

  /**
   * Gets the issue.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   *
   * @return the issue
   */
  def getIssue(userName: String, repositoryName: String, issueNumber: Int): Issue

  /**
   * Gets the issue comments.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   *
   * @return the issue comments
   */
  def getIssueComments(userName: String, repositoryName: String, issueNumber: Int): List[Comment]

  /**
   * Creates the issue.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param title
   *            the title
   * @param body
   *            the body
   */
  def createIssue(userName: String, repositoryName: String, title: String, body: String): Unit

  /**
   * Close issue.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   */
  def closeIssue(userName: String, repositoryName: String, issueNumber: Int): Unit

  /**
   * Reopen issue.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   */
  def reopenIssue(userName: String, repositoryName: String, issueNumber: Int): Unit

  /**
   * Update issue.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   * @param title
   *            the title
   * @param body
   *            the body
   */
  def updateIssue(userName: String, repositoryName: String, issueNumber: Int, title: String, body: String): Unit

  /**
   * Gets the issue labels.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   *
   * @return the issue labels
   */
  def getIssueLabels(userName: String, repositoryName: String): List[String]

  /**
   * Adds the label.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   * @param label
   *            the label
   *
   * @return the list< string>
   */
  def addLabel(userName: String, repositoryName: String, issueNumber: Int, label: String): List[String]

  /**
   * Removes the label.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   * @param label
   *            the label
   *
   * @return the list< string>
   */
  def removeLabel(userName: String, repositoryName: String, issueNumber: Int, label: String): List[String]

  /**
   * Adds the comment.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param issueNumber
   *            the issue number
   * @param comment
   *            the comment
   */
  def addComment(userName: String, repositoryName: String, issueNumber: Int, comment: String): Unit
}