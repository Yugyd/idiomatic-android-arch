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

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.yugyd.idiomatic.android.arch.core.componentui.mvi.domain.StoreImplFactory
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.Intent
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State
import com.yugyd.idiomatic.android.arch.mvikotlin.domain.MviKotlinStore.State.NavigationState

internal class MviKotlinStoreFactory(
    private val storeFactory: StoreFactory,
    private val executorFactory: MviKotlinExecutorFactory,
    private val reducer: MviKotlinReducer,
) : StoreImplFactory<MviKotlinStore> {

    private val bootstrapper = SimpleBootstrapper(Action.LoadSampleData)

    override fun create(autoInit: Boolean): MviKotlinStore {
        return object : MviKotlinStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = "MviKotlinStore",
                autoInit = autoInit,
                initialState = getInitialState(),
                bootstrapper = bootstrapper,
                executorFactory = executorFactory.create(),
                reducer = reducer,
            ) {}
    }

    private fun getInitialState() = State()

    sealed interface Action {
        object LoadSampleData : Action
    }

    sealed interface Message {
        object Loading : Message

        data class UpdateData(val message: String) : Message

        object UpdateError : Message

        data class UpdateNavigationState(
            val navigationState: NavigationState?
        ) : Message

        object HideErrorMessage : Message
    }
}
