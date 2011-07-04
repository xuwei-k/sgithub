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
import com.github.api.v2.schema.GeoLocation
import com.github.api.v2.schema.Job

/**
 * The Interface JobService.
 */
abstract trait JobService extends GitHubService {
  /**
   * Search jobs.
   *
   * @param query
   *            the query
   *
   * @return the list< job>
   */
  def searchJobs(query: String): List[Job]

  /**
   * Search jobs.
   *
   * @param query
   *            the query
   * @param location
   *            the location
   *
   * @return the list< job>
   */
  def searchJobs(query: String, location: String): List[Job]

  /**
   * Search jobs.
   *
   * @param query
   *            the query
   * @param location
   *            the location
   *
   * @return the list< job>
   */
  def searchJobs(query: String, location: GeoLocation): List[Job]

  /**
   * Search full time jobs.
   *
   * @param query
   *            the query
   *
   * @return the list< job>
   */
  def searchFullTimeJobs(query: String): List[Job]

  /**
   * Search full time jobs.
   *
   * @param query
   *            the query
   * @param location
   *            the location
   *
   * @return the list< job>
   */
  def searchFullTimeJobs(query: String, location: String): List[Job]

  /**
   * Search full time jobs.
   *
   * @param query
   *            the query
   * @param location
   *            the location
   *
   * @return the list< job>
   */
  def searchFullTimeJobs(query: String, location: GeoLocation): List[Job]

  /**
   * Gets the job.
   *
   * @param id
   *            the id
   *
   * @return the job
   */
  def getJob(id: String): Job
}