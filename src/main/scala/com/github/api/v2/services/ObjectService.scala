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

import java.io.InputStream
import java.util.List
import com.github.api.v2.schema.Blob
import com.github.api.v2.schema.Tree

/**
 * The Interface ObjectService.
 */
abstract trait ObjectService extends GitHubService {
  /**
   * Gets the tree.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param treeSha
   *            the tree sha
   *
   * @return the tree
   */
  def getTree(userName: String, repositoryName: String, treeSha: String): List[Tree]

  /**
   * Gets the blob.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param treeSha
   *            the tree sha
   * @param filePath
   *            the file path
   *
   * @return the blob
   */
  def getBlob(userName: String, repositoryName: String, treeSha: String, filePath: String): Blob

  /**
   * Gets the blobs.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param treeSha
   *            the tree sha
   *
   * @return the blobs
   */
  def getBlobs(userName: String, repositoryName: String, treeSha: String): List[Blob]

  /**
   * Gets the object content.
   *
   * @param userName
   *            the user name
   * @param repositoryName
   *            the repository name
   * @param objectSha
   *            the object sha
   *
   * @return the object content
   */
  def getObjectContent(userName: String, repositoryName: String, objectSha: String): InputStream
}