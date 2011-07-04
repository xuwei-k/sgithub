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

import java.util.Map

/**
 * The Interface GitHubCommunicator.
 */
abstract trait GitHubCommunicator {
  /**
   * Sets the request headers.
   *
   * @param requestHeaders
   *            the request headers
   */
  def setRequestHeaders(requestHeaders: Map[String, String]): Unit

  /**
   * Gets the request headers.
   *
   * @return the request headers
   */
  def getRequestHeaders: Map[String, String]

  /**
   * Adds the request header.
   *
   * @param headerName
   *            the header name
   * @param headerValue
   *            the header value
   */
  def addRequestHeader(headerName: String, headerValue: String): Unit

  /**
   * Removes the request header.
   *
   * @param headerName
   *            the header name
   */
  def removeRequestHeader(headerName: String): Unit
}