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

/**
 * The Class GitHubException.
 */
object GitHubException {
  /**The Constant serialVersionUID. */
  private final val serialVersionUID: Long = -2392119987027760999L
}

/**
 * Instantiates a new git hub exception.
 *
 * @param message
 *            the message
 * @param cause
 *            the cause
 */
class GitHubException(message: String, cause: Throwable) extends RuntimeException(message, cause) {
  
  /**
   * Instantiates a new git hub exception.
   *
   * @param message
   *            the message
   */
  def this(message: String) {
    this(message,null)
  }

  /**
   * Instantiates a new git hub exception.
   *
   * @param cause
   *            the cause
   */
  def this(cause: Throwable) {
    this("",cause)
  }

}