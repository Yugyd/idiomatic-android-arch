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

import com.arkivanov.mvikotlin.core.store.Reducer
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStoreFactory.Message

internal class MviKotlinReducer : Reducer<State, Message> {

    override fun State.reduce(msg: Message) = when (msg) {
        Message.Loading -> reduceLoading()
        is Message.UpdateData -> reduce(msg)
        Message.UpdateError -> reduceError()
        Message.HideErrorMessage -> copy(showErrorMessage = false)
        is Message.UpdateNavigationState -> copy(navigationState = msg.navigationState)
    }

    private fun State.reduceLoading() = copy(
        isLoading = true,
        isWarning = false,
        message = "",
    )

    private fun State.reduce(msg: Message.UpdateData) = copy(
        isLoading = false,
        isWarning = false,
        message = msg.message,
    )

    private fun State.reduceError() = copy(
        isLoading = false,
        isWarning = false,
        message = "",
        showErrorMessage = true,
    )
}