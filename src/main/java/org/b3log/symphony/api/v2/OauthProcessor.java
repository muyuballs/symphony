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

import java.util.Date;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Keys;
import org.b3log.latke.model.User;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.service.UserMgmtService;
import org.b3log.symphony.service.UserQueryService;
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
public class OauthProcessor {

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * User management service.
     */
    @Inject
    private UserMgmtService userMgmtService;

    /**
     * Mobile logins user.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     */
    @RequestProcessing(value = "/api/v2/oauth/token", method = HTTPRequestMethod.POST)
    @SuppressWarnings("UseSpecificCatch")
    public void mobileLogin(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response) {
        final RestResultBuilder resultBuilder = new RestResultBuilder();
        try {
            final JSONObject ret = new JSONObject();
            final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
            final String username = requestJSONObject.optString("username", null);
            final String password = requestJSONObject.optString("password", null);
            if (Strings.isEmptyOrNull(username) || Strings.isEmptyOrNull(password)) {
                resultBuilder.setSuccess(false).setResultCode(ErrorCode.PARAMATERS_ERROR).setMessage("username or password is null");
                return;
            }
            JSONObject user = userQueryService.getUserByName(username);
            if (null == user) {
                user = userQueryService.getUserByEmail(username);
            }
            if (null == user) {
                resultBuilder.setSuccess(false).setResultCode(ErrorCode.USER_NOT_EXISTS).setMessage("user not exists");
                return;
            }
            if (UserExt.USER_STATUS_C_VALID != user.optInt(UserExt.USER_STATUS)) {
                userMgmtService.updateOnlineStatus(user.optString(Keys.OBJECT_ID), "", false);
                resultBuilder.setSuccess(false).setResultCode(ErrorCode.USER_STATUS_INVALID).setMessage("user status - invalid");
                return;
            }
            final String userPassword = user.optString(User.USER_PASSWORD);
            if (!userPassword.equals(password)) {
                userMgmtService.updateOnlineStatus(user.optString(Keys.OBJECT_ID), "", false);
                resultBuilder.setSuccess(false).setResultCode(ErrorCode.USER_PASSWORD_INVALID).setMessage("user password  invalid");
                return;
            }
            final String ip = Requests.getRemoteAddr(request);
            userMgmtService.updateOnlineStatus(user.optString(Keys.OBJECT_ID), ip, true);
            ret.put("access_token", "{\"userPassword\":\"" + user.optString(User.USER_PASSWORD) + "\",\"userEmail\":\"" + user.optString(User.USER_EMAIL) + "\"}");
            ret.put("token_type", "bearer");
            ret.put("scope", "user");
            ret.put("created_at", new Date().getTime());
            resultBuilder.setSuccess(true).setResultCode(ErrorCode.SUCCESS).setMessage("success").setData(ret);
        } catch (final Exception e) {
            resultBuilder.setSuccess(false).setResultCode(ErrorCode.SERVER_ERROR).setMessage(e.getMessage());
        } finally {
            context.renderJSON(resultBuilder.build());
        }
    }
}
