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
import com.yugyd.idiomatic.android.arch.mvvmgoogle.data.MvvmGoogleRepository
import com.yugyd.idiomatic.android.arch.mvvmgoogle.data.MvvmGoogleRepositoryImpl
import com.yugyd.idiomatic.android.arch.mvvmgoogle.domain.MvvmGoogleUseCase

class MvvmGoogleFragment : Fragment() {

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repository: MvvmGoogleRepository = MvvmGoogleRepositoryImpl(requireContext())
            val useCase = MvvmGoogleUseCase(repository)
            return MvvmGoogleViewModel(useCase) as T
        }
    }
    private val viewModel: MvvmGoogleViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MvvmGoogleRoute(viewModel = viewModel,
                    snackbarHostState = SnackbarHostState(),
                    onNavigateToNext = {})
            }
        }
    }
}