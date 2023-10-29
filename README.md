Idiomic Android Arch
====================

A typical Android project using different architecture options. Suitable for studying, analyzing and
choosing an architecture for your own project.

# List of architectures

## MVVM Google

https://developer.android.com/topic/architecture

## Mvi-Kotlin

https://github.com/arkivanov/MVIKotlin

https://arkivanov.github.io/MVIKotlin

# Template code

## Base project file structure

- app: main project configuration, application build
- build-logic: project build logic
- docs: accompanying documentation for the project
- gradle: everything related to gradle is the wrapper and libs directory
- product: the project code base, here is all the functionality of the application
- sample-app: project configuration of dependencies that a developer needs to develop a feature

## Stack template project

* Language: Kotlin
* Architecture: clean, multi-module
* UI: Compose, Material 3
* Navigation: Jetpack Compose Navigation
* Testing: Coming Soon!

## Build types

`debug` - Logging, debug mode, proguard off.

`release` - No logging, no debug mode, proguard enabled.

# Contributions

[Guide](docs/CONTRIBUTION.md)

# License

```
   Copyright 2023 Roman Likhachev

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
