package pl.maciejnowak.exercise.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Captor

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pl.maciejnowak.commonobjects.entities.TopWiki
import pl.maciejnowak.exercise.R
import pl.maciejnowak.exercise.matcher.ToastMatcher.Companion.onToast
import pl.maciejnowak.exercise.ui.fragment.TopWikisFragment
import pl.maciejnowak.exercise.ui.viewmodel.TopWikisViewModel
import pl.maciejnowak.repositories.model.TopWikisResult

@RunWith(AndroidJUnit4::class)
class TopWikisFragmentTest : KoinTest {

    @Mock private lateinit var viewModel: TopWikisViewModel
    @Mock private lateinit var resultLiveData: LiveData<TopWikisResult>
    @Mock private lateinit var isLoadingLiveData: LiveData<Boolean>

    @Captor private lateinit var resultCaptor: ArgumentCaptor<Observer<TopWikisResult>>
    @Captor private lateinit var isLoadingCaptor: ArgumentCaptor<Observer<Boolean>>

    private val mockModule = module {
        viewModel(override = true) { viewModel }
    }
    private val topWiki = TopWiki(0, "title", "imageUrl", 0)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        `when`(viewModel.result).thenReturn(resultLiveData)
        `when`(viewModel.isLoading).thenReturn(isLoadingLiveData)
        loadKoinModules(mockModule)
        launchFragmentInContainer<TopWikisFragment>(themeResId = R.style.AppTheme_NoActionBar)
        verify(resultLiveData).observe(any(LifecycleOwner::class.java), resultCaptor.capture())
        verify(isLoadingLiveData).observe(any(LifecycleOwner::class.java), isLoadingCaptor.capture())
    }

    @Test
    fun startScreen_validateVisibility() {
        onView(withId(R.id.error_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.swipe_refresh))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.empty_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progressbar_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun clickErrorButton_resultSuccess_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.Success(listOf(topWiki)))
        changeResult(TopWikisResult.Error)
        onView(withId(R.id.error_button)).perform(click())
        checkViewSuccess()
    }

    @Test
    fun clickErrorButton_resultSuccessEmpty_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.Success(emptyList()))
        changeResult(TopWikisResult.Error)
        onView(withId(R.id.error_button)).perform(click())
        checkViewSuccessEmpty()
    }

    @Test
    fun clickErrorButton_resultError_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.Error)
        changeResult(TopWikisResult.Error)
        onView(withId(R.id.error_button)).perform(click())
        checkViewError()
    }

    @Test
    fun swipeRefresh_resultSuccess_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.Success(listOf(topWiki)))
        changeResult(TopWikisResult.Success(listOf(topWiki)))
        onView(withId(R.id.swipe_refresh)).perform(swipeDown())
        checkViewSuccess()
    }

    @Test
    fun swipeRefresh_resultSuccessEmpty_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.Success(emptyList()))
        changeResult(TopWikisResult.Success(emptyList()))
        onView(withId(R.id.swipe_refresh)).perform(swipeDown())
        checkViewSuccessEmpty()
    }

    @Test
    fun swipeRefresh_whenEmpty_resultErrorRefresh_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.ErrorRefresh(emptyList()))
        changeResult(TopWikisResult.Success(emptyList()))
        onView(withId(R.id.swipe_refresh)).perform(swipeDown())
        onToast(R.string.refresh_data_failed).check(matches(isDisplayed()))
        checkViewSuccessEmpty()
    }

    @Test
    fun swipeRefresh_whenData_resultErrorRefresh_validateVisibility() {
        mockLoadTopWikis(TopWikisResult.ErrorRefresh(emptyList()))
        changeResult(TopWikisResult.Success(listOf(topWiki)))
        onView(withId(R.id.swipe_refresh)).perform(swipeDown())
        onToast(R.string.refresh_data_failed).check(matches(isDisplayed()))
        checkViewSuccess()
    }

    @Test
    fun renderStartLoadingState_validateVisibility() {
        changeIsLoading(true)
        onView(withId(R.id.error_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progressbar_container))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun renderFinishLoadingState_validateVisibility() {
        changeIsLoading(false)
        onView(withId(R.id.progressbar_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun renderSuccessState_validateVisibility() {
        changeResult(TopWikisResult.Success(listOf(topWiki)))
        checkViewSuccess()
    }

    @Test
    fun renderSuccessEmptyState_validateVisibility() {
        changeResult(TopWikisResult.Success(emptyList()))
        checkViewSuccessEmpty()
    }

    @Test
    fun renderErrorRefreshState_validateVisibility() {
        changeResult(TopWikisResult.ErrorRefresh(listOf(topWiki)))
        onToast(R.string.refresh_data_failed).check(matches(isDisplayed()))
        checkViewSuccess()
    }

    @Test
    fun renderErrorRefreshEmptyState_validateVisibility() {
        changeResult(TopWikisResult.ErrorRefresh(emptyList()))
        onToast(R.string.refresh_data_failed).check(matches(isDisplayed()))
        checkViewSuccessEmpty()
    }

    @Test
    fun renderErrorState_validateVisibility() {
        changeResult(TopWikisResult.Error)
        checkViewError()
    }

    private fun changeResult(result: TopWikisResult) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            resultCaptor.value.onChanged(result)
        }
        mockIsSuccess(result !is TopWikisResult.Error)
    }

    private fun changeIsLoading(enable: Boolean) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            isLoadingCaptor.value.onChanged(enable)
        }
    }

    private fun mockLoadTopWikis(result: TopWikisResult) {
        `when`(viewModel.loadTopWikis(anyBoolean())).then {
            isLoadingCaptor.value.onChanged(true)
            resultCaptor.value.onChanged(result)
            isLoadingCaptor.value.onChanged(false)
        }
        mockIsSuccess(result !is TopWikisResult.Error)
    }

    private fun mockIsSuccess(isSuccess: Boolean) {
        `when`(viewModel.isSuccess()).thenReturn(isSuccess)
    }

    private fun checkViewSuccess() {
        onView(withId(R.id.error_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.swipe_refresh))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    private fun checkViewSuccessEmpty() {
        onView(withId(R.id.error_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.swipe_refresh))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.empty_container))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    private fun checkViewError() {
        onView(withId(R.id.error_container))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.swipe_refresh))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.empty_container))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}