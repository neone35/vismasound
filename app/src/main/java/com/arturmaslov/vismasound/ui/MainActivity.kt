package com.arturmaslov.vismasound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.helpers.utils.ActivityHelper
import com.arturmaslov.vismasound.helpers.utils.ToastUtils
import com.arturmaslov.vismasound.ui.compose.BottomStorageMenuBar
import com.arturmaslov.vismasound.ui.compose.LoadingScreen
import com.arturmaslov.vismasound.ui.compose.MainAllGenreScreen
import com.arturmaslov.vismasound.ui.compose.OneGenreLayout
import com.arturmaslov.vismasound.ui.compose.VismaTopAppBar
import com.arturmaslov.vismasound.ui.theme.VismaSoundTheme
import com.arturmaslov.vismasound.viewmodel.MainVM
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : ComponentActivity(), ActivityHelper {

    private val mainVM: MainVM by viewModel()
    private var disableBackCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setListeners()

        setContent {
            val navController = rememberNavController()
            LocalOnBackPressedDispatcherOwner provides this

            val loadStatus = mainVM.loadStatus().collectAsState().value
            val genresTrackLists = mainVM.genresTrackLists().collectAsState().value
                ?: emptyMap()
            val oneGenreTrackList =
                mainVM.oneGenreTrackList().collectAsState().value
                    ?: emptyList()

            VismaSoundTheme {
                Scaffold(
                    topBar = {
                        VismaTopAppBar()
                    },
                    content = { it ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoadingScreen(
                                showLoading = loadStatus == LoadStatus.LOADING
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = MAIN_SCREEN
                                ) {
                                    composable(MAIN_SCREEN) {
                                        MainAllGenreScreen(
                                            genresTrackLists = genresTrackLists,
                                            navController = navController,
                                            onSeeAllClick = { genre ->
                                                mainVM.onSeeAllClicked(genre)
                                            }
                                        )
                                    }
                                    composable("$GENRE_SCREEN/{genre}") { backStackEntry ->
                                        val oneGenre = backStackEntry.arguments?.getString("genre")
                                        oneGenre?.let { genre ->
                                            OneGenreLayout(
                                                oneGenreTrackList,
                                                genre,
                                                onSaveOptionSelected = { track, saveState ->
                                                    mainVM.onTrackSaveStateClick(track, saveState)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    bottomBar = {
                        val tempTrackSumDuration =
                            mainVM.tempTrackListLength().collectAsState().value
                        val permTrackSumDuration =
                            mainVM.permTrackListLength().collectAsState().value

                        BottomStorageMenuBar(
                            tempTrackSumDuration,
                            permTrackSumDuration,
                            tempStorageSelected = {
                                mainVM.onSeeAllClicked(MainVM.TEMP_STORAGE)
                            },
                            permStorageSelected = {
                                mainVM.onSeeAllClicked(MainVM.PERM_STORAGE)
                            },
                            navController = navController
                        )
                    }
                )
            }
        }
    }

    override fun setObservers() {
        lifecycleScope.launch { observeInternetAvailability(mainVM.internetIsAvailable()) }
        lifecycleScope.launch { observeLoadStatusToDisableBack(mainVM.loadStatus()) }
        lifecycleScope.launch { mainVM.remoteResponse?.let { observeRepositoryResponse(it) } }
    }

    override fun setListeners() {
        // Impossible to go back for this activity and all its children if loading when enabled
        disableBackCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                return
            }
        }
        onBackPressedDispatcher.addCallback(this, disableBackCallback as OnBackPressedCallback)
    }

    private suspend fun observeInternetAvailability(statusFlow: StateFlow<Boolean>) {
        statusFlow.collect {
            if (!it) ToastUtils.updateLong(this, getString(R.string.no_internet))
        }
    }

    private suspend fun observeLoadStatusToDisableBack(statusFlow: StateFlow<LoadStatus>) {
        statusFlow.collect {
            Timber.d("api status is $it")
            when (it) {
                LoadStatus.LOADING -> {
                    disableBackCallback?.isEnabled = true
                }

                LoadStatus.DONE -> {
                    disableBackCallback?.isEnabled = false
                }

                LoadStatus.ERROR -> {
                    Timber.e("Failure: $it")
                    disableBackCallback?.isEnabled = false
                }
            }
        }
    }

    private suspend fun observeRepositoryResponse(repoResponseFlow: SharedFlow<String?>) {
        repoResponseFlow.collect {
            it?.let { remoteRes -> ToastUtils.updateShort(this, remoteRes) }
            Timber.i("observeRepositoryResponse: $it")
        }
    }

    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val GENRE_SCREEN = "genre_screen"
    }
}