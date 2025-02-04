/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2023 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agorapulse.micronaut.console.function;

import com.agorapulse.micronaut.console.ConsoleConfiguration;
import com.agorapulse.micronaut.console.ConsoleService;
import com.agorapulse.micronaut.console.Script;
import com.agorapulse.micronaut.console.util.ExceptionSanitizer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;

import javax.inject.Inject;
import java.util.function.Function;

@FunctionBean(value = "auth-console", method = "apply")
public class AuthConsoleHandler extends FunctionInitializer implements Function<AuthorizedScript, String> {

    @Inject private ConsoleService service;
    @Inject private ConsoleConfiguration configuration;
    @Inject private ExceptionSanitizer sanitizer;

    @Override
    public String apply(AuthorizedScript authorizedScript) {
        Script script = new Script(configuration.getLanguage(), authorizedScript.getBody(), authorizedScript.getUser().toUser());
        try {
            return service.execute(script).toString();
        } catch (Throwable th) {
            return sanitizer.extractMessage(th) + "\n\nScript:\n" + script;
        }
    }

    public AuthConsoleHandler() { }

    public AuthConsoleHandler(ApplicationContext applicationContext) {
        super(applicationContext);
    }

}
