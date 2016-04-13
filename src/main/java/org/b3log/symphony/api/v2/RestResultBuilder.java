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

import java.util.Collection;
import org.apache.commons.lang.time.StopWatch;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * RestResultBuilder
 *
 * @author <a href="http://blog.mornning.com">Qiao</a>
 * @version 1.0.0.0, Apri 13, 2016
 */
public class RestResultBuilder {

    private final JSONObject resultJson;
    private final StopWatch watch = new StopWatch();

    public RestResultBuilder() {
        watch.start();
        resultJson = new JSONObject();
    }

    public RestResultBuilder setSuccess(boolean success) {
        resultJson.put("success", success);
        return this;
    }

    public RestResultBuilder setResultCode(int code) {
        resultJson.put("code", code);
        return this;
    }

    public RestResultBuilder setMessage(String message) {
        resultJson.put("message", message);
        return this;
    }

    public RestResultBuilder setData(JSONObject data) {
        resultJson.put("data", data);
        return this;
    }

    public RestResultBuilder setData(Collection<JSONObject> data) {
        resultJson.put("data", data);
        return this;
    }

    public RestResultBuilder setData(JSONArray data) {
        resultJson.put("data", data);
        return this;
    }

    public RestResultBuilder setData(Object data) {
        resultJson.put("data", data);
        return this;
    }

    private void setTimeCost(long time) {
        resultJson.put("cost", time);
    }

    public JSONObject build() {
        watch.stop();
        setTimeCost(watch.getTime());
        return resultJson;
    }

}
