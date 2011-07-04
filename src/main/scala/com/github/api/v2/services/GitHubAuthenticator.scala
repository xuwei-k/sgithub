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

import com.github.api.v2.services.auth.Authentication

/**
 * The Interface GitHubAuthenticator.
 */
abstract trait GitHubAuthenticator extends GitHubCommunicator {
  /**
   * Sets the authentication.
   *
   * @param authentication
   *            the new authentication
   */
  def setAuthentication(authentication: Authentication): Unit

  /**
   * Sets the user ip address.
   *
   * @param userIpAddress
   *            the new user ip address
   */
  def setUserIpAddress(userIpAddress: String): Unit

  /**
   * Sets the referrer.
   *
   * @param referrer
   *            the new referrer
   */
  def setReferrer(referrer: String): Unit
}