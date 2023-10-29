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

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yugyd.idiomatic.android.arch.components.ErrorMessageEffect
import com.yugyd.idiomatic.android.arch.components.ReusableSimpleScreen
import com.yugyd.idiomatic.android.arch.mvikotlin.R
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State.NavigationState
import com.yugyd.idiomatic.android.arch.mvikotlin.ui.MviKotlinBinder.Model
import com.yugyd.idiomatic.android.arch.theme.ThemePreviews

@Composable
internal fun MviKotlinRoute(
    binder: MviKotlinBinder,
    snackbarHostState: SnackbarHostState,
    onNavigateToNext: () -> Unit,
) {
    val state by binder.model.collectAsStateWithLifecycle()

    MviKotlinScreen(
        uiState = state,
        snackbarHostState = snackbarHostState,
        onActionClicked = binder::onActionClicked,
        onErrorDismissState = binder::onSnackbarDismissed,
        onNavigateToNext = onNavigateToNext,
        onNavigationHandled = binder::onNavigationHandled,
    )
}

@Composable
internal fun MviKotlinScreen(
    uiState: Model,
    snackbarHostState: SnackbarHostState,
    onActionClicked: () -> Unit,
    onErrorDismissState: () -> Unit,
    onNavigateToNext: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    ErrorMessageEffect(
        showErrorMessage = uiState.showErrorMessage,
        snackbarHostState = snackbarHostState,
        onErrorDismissState = onErrorDismissState,
    )

    ReusableSimpleScreen(
        toolbarTitle = stringResource(id = R.string.mvi_kotlin_title),
        isLoading = uiState.isLoading,
        isWarning = uiState.isWarning,
        message = uiState.message,
        onActionClicked = onActionClicked,
    )

    NavigationHandlerEffect(
        navigationState = uiState.navigationState,
        onNavigateToNext = onNavigateToNext,
        onNavigationHandled = onNavigationHandled,
    )
}

@Composable
internal fun NavigationHandlerEffect(
    navigationState: NavigationState?,
    onNavigateToNext: () -> Unit,
    onNavigationHandled: () -> Unit,
) {
    LaunchedEffect(key1 = navigationState) {
        when (navigationState) {
            is NavigationState.NavigateToNext -> onNavigateToNext()
            null -> Unit
        }

        navigationState?.let { onNavigationHandled() }
    }
}

@ThemePreviews
@Composable
private fun MviKotlinScreenPreview() {
    MaterialTheme {
        Surface {
            MviKotlinScreen(
                uiState = Model(
                    isLoading = false,
                    isWarning = false,
                    message = "Foo",
                    showErrorMessage = false,
                    navigationState = null,
                ),
                snackbarHostState = SnackbarHostState(),
                onActionClicked = {},
                onErrorDismissState = {},
                onNavigateToNext = {},
                onNavigationHandled = {},
            )
        }
    }
}
