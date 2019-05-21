package com.example.moviedbclean.presentation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviedbclean.domain.useCase.BaseUseCase
import com.example.moviedbclean.domain.useCase.DiscoverMovieUseCase
import com.example.moviedbclean.presentation.model.MovieModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.spyk
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    @MockK
    private lateinit var useCase: DiscoverMovieUseCase
    @MockK
    private lateinit var callback: BaseUseCase.OnCompletionCallback<List<MovieModel>>
    private val list = arrayListOf(MovieModel(1))

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun discoverMovie_init_listMovie() {
        val blockCallback = slot<BaseUseCase.OnCompletionCallback<List<MovieModel>>.()->Unit>()
        val blockOnComplete = slot<(result: List<MovieModel>) -> Unit>()
        var onComplete: ((result: List<MovieModel>) -> Unit)? = null

        every {
            callback.onComplete(capture(blockOnComplete))
        } answers {
            onComplete = blockOnComplete.captured
        }

        every {
            useCase.execute(capture(blockCallback))
        } answers {
            blockCallback.captured.invoke(callback)
            onComplete?.invoke(list)
        }

        viewModel = spyk(HomeViewModel(useCase))

        assertEquals(list, viewModel.movies.value)
    }

    @Test
    fun discoverMovie_init_loading() {
        val blockCallback = slot<BaseUseCase.OnCompletionCallback<List<MovieModel>>.()->Unit>()
        val blockOnLoading = slot<(isLoading: Boolean, progress: Int?) -> Unit>()
        var onLoading: ((isLoading: Boolean, progress: Int?) -> Unit)? = null

        every {
            callback.onLoading(capture(blockOnLoading))
        } answers {
            onLoading = blockOnLoading.captured
        }

        every {
            useCase.execute(capture(blockCallback))
        } answers {
            blockCallback.captured.invoke(callback)
            onLoading?.invoke(true, null)
        }

        viewModel = spyk(HomeViewModel(useCase))

        assertEquals(true, viewModel.isLoading.value)
    }
}