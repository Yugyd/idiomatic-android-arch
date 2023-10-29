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

package com.yugyd.idiomatic.android.arch.mvvmgoogle.ui

import androidx.lifecycle.viewModelScope
import com.yugyd.idiomatic.android.arch.core.componentui.mvvm.BaseViewModel
import com.yugyd.idiomatic.android.arch.mvvmgoogle.domain.MvvmGoogleUseCase
import com.yugyd.idiomatic.android.arch.mvvmgoogle.ui.MvvmGoogleView.Action
import com.yugyd.idiomatic.android.arch.mvvmgoogle.ui.MvvmGoogleView.State
import com.yugyd.idiomatic.android.arch.mvvmgoogle.ui.MvvmGoogleView.State.NavigationState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class MvvmGoogleViewModel(
    private val sample1UseCase: MvvmGoogleUseCase,
) : BaseViewModel<State, Action>(
    initialState = State(isLoading = true),
) {

    init {
        loadData()
    }

    override fun handleAction(action: Action) = when (action) {
        Action.OnActionClicked -> onActionClicked()
        Action.OnNavigationHandled -> {
            screenState = screenState.copy(navigationState = null)
        }
        Action.OnSnackbarDismissed -> {
            screenState = screenState.copy(showErrorMessage = false)
        }
    }

    private fun onActionClicked() {
        screenState = screenState.copy(
            navigationState = NavigationState.NavigateToNext,
        )
    }

    private fun loadData() {
        screenState = screenState.copy(isLoading = true)

        viewModelScope.launch {
            sample1UseCase().catch {
                processDataError(it)
            }.collect(::processData)
        }
    }

    private fun processData(message: String) {
        screenState = screenState.copy(
            message = message,
            isLoading = false,
        )
    }

    private fun processDataError(error: Throwable) {
        error.printStackTrace()

        screenState = screenState.copy(
            isLoading = false,
            showErrorMessage = true,
        )
    }
}
