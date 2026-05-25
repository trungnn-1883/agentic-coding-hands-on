package com.learning.agentic_coding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.learning.agentic_coding.ServiceLocator
import com.learning.agentic_coding.data.locale.LocalizedContent
import com.learning.agentic_coding.domain.Language
import com.learning.agentic_coding.ui.screens.award.AwardDetailRoute
import com.learning.agentic_coding.ui.screens.award.AwardDetailViewModel
import com.learning.agentic_coding.ui.screens.home.HomeRoute
import com.learning.agentic_coding.ui.screens.home.HomeViewModel
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.login.LoginRoute
import com.learning.agentic_coding.ui.screens.login.LoginViewModel
import com.learning.agentic_coding.ui.theme.MyApplicationTheme

/**
 * Top-level app composable. Three destinations:
 *  - Login when no auth user
 *  - Home (default after sign-in)
 *  - AwardDetail(slug) reachable from Home (award card tap or Awards bottom-nav tab)
 *
 * Destination is hoisted as in-memory state; bottom-nav SAA tab returns to Home.
 */
@Composable
fun SaaApp(services: ServiceLocator) {
    val baseFactory = viewModelFactory {
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
    val loginViewModel: LoginViewModel = viewModel(factory = baseFactory)
    val homeViewModel: HomeViewModel = viewModel(factory = baseFactory)

    val user by services.authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val language by services.localeRepository.languageFlow
        .collectAsStateWithLifecycle(initialValue = Language.DEFAULT)

    var destination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }

    MyApplicationTheme {
        LocalizedContent(language = language) {
            if (user == null) {
                LoginRoute(viewModel = loginViewModel)
                return@LocalizedContent
            }
            when (val dest = destination) {
                is AppDestination.Home -> HomeRoute(
                    viewModel = homeViewModel,
                    onAwardClick = { award ->
                        destination = AppDestination.AwardDetail(award.slug)
                    },
                    onTabClick = { tab ->
                        if (tab == HomeTab.AWARDS) {
                            destination = AppDestination.AwardDetail(DEFAULT_AWARD_SLUG)
                        }
                    },
                )
                is AppDestination.AwardDetail -> {
                    val detailFactory = viewModelFactory {
                        initializer {
                            AwardDetailViewModel(
                                awardsRepository = services.awardsRepository,
                                localeRepository = services.localeRepository,
                                initialSlug = dest.slug,
                            )
                        }
                    }
                    val detailViewModel: AwardDetailViewModel = viewModel(
                        key = "award-detail-${dest.slug}",
                        factory = detailFactory,
                    )
                    AwardDetailRoute(
                        viewModel = detailViewModel,
                        onTabClick = { tab ->
                            destination = when (tab) {
                                HomeTab.SAA -> AppDestination.Home
                                HomeTab.AWARDS -> AppDestination.AwardDetail(dest.slug)
                                else -> AppDestination.Home
                            }
                        },
                    )
                }
            }
        }
    }
}

private const val DEFAULT_AWARD_SLUG = "top-talent"

/** Lightweight in-memory routing — replace with NavHost if/when deep links land. */
sealed interface AppDestination {
    data object Home : AppDestination
    data class AwardDetail(val slug: String) : AppDestination
}
