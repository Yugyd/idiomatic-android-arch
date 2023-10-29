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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.buildStoreFactory
import com.yugyd.idiomatic.android.arch.mvikotlin.data.MviKotlinRepository
import com.yugyd.idiomatic.android.arch.mvikotlin.data.MviKotlinRepositoryImpl
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinActionCoroutineExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinExecutorFactory
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinReducer
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStoreFactory
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.delegates.LoadSampleDataActionExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.delegates.OnActionClickedIntentExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.delegates.OnNavigationHandledIntentExecutorDelegate
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.delegates.OnSnackbarDismissedIntentExecutorDelegate
import javax.inject.Provider

internal class MviKotlinFragment : Fragment() {

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val storeFactory = buildStoreFactory(isDebug = false)

            val actionDelegates = Provider<Set<MviKotlinActionCoroutineExecutorDelegate>> {
                val repository: MviKotlinRepository = MviKotlinRepositoryImpl(requireContext())
                setOf(
                    LoadSampleDataActionExecutorDelegate(repository)
                )
            }

            val intentDelegates = Provider {
                setOf(
                    OnActionClickedIntentExecutorDelegate(),
                    OnNavigationHandledIntentExecutorDelegate(),
                    OnSnackbarDismissedIntentExecutorDelegate(),
                )
            }

            val executorFactory = MviKotlinExecutorFactory(
                actionDelegates = actionDelegates,
                intentDelegates = intentDelegates,
            )

            val reducer = MviKotlinReducer()

            val mviKotlinStoreFactory = MviKotlinStoreFactory(
                storeFactory = storeFactory,
                executorFactory = executorFactory,
                reducer = reducer,
            )

            val store = mviKotlinStoreFactory.create(autoInit = true)

            return MviKotlinBinder(store) as T
        }
    }
    private val binder: MviKotlinBinder by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MviKotlinRoute(
                    binder = binder,
                    snackbarHostState = SnackbarHostState(),
                    onNavigateToNext = {},
                )
            }
        }
    }
}