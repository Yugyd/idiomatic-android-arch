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
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.utils.internal.atomic
import com.arkivanov.mvikotlin.utils.internal.initialize
import com.arkivanov.mvikotlin.utils.internal.requireValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class DelegationCoroutineExecutor<in Intent : Any, in Action : Any, in State : Any, Message : Any, Label : Any>(
    mainContext: CoroutineContext = Dispatchers.Main,
    private val intentDelegates: Set<IntentCoroutineExecutorDelegate<Intent, State, Message, Label>> = emptySet(),
    private val actionDelegates: Set<ActionCoroutineExecutorDelegate<Action, State, Message, Label>> = emptySet(),
) : Executor<Intent, Action, State, Message, Label> {

    private val callbacks = atomic<Executor.Callbacks<State, Message, Label>>()
    private val getState: () -> State = { callbacks.requireValue().state }

    private val scope: CoroutineScope = CoroutineScope(mainContext)

    override fun init(callbacks: Executor.Callbacks<State, Message, Label>) {
        this.callbacks.initialize(callbacks)
    }

    override fun executeIntent(intent: Intent) {
        val lastIndex = intentDelegates.size - 1

        intentDelegates.forEachIndexed { index, delegate ->
            val isHandled = delegate.execute(
                intent = intent,
                executor = this,
                scope = scope,
                getState = getState,
            )

            when {
                isHandled -> return
                index == lastIndex -> error("$$intent not handled")
                else -> Unit
            }
        }
    }

    override fun executeAction(action: Action) {
        val lastIndex = actionDelegates.size - 1

        actionDelegates.forEachIndexed { index, delegate ->
            val isHandled = delegate.execute(
                action = action,
                executor = this,
                scope = scope,
                getState = getState,
            )

            when {
                isHandled -> return
                index == lastIndex -> error("$action not handled")
                else -> Unit
            }
        }
    }

    override fun dispose() {
        scope.cancel()
    }

    @MainThread
    fun dispatch(message: Message) {
        callbacks.requireValue().onMessage(message)
    }

    @MainThread
    fun publish(label: Label) {
        callbacks.requireValue().onLabel(label)
    }
}
