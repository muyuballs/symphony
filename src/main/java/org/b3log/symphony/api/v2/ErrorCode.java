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
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.b3log.symphony.api.v2;

/**
 *
 * @author <a href="http://blog.mornning.com">Qiao</a>
 * @version 1.0.0.0, Apri 13, 2016
 */
public final class ErrorCode {

    public static final int SUCCESS = 0;
    public static final int SERVER_ERROR = -1;
    public static final int PARAMATERS_ERROR = 1;
    public final static int USER_NOT_EXISTS = 2;
    static int USER_STATUS_INVALID = 3;
    static int USER_PASSWORD_INVALID = 4;
}
