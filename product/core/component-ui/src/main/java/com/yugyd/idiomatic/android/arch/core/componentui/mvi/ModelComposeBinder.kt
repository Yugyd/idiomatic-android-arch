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

package com.yugyd.idiomatic.android.arch.core.componentui.mvi

import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.ComposeStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class ModelComposeBinder<Intent : Any, State : Any, Model : Any>(
    private val store: ComposeStore<Intent, State>,
    initialModel: Model,
) : ComposeBinder() {

    protected abstract val stateToModel: (State.() -> Model)

    val model: StateFlow<Model> by lazy(LazyThreadSafetyMode.NONE) {
        store.states.map(stateToModel).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLS),
            initialValue = initialModel,
        )
    }

    init {
        check(!store.isDisposed) { "Store must not be disposed" }
    }

    override fun unbind() {
        store.dispose()
    }

    companion object {
        private const val STOP_TIMEOUT_MILLS = 5000L
    }
}
