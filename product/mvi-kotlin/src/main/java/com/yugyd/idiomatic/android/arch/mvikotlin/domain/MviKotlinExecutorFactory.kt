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

package com.yugyd.idiomatic.android.arch.mvikotlin.domain

import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.ActionCoroutineExecutorDelegate
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.DelegationCoroutineExecutor
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.ExecutorFactory
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.IntentCoroutineExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.Intent
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStoreFactory.Action
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStoreFactory.Message
import javax.inject.Provider

internal typealias MviKotlinExecutor = DelegationCoroutineExecutor<Intent, Action, State, Message, Nothing>

internal typealias MviKotlinActionCoroutineExecutorDelegate =
        ActionCoroutineExecutorDelegate<Action, State, Message, Nothing>

internal typealias MviKotlinIntentCoroutineExecutorDelegate =
        IntentCoroutineExecutorDelegate<Intent, State, Message, Nothing>

internal class MviKotlinExecutorFactory(
    private val actionDelegates: Provider<Set<@JvmSuppressWildcards MviKotlinActionCoroutineExecutorDelegate>>,
    private val intentDelegates: Provider<Set<@JvmSuppressWildcards MviKotlinIntentCoroutineExecutorDelegate>>,
) : ExecutorFactory<MviKotlinExecutor> {

    override fun create() = { ->
        MviKotlinExecutor(
            actionDelegates = actionDelegates.get(),
            intentDelegates = intentDelegates.get(),
        )
    }
}
