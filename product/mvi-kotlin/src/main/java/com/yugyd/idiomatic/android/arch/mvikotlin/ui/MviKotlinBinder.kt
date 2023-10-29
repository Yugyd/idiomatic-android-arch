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

package com.yugyd.idiomatic.android.arch.mvikotlin.ui

import com.yugyd.idiomatic.android.arch.core.componentui.mvi.ModelComposeBinder
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.Intent
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State.NavigationState
import com.yugyd.idiomatic.android.arch.mvikotlin.ui.MviKotlinBinder.Model

internal class MviKotlinBinder(
    private val store: MviKotlinStore,
) : ModelComposeBinder<Intent, State, Model>(
    store = store,
    initialModel = Model(),
) {

    override val stateToModel: State.() -> Model = {
        Model(
            isLoading = isLoading,
            isWarning = isWarning,
            message = message,
            showErrorMessage = showErrorMessage,
        )
    }

    fun onActionClicked() {
        store.accept(Intent.OnActionClicked)
    }

    fun onSnackbarDismissed() {
        store.accept(Intent.OnSnackbarDismissed)
    }

    fun onNavigationHandled() {
        store.accept(Intent.OnNavigationHandled)
    }

    data class Model(
        val isLoading: Boolean = false,
        val isWarning: Boolean = false,
        val message: String = "",
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    )
}
