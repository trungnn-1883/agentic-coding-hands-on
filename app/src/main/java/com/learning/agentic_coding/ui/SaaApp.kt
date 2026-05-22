package com.learning.agentic_coding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.learning.agentic_coding.ServiceLocator
import com.learning.agentic_coding.data.locale.LocalizedContent
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.screens.home.HomeRoute
import com.learning.agentic_coding.ui.screens.home.HomeViewModel
import com.learning.agentic_coding.ui.screens.login.LoginRoute
import com.learning.agentic_coding.ui.screens.login.LoginViewModel
import com.learning.agentic_coding.ui.theme.MyApplicationTheme

/**
 * Top-level app composable. Routes between Login and Home based on Supabase session presence
 * (TC_LOGIN_ACC_001 / _ACC_002 / FUN_007 / _012). No NavHost — the auth state is the routing
 * signal, and there are only two destinations.
 */
@Composable
fun SaaApp(services: ServiceLocator) {
    val factory = viewModelFactory {
        initializer { LoginViewModel(services.authRepository, services.localeRepository) }
        initializer {
            HomeViewModel(
                authRepository = services.authRepository,
                localeRepository = services.localeRepository,
                awardsRepository = services.awardsRepository,
                kudosRepository = services.kudosRepository,
            )
        }
    }
    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val homeViewModel: HomeViewModel = viewModel(factory = factory)

    val user by services.authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val language by services.localeRepository.languageFlow
        .collectAsStateWithLifecycle(initialValue = Language.DEFAULT)

    MyApplicationTheme {
        LocalizedContent(language = language) {
            if (user == null) {
                LoginRoute(viewModel = loginViewModel)
            } else {
                HomeRoute(viewModel = homeViewModel)
            }
        }
    }
}
