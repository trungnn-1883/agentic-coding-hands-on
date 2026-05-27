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
import com.learning.agentic_coding.ui.screens.kudos.all.AllKudosRoute
import com.learning.agentic_coding.ui.screens.kudos.all.AllKudosViewModel
import com.learning.agentic_coding.ui.screens.kudos.compose.KudoComposeRoute
import com.learning.agentic_coding.ui.screens.kudos.compose.KudoComposeViewModel
import com.learning.agentic_coding.ui.screens.kudos.detail.KudoDetailRoute
import com.learning.agentic_coding.ui.screens.kudos.detail.KudoDetailViewModel
import com.learning.agentic_coding.ui.screens.kudos.main.KudosHomeRoute
import com.learning.agentic_coding.ui.screens.kudos.main.KudosHomeViewModel
import com.learning.agentic_coding.ui.screens.kudos.search.SunnerSearchRoute
import com.learning.agentic_coding.ui.screens.kudos.search.SunnerSearchViewModel
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
        initializer {
            KudosHomeViewModel(
                kudosRepository = services.kudosRepository,
                localeRepository = services.localeRepository,
            )
        }
        initializer {
            AllKudosViewModel(
                kudosRepository = services.kudosRepository,
                localeRepository = services.localeRepository,
            )
        }
    }
    val loginViewModel: LoginViewModel = viewModel(factory = baseFactory)
    val homeViewModel: HomeViewModel = viewModel(factory = baseFactory)
    val kudosHomeViewModel: KudosHomeViewModel = viewModel(factory = baseFactory)
    val allKudosViewModel: AllKudosViewModel = viewModel(factory = baseFactory)

    val user by services.authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)
    val language by services.localeRepository.languageFlow
        .collectAsStateWithLifecycle(initialValue = Language.DEFAULT)

    var destination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }
    // Back target for KudoDetail — set when opening detail so we can return to the
    // origin screen (KudosHome or AllKudos) instead of always defaulting to KudosHome.
    var kudoDetailBack by remember { mutableStateOf<AppDestination>(AppDestination.KudosHome) }

    MyApplicationTheme {
        LocalizedContent(language = language) {
            if (user == null) {
                LoginRoute(viewModel = loginViewModel)
                return@LocalizedContent
            }
            val tabRouter: (HomeTab) -> Unit = { tab ->
                destination = when (tab) {
                    HomeTab.SAA -> AppDestination.Home
                    HomeTab.AWARDS -> AppDestination.AwardDetail(DEFAULT_AWARD_SLUG)
                    HomeTab.KUDOS -> AppDestination.KudosHome
                    HomeTab.PROFILE -> destination
                }
            }
            val openKudoDetail: (String) -> Unit = { id ->
                kudoDetailBack = destination
                destination = AppDestination.KudoDetail(id)
            }
            val openKudoCompose: () -> Unit = { destination = AppDestination.KudoCompose }
            val openSunnerSearch: () -> Unit = { destination = AppDestination.SunnerSearch }
            when (val dest = destination) {
                is AppDestination.Home -> HomeRoute(
                    viewModel = homeViewModel,
                    onAwardClick = { award ->
                        destination = AppDestination.AwardDetail(award.slug)
                    },
                    onTabClick = tabRouter,
                    onComposeKudo = openKudoCompose,
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
                        onTabClick = tabRouter,
                    )
                }
                is AppDestination.KudosHome -> KudosHomeRoute(
                    viewModel = kudosHomeViewModel,
                    onLanguageSelect = homeViewModel::onLanguageSelect,
                    onViewAllKudos = { destination = AppDestination.AllKudos },
                    onDetailClick = openKudoDetail,
                    onTabClick = tabRouter,
                    onComposeClick = openKudoCompose,
                    onSearchClick = openSunnerSearch,
                )
                is AppDestination.AllKudos -> AllKudosRoute(
                    viewModel = allKudosViewModel,
                    onBack = { destination = AppDestination.KudosHome },
                    onDetailClick = openKudoDetail,
                    onTabClick = tabRouter,
                )
                is AppDestination.KudoCompose -> {
                    val composeFactory = viewModelFactory {
                        initializer {
                            KudoComposeViewModel(
                                kudosRepository = services.kudosRepository,
                                authRepository = services.authRepository,
                            )
                        }
                    }
                    val composeViewModel: KudoComposeViewModel = viewModel(
                        key = "kudo-compose",
                        factory = composeFactory,
                    )
                    KudoComposeRoute(
                        viewModel = composeViewModel,
                        onBack = { destination = AppDestination.KudosHome },
                        onSent = { destination = AppDestination.KudosHome },
                        onTabClick = tabRouter,
                    )
                }
                is AppDestination.SunnerSearch -> {
                    val searchFactory = viewModelFactory {
                        initializer {
                            SunnerSearchViewModel(
                                kudosRepository = services.kudosRepository,
                                recentSearchStore = services.recentSearchStore,
                            )
                        }
                    }
                    val searchViewModel: SunnerSearchViewModel = viewModel(
                        key = "sunner-search",
                        factory = searchFactory,
                    )
                    SunnerSearchRoute(
                        viewModel = searchViewModel,
                        onBack = { destination = AppDestination.KudosHome },
                        onTabClick = tabRouter,
                    )
                }
                is AppDestination.KudoDetail -> {
                    val detailFactory = viewModelFactory {
                        initializer {
                            KudoDetailViewModel(
                                kudosRepository = services.kudosRepository,
                                localeRepository = services.localeRepository,
                                kudoId = dest.id,
                            )
                        }
                    }
                    val kudoDetailViewModel: KudoDetailViewModel = viewModel(
                        key = "kudo-detail-${dest.id}",
                        factory = detailFactory,
                    )
                    KudoDetailRoute(
                        viewModel = kudoDetailViewModel,
                        onBack = { destination = kudoDetailBack },
                        onTabClick = tabRouter,
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
    data object KudosHome : AppDestination
    data object AllKudos : AppDestination
    data class KudoDetail(val id: String) : AppDestination
    data object KudoCompose : AppDestination
    data object SunnerSearch : AppDestination
}
