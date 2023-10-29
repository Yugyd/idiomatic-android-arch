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

import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.ComposeStore
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.Intent
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State

internal interface MviKotlinStore : ComposeStore<Intent, State> {

    sealed interface Intent {
        object OnActionClicked : Intent
        object OnSnackbarDismissed : Intent
        object OnNavigationHandled : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isWarning: Boolean = false,
        val message: String = "",
        val showErrorMessage: Boolean = false,
        val navigationState: NavigationState? = null,
    ) {

        sealed interface NavigationState {
            object NavigateToNext : NavigationState
        }
    }
}