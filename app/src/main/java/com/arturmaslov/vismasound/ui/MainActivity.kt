package com.arturmaslov.vismasound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.arturmaslov.vismasound.R
import com.arturmaslov.vismasound.data.source.remote.LoadStatus
import com.arturmaslov.vismasound.helpers.utils.ActivityHelper
import com.arturmaslov.vismasound.helpers.utils.ToastUtils
import com.arturmaslov.vismasound.ui.compose.BottomStorageMenuBar
import com.arturmaslov.vismasound.ui.compose.LoadingScreen
import com.arturmaslov.vismasound.ui.compose.MainMusicGenreScreen
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
            VismaSoundTheme {
                val loadStatus = mainVM.loadStatus().collectAsState().value

                val genresTrackLists = mainVM.genresTrackLists().collectAsState().value
                    ?: emptyMap()

                Scaffold(
                    topBar = {
                        VismaTopAppBar()
                    },
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoadingScreen(
                                showLoading = loadStatus == LoadStatus.LOADING
                            ) {
                                MainMusicGenreScreen(
                                    genresTrackLists = genresTrackLists
                                )
                            }
                        }
                    },
                    bottomBar = {
                        BottomStorageMenuBar()
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
            Timber.i("observeRepositoryResponse: $it")
        }
    }
}