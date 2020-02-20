@file:Suppress("SpellCheckingInspection", "MayBeConstant")

import org.gradle.api.JavaVersion

object Config {
    val minSdk = 21
    val compileSdk = 29
    val targetSdk = 29
    val buildTools = "29.0.3"
    val javaVersion = JavaVersion.VERSION_1_8
}

object Versions {

    //gradle
    val gradle = "3.5.3"
    val versions_gradle_plugin = "0.27.0"

    //kotlin
    val kotlin = "1.3.61"
    val kotlinx = "1.3.3"

    //androidx
    val androidx_appcompat = "1.1.0"
    val androidx_core = "1.2.0"
    val androidx_fragment = "1.2.1"
    val androidx_legacy = "1.0.0"
    val androidx_recyclerview = "1.1.0"
    val androidx_constraintlayout = "1.1.3"
    val androidx_swiperefreshlayout = "1.0.0"
    val androidx_lifecycle = "2.2.0"
    val androidx_room = "2.2.3"

    //test
    val junit = "4.12"
    val androidx_test_ext = "1.1.1"
    val androidx_test_espresso = "3.2.0"
    val androidx_arch_core = "2.1.0"
    val mockito = "3.2.4"

    //external
    val android_material = "1.1.0"
    val koin = "2.0.1"
    val glide = "4.11.0"
    val retrofit = "2.7.1"
}

object Deps {

    //gradle
    val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    val versions_gradle_plugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.versions_gradle_plugin}"

    //kotlin
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx}"
    val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx}"

    //androidx
    val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    val androidx_core = "androidx.core:core-ktx:${Versions.androidx_core}"
    val androidx_fragment = "androidx.fragment:fragment-ktx:${Versions.androidx_fragment}"
    val androidx_legacy = "androidx.legacy:legacy-support-v4:${Versions.androidx_legacy}"
    val androidx_recyclerview = "androidx.recyclerview:recyclerview:${Versions.androidx_recyclerview}"
    val androidx_constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraintlayout}"
    val androidx_swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.androidx_swiperefreshlayout}"
    val androidx_lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.androidx_lifecycle}"
    val androidx_lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx_lifecycle}"
    val androidx_lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidx_lifecycle}"
    val androidx_lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.androidx_lifecycle}"
    val androidx_room_runtime = "androidx.room:room-runtime:${Versions.androidx_room}"
    val androidx_room_ktx = "androidx.room:room-ktx:${Versions.androidx_room}"
    val androidx_room_compiler = "androidx.room:room-compiler:${Versions.androidx_room}"

    //test
    val junit = "junit:junit:${Versions.junit}"
    val kotlinx_coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx}"
    val androidx_test_ext = "androidx.test.ext:junit:${Versions.androidx_test_ext}"
    val androidx_test_espresso = "androidx.test.espresso:espresso-core:${Versions.androidx_test_espresso}"
    val androidx_arch_core_testing = "androidx.arch.core:core-testing:${Versions.androidx_arch_core}"
    val mockito_inline = "org.mockito:mockito-inline:${Versions.mockito}"

    //external
    val android_material = "com.google.android.material:material:${Versions.android_material}"
    val koin_android = "org.koin:koin-android:${Versions.koin}"
    val koin_viewmodel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
}




