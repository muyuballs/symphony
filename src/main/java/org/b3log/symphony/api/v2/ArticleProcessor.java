/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.symphony.api.v2;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Domain;
import org.b3log.symphony.repository.DomainRepository;
import org.b3log.symphony.service.ArticleQueryService;
import org.json.JSONObject;

/**
 * Article processor.
 *
 * @author <a href="http://blog.mornning.com">Qiao</a>
 * @version 1.0.0.0, Apri 13, 2016
 */
@RequestProcessor
public class ArticleProcessor {

    private static final Logger LOGGER = Logger.getLogger(ArticleProcessor.class);
    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

    /**
     * Domain repository.
     */
    @Inject
    private DomainRepository domainRepository;

    /**
     * Gets top articles.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @param page
     */
    @RequestProcessing(value = "/api/v2/stories/top/{page}", method = HTTPRequestMethod.GET)
    public void getArticles(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response, final String page) {
        final int pageSize = 25;
        final RestResultBuilder resutlBuilder = new RestResultBuilder();
        try {
            int currentPage;
            try {
                currentPage = Integer.parseInt(page);
            } catch (Exception exp) {
                resutlBuilder.setSuccess(false).setResultCode(ErrorCode.PARAMATERS_ERROR).setMessage(exp.getMessage());
                return;
            }
            resutlBuilder.setSuccess(true).setResultCode(ErrorCode.SUCCESS).setMessage("success");
            resutlBuilder.setData(this.articleQueryService.getTopArticlesWithComments(currentPage, pageSize));
        } catch (Exception exp) {
            LOGGER.log(Level.ERROR, exp.getMessage(), exp);
            resutlBuilder.setSuccess(false).setResultCode(ErrorCode.SERVER_ERROR).setMessage(exp.getMessage());
        } finally {
            context.renderJSON(resutlBuilder.build());
        }
    }

    /**
     * Gets recent articles
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @param page
     */
    @RequestProcessing(value = "/api/v2/stories/recent/{page}", method = HTTPRequestMethod.GET)
    public void getRecentArticles(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response, final String page) {
        final int pageSize = 25;
        final RestResultBuilder resutlBuilder = new RestResultBuilder();
        try {
            int currentPage;
            try {
                currentPage = Integer.parseInt(page);
            } catch (Exception exp) {
                resutlBuilder.setSuccess(false).setResultCode(ErrorCode.PARAMATERS_ERROR).setMessage(exp.getMessage());
                return;
            }
            resutlBuilder.setSuccess(true).setResultCode(ErrorCode.SUCCESS).setMessage("success");
            resutlBuilder.setData(this.articleQueryService.getRecentArticlesWithComments(currentPage, pageSize));
        } catch (Exception exp) {
            LOGGER.log(Level.ERROR, exp.getMessage(), exp);
            resutlBuilder.setSuccess(false).setResultCode(ErrorCode.SERVER_ERROR).setMessage(exp.getMessage());
        } finally {
            context.renderJSON(resutlBuilder.build());
        }
    }

    /**
     * Gets recent articles
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @param domainURI
     * @param page
     */
    @RequestProcessing(value = "/api/v2/stories/domain/{domainURI}/{page}", method = HTTPRequestMethod.GET)
    @SuppressWarnings("UseSpecificCatch")
    public void getArticlesWithDomain(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response, final String domainURI, final String page) {
        final int pageSize = 25;
        final RestResultBuilder resutlBuilder = new RestResultBuilder();
        try {
            int currentPage;
            try {
                currentPage = Integer.parseInt(page);
            } catch (Exception exp) {
                resutlBuilder.setSuccess(false).setResultCode(ErrorCode.PARAMATERS_ERROR).setMessage(exp.getMessage());
                return;
            }
            JSONObject domain = domainRepository.getByURI(domainURI);
            if (domain == null) {
                resutlBuilder.setSuccess(false).setResultCode(ErrorCode.PARAMATERS_ERROR).setMessage("domain not exists");
                return;
            }
            resutlBuilder.setSuccess(true).setResultCode(ErrorCode.SUCCESS).setMessage("success");
            resutlBuilder.setData(this.articleQueryService.getDomainArticles(domain.optString(Domain.DOMAIN_T_ID), currentPage, pageSize).opt(Article.ARTICLES));
        } catch (Exception exp) {
            LOGGER.log(Level.ERROR, exp.getMessage(), exp);
            resutlBuilder.setSuccess(false).setResultCode(ErrorCode.SERVER_ERROR).setMessage(exp.getMessage());
        } finally {
            context.renderJSON(resutlBuilder.build());
        }
    }
}
