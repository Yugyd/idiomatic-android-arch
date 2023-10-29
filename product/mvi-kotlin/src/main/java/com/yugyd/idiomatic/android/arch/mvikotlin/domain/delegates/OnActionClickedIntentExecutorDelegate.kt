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

package com.yugyd.idiomatic.android.arch.mvikotlin.domain.delegates

import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.DelegationCoroutineExecutor
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinIntentCoroutineExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStoreFactory.Message
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.Intent
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State.NavigationState
import kotlinx.coroutines.CoroutineScope

internal class OnActionClickedIntentExecutorDelegate : MviKotlinIntentCoroutineExecutorDelegate {

    override fun execute(
        intent: Intent,
        executor: DelegationCoroutineExecutor<Intent, Nothing, State, Message, Nothing>,
        scope: CoroutineScope,
        getState: () -> State
    ): Boolean {
        if (intent != Intent.OnActionClicked) return false

        executor.dispatch(
            Message.UpdateNavigationState(navigationState = NavigationState.NavigateToNext)
        )

        return true
    }
}