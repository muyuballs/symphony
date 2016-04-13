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

import java.util.HashMap;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.symphony.model.Domain;
import org.b3log.symphony.service.DomainQueryService;
import org.json.JSONObject;

/**
 * Oauth processor.
 *
 * <ul>
 * <li>(/api/v2/oauth/token), POST</li>
 * </ul>
 *
 * @author <a href="http://blog.mornning.com">Qiao</a>
 * @version 1.0.0.0, Apri 13, 2016
 */
@RequestProcessor
public class DomainProcessor {

    private static final HashMap<String, Class<?>> DOMAIN_FIEIDS = new HashMap<String, Class<?>>();

    static {
        DOMAIN_FIEIDS.put(Domain.DOMAIN_T_ID, String.class);
        DOMAIN_FIEIDS.put(Domain.DOMAIN_TITLE, String.class);
        DOMAIN_FIEIDS.put(Domain.DOMAIN_URI, String.class);
        DOMAIN_FIEIDS.put(Domain.DOMAIN_SORT, int.class);
        DOMAIN_FIEIDS.put(Domain.DOMAIN_DESCRIPTION, String.class);
    }

    /**
     * Domain query service.
     */
    @Inject
    private DomainQueryService domainQueryService;

    /**
     * Get domain list
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     */
    @RequestProcessing(value = "/api/v2/domain/list", method = HTTPRequestMethod.GET)
    @SuppressWarnings("UseSpecificCatch")
    public void mobileLogin(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response) {
        final RestResultBuilder resultBuilder = new RestResultBuilder();
        try {
            JSONObject query = new JSONObject();
            query.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, 1);
            query.put(Pagination.PAGINATION_PAGE_SIZE, 15);
            query.put(Pagination.PAGINATION_WINDOW_SIZE, 15);
            JSONObject queryRet = domainQueryService.getDomains(query, DOMAIN_FIEIDS);
            resultBuilder.setSuccess(true).setResultCode(ErrorCode.SUCCESS).setMessage("success").setData(queryRet.optJSONArray(Domain.DOMAINS));
        } catch (final Exception e) {
            resultBuilder.setSuccess(false).setResultCode(ErrorCode.SERVER_ERROR).setMessage(e.getMessage());
        } finally {
            context.renderJSON(resultBuilder.build());
        }
    }
}
