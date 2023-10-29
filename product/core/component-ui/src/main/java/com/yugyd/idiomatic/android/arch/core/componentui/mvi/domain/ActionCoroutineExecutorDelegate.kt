/*
 * Copyright 2023 Roman Likhachev
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

package com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain

import com.arkivanov.mvikotlin.core.annotations.MainThread
import kotlinx.coroutines.CoroutineScope

interface ActionCoroutineExecutorDelegate<Action : Any, State : Any, Message : Any, Label : Any> {

    @MainThread
    fun execute(
        action: Action,
        executor: DelegationCoroutineExecutor<Nothing, Action, State, Message, Label>,
        scope: CoroutineScope,
        @MainThread getState: () -> State,
    ): Boolean
}
