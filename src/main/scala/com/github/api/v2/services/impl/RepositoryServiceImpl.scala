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
package com.github.api.v2.services.impl

import java.util.{List => jList,Map => jMap,HashMap => jHashMap}
import java.util.zip.ZipInputStream
import com.github.api.v2.schema.Key
import com.github.api.v2.schema.Language
import com.github.api.v2.schema.Repository
import com.github.api.v2.schema.User
import com.github.api.v2.schema.Repository.Visibility
import com.github.api.v2.services.RepositoryService
import com.github.api.v2.services.constant.GitHubApiUrls
import com.github.api.v2.services.constant.ParameterNames
import com.github.api.v2.services.constant.GitHubApiUrls.GitHubApiUrlBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * The Class RepositoryServiceImpl.
 */
class RepositoryServiceImpl extends BaseGitHubService with RepositoryService {
  override def addCollaborator(userName: String, repositoryName: String, collaboratorName: String): Unit = {
    var builder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.ADD_COLLABORATOR_URL)
    var apiUrl = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).withField(ParameterNames.COLLABORATOR_NAME, collaboratorName).buildUrl
    unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
  }

  override def addDeployKey(repositoryName: String, title: String, key: String): jList[Key] = {
    var builder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.ADD_DEPLOY_KEY_URL)
    var apiUrl = builder.withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var parameters = new jHashMap[String, String]
    parameters.put(ParameterNames.TITLE, title)
    parameters.put(ParameterNames.KEY, key)
    var json = unmarshall(callApiPost(apiUrl, parameters))
    return unmarshall(new TypeToken[jList[Key]] {
    }, json.get("public_keys"))
  }

  override def changeVisibility(repositoryName: String, visibility: Repository.Visibility): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.CHANGE_VISIBILITY_URL)
    var apiUrl: String = builder.withField(ParameterNames.REPOSITORY_NAME, repositoryName).withFieldEnum(ParameterNames.VISIBILITY, visibility).buildUrl
    var json = unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
    unmarshall(new TypeToken[Repository] {}, json.get("repository"))
  }

  override def createRepository(name: String, description: String, homePage: String, visibility: Repository.Visibility): Repository = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.CREATE_REPOSITORY_URL)
    var apiUrl: String = builder.buildUrl
    var parameters: jMap[String, String] = new jHashMap[String, String]
    parameters.put(ParameterNames.NAME, name)
    parameters.put(ParameterNames.DESCRIPTION, description)
    parameters.put(ParameterNames.HOME_PAGE, homePage)
    parameters.put(ParameterNames.PUBLIC, (if ((visibility == Visibility.PUBLIC)) "1" else "0"))
    var json = unmarshall(callApiPost(apiUrl, parameters))
    return unmarshall(new TypeToken[Repository] {}, json.get("repository"))
  }

  override def deleteRepository(repositoryName: String): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.DELETE_REPOSITORY_URL)
    var apiUrl = builder.withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
    if (json.has("delete_token")) {
      var parameters: jMap[String, String] = new jHashMap[String, String]
      parameters.put(ParameterNames.DELETE_TOKEN, json.get("delete_token").getAsString)
      callApiPost(apiUrl, parameters)
    }
  }

  override def forkRepository(userName: String, repositoryName: String): Repository = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.FORK_REPOSITORY_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
    return unmarshall(new TypeToken[Repository] {}, json.get("repository"))
  }

  override def getBranches(userName: String, repositoryName: String): jMap[String, String] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_BRANCHES_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jMap[String,String]] {}, json.get("branches"))
  }

  override def getCollaborators(userName: String, repositoryName: String): jList[String] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_COLLABORATORS_URL)
    var apiUrl = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    unmarshall(new TypeToken[jList[String]]{}, json.get("collaborators"))
  }

  override def getContributors(userName: String, repositoryName: String): jList[User] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_CONTRIBUTORS_URL)
    var apiUrl = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[User]] {}, json.get("contributors"))
  }

  override def getForks(userName: String, repositoryName: String): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_FORKS_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] { }, json.get("network"))
  }

  override def getDeployKeys(repositoryName: String): jList[Key] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_DEPLOY_KEYS_URL)
    var apiUrl = builder.withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    unmarshall(new TypeToken[jList[Key]] {}, json.get("public_keys"))
  }

  override def getLanguageBreakdown(userName: String, repositoryName: String): jMap[Language, java.lang.Long] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_LANGUAGE_BREAKDOWN_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    unmarshall(new TypeToken[jMap[Language,java.lang.Long]] {}, json.get("languages"))
  }

  override def getPushableRepositories: jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_PUSHABLE_REPOSITORIES_URL)
    var apiUrl: String = builder.buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] { }, json.get("repositories"))
  }

  override def getRepositories(userName: String): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_REPOSITORIES_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]]{ }, json.get("repositories"))
  }

  override def getRepository(userName: String, repositoryName: String): Repository = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_REPOSITORY_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[Repository]{  }, json.get("repository"))
  }

  override def getTags(userName: String, repositoryName: String): jMap[String, String] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_TAGS_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jMap[String,String]] { }, json.get("tags"))
  }

  override def getWatchers(userName: String, repositoryName: String): jList[String] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_WATCHERS_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    unmarshall(new TypeToken[jList[String]] { }, json.get("watchers"))
  }

  override def removeCollaborator(userName: String, repositoryName: String, collaboratorName: String): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.REMOVE_COLLABORATOR_URL)
    var apiUrl = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).withField(ParameterNames.COLLABORATOR_NAME, collaboratorName).buildUrl
    unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
  }

  override def removeDeployKey(repositoryName: String, id: String): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.REMOVE_DEPLOY_KEY_URL)
    var apiUrl: String = builder.withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    var parameters: jMap[String, String] = new jHashMap[String, String]
    parameters.put(ParameterNames.ID, id)
    unmarshall(callApiPost(apiUrl, parameters))
  }

  override def searchRepositories(query: String): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.SEARCH_REPOSITORIES_URL)
    var apiUrl = builder.withField(ParameterNames.KEYWORD, query).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] { }, json.get("repositories"))
  }

  override def searchRepositories(query: String, language: Language): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.SEARCH_REPOSITORIES_URL)
    var apiUrl: String = builder.withField(ParameterNames.KEYWORD, query).withParameterEnum(ParameterNames.LANGUAGE, language).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] { }, json.get("repositories"))
  }

  override def searchRepositories(query: String, pageNumber: Int): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.SEARCH_REPOSITORIES_URL)
    var apiUrl: String = builder.withField(ParameterNames.KEYWORD, query).withParameter(ParameterNames.START_PAGE, String.valueOf(pageNumber)).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] {  }, json.get("repositories"))
  }

  override def searchRepositories(query: String, language: Language, pageNumber: Int): jList[Repository] = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.SEARCH_REPOSITORIES_URL)
    var apiUrl: String = builder.withField(ParameterNames.KEYWORD, query).withParameterEnum(ParameterNames.LANGUAGE, language).withParameter(ParameterNames.START_PAGE, String.valueOf(pageNumber)).buildUrl
    var json = unmarshall(callApiGet(apiUrl))
    return unmarshall(new TypeToken[jList[Repository]] { }, json.get("repositories"))
  }

  override def unwatchRepository(userName: String, repositoryName: String): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.UNWATCH_REPOSITORY_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
  }

  override def updateRepository(repository: Repository): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.UPDATE_REPOSITORY_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, repository.getOwner).withField(ParameterNames.REPOSITORY_NAME, repository.getName).buildUrl
    var parameters: jMap[String, String] = new jHashMap[String, String]
    parameters.put("values[" + ParameterNames.DESCRIPTION + "]", repository.getDescription)
    parameters.put("values[" + ParameterNames.HOME_PAGE + "]", repository.getHomepage)
    parameters.put("values[" + ParameterNames.HAS_WIKI + "]", String.valueOf(repository.isHasWiki))
    parameters.put("values[" + ParameterNames.HAS_ISSUES + "]", String.valueOf(repository.isHasIssues))
    parameters.put("values[" + ParameterNames.HAS_DOWNLOADS + "]", String.valueOf(repository.isHasDownloads))
    var json = unmarshall(callApiPost(apiUrl, parameters))
    unmarshall(new TypeToken[Repository] { }, json.get("repository"))
  }

  override def watchRepository(userName: String, repositoryName: String): Unit = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.WATCH_REPOSITORY_URL)
    var apiUrl: String = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).buildUrl
    unmarshall(callApiPost(apiUrl, new jHashMap[String, String]))
  }

  override def getRepositoryArchive(userName: String, repositoryName: String, branchName: String): ZipInputStream = {
    var builder: GitHubApiUrls.GitHubApiUrlBuilder = createGitHubApiUrlBuilder(GitHubApiUrls.RepositoryApiUrls.GET_REPOSITORY_ARCHIVE_URL)
    var apiUrl = builder.withField(ParameterNames.USER_NAME, userName).withField(ParameterNames.REPOSITORY_NAME, repositoryName).withField(ParameterNames.BRANCH, branchName).buildUrl
    new ZipInputStream(callApiGet(apiUrl))
  }
}