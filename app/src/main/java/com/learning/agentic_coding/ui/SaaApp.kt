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
import com.learning.agentic_coding.ui.screens.debug.DebugMenuScreen
import com.learning.agentic_coding.ui.screens.error.AccessDeniedScreen
import com.learning.agentic_coding.ui.screens.error.NotFoundScreen
import com.learning.agentic_coding.ui.screens.home.HomeRoute
import com.learning.agentic_coding.ui.screens.home.HomeViewModel
import com.learning.agentic_coding.ui.screens.home.components.HomeTab
import com.learning.agentic_coding.ui.screens.kudos.all.AllKudosRoute
import com.learning.agentic_coding.ui.screens.kudos.all.AllKudosViewModel
import com.learning.agentic_coding.ui.screens.kudos.compose.KudoComposeRoute
import com.learning.agentic_coding.ui.screens.kudos.compose.KudoComposeViewModel
import com.learning.agentic_coding.ui.screens.kudos.detail.KudoDetailRoute
import com.learning.agentic_coding.ui.screens.kudos.detail.KudoDetailViewModel
import com.learning.agentic_coding.ui.screens.kudos.info.CommunityStandardsScreen
import com.learning.agentic_coding.ui.screens.kudos.info.RulesScreen
import com.learning.agentic_coding.ui.screens.kudos.main.KudosHomeRoute
import com.learning.agentic_coding.ui.screens.kudos.main.KudosHomeViewModel
import com.learning.agentic_coding.ui.screens.kudos.search.SunnerSearchRoute
import com.learning.agentic_coding.ui.screens.kudos.search.SunnerSearchViewModel
import com.learning.agentic_coding.ui.screens.kudos.secretbox.OpenBoxRoute
import com.learning.agentic_coding.ui.screens.kudos.secretbox.OpenBoxViewModel
import com.learning.agentic_coding.ui.screens.login.LoginRoute
import com.learning.agentic_coding.ui.screens.login.LoginViewModel
import com.learning.agentic_coding.ui.screens.notifications.NotificationsRoute
import com.learning.agentic_coding.ui.screens.notifications.NotificationsViewModel
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
                notificationsRepository = services.notificationsRepository,
            )
        }
        initializer {
            KudosHomeViewModel(
                kudosRepository = services.kudosRepository,
                localeRepository = services.localeRepository,
                notificationsRepository = services.notificationsRepository,
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
    // Back targets for screens that can be reached from multiple entry points.
    var notificationsBack by remember { mutableStateOf<AppDestination>(AppDestination.Home) }
    var communityStandardsBack by remember { mutableStateOf<AppDestination>(AppDestination.KudoCompose) }
    var errorBack by remember { mutableStateOf<AppDestination>(AppDestination.DebugMenu) }

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
                    HomeTab.PROFILE -> AppDestination.DebugMenu
                }
            }
            val openNotFound: () -> Unit = {
                errorBack = destination
                destination = AppDestination.NotFound
            }
            val openAccessDenied: () -> Unit = {
                errorBack = destination
                destination = AppDestination.AccessDenied
            }
            val openKudoDetail: (String) -> Unit = { id ->
                kudoDetailBack = destination
                destination = AppDestination.KudoDetail(id)
            }
            val openKudoCompose: () -> Unit = { destination = AppDestination.KudoCompose }
            val openSunnerSearch: () -> Unit = { destination = AppDestination.SunnerSearch }
            val openCommunityStandards: () -> Unit = {
                communityStandardsBack = AppDestination.KudoCompose
                destination = AppDestination.CommunityStandards
            }
            val openRules: () -> Unit = { destination = AppDestination.Rules }
            val openSecretBox: () -> Unit = { destination = AppDestination.OpenBox }
            val openNotifications: () -> Unit = {
                notificationsBack = destination
                destination = AppDestination.Notifications
            }
            val openCommunityStandardsFromNotifications: () -> Unit = {
                communityStandardsBack = AppDestination.Notifications
                destination = AppDestination.CommunityStandards
            }
            when (val dest = destination) {
                is AppDestination.Home -> HomeRoute(
                    viewModel = homeViewModel,
                    onAwardClick = { award ->
                        destination = AppDestination.AwardDetail(award.slug)
                    },
                    onTabClick = tabRouter,
                    onComposeKudo = openKudoCompose,
                    onNotificationsClick = openNotifications,
                )
                is AppDestination.AwardDetail -> {
                    val detailFactory = viewModelFactory {
                        initializer {
                            AwardDetailViewModel(
                                awardsRepository = services.awardsRepository,
                                localeRepository = services.localeRepository,
                                notificationsRepository = services.notificationsRepository,
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
                        onNotificationsClick = openNotifications,
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
                    onRulesClick = openRules,
                    onOpenSecretBox = openSecretBox,
                    onNotificationsClick = openNotifications,
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
                        onOpenCommunityStandards = openCommunityStandards,
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
                is AppDestination.CommunityStandards -> CommunityStandardsScreen(
                    onBack = { destination = communityStandardsBack },
                )
                is AppDestination.Notifications -> {
                    val notificationsFactory = viewModelFactory {
                        initializer {
                            NotificationsViewModel(
                                repository = services.notificationsRepository,
                                localeRepository = services.localeRepository,
                            )
                        }
                    }
                    val notificationsViewModel: NotificationsViewModel = viewModel(
                        key = "notifications",
                        factory = notificationsFactory,
                    )
                    NotificationsRoute(
                        viewModel = notificationsViewModel,
                        onBack = { destination = notificationsBack },
                        onOpenKudoDetail = openKudoDetail,
                        onOpenSecretBox = openSecretBox,
                        onOpenCommunityStandards = openCommunityStandardsFromNotifications,
                    )
                }
                is AppDestination.Rules -> RulesScreen(
                    onClose = { destination = AppDestination.KudosHome },
                    onWriteKudos = { destination = AppDestination.KudoCompose },
                )
                is AppDestination.DebugMenu -> DebugMenuScreen(
                    onBack = { destination = AppDestination.Home },
                    onOpenNotFound = openNotFound,
                    onOpenAccessDenied = openAccessDenied,
                )
                is AppDestination.NotFound -> NotFoundScreen(
                    onBack = { destination = errorBack },
                    onGoHome = { destination = AppDestination.Home },
                )
                is AppDestination.AccessDenied -> AccessDeniedScreen(
                    onBack = { destination = errorBack },
                    onGoHome = { destination = AppDestination.Home },
                )
                is AppDestination.OpenBox -> {
                    val openBoxFactory = viewModelFactory {
                        initializer { OpenBoxViewModel(kudosRepository = services.kudosRepository) }
                    }
                    val openBoxViewModel: OpenBoxViewModel = viewModel(
                        key = "open-box",
                        factory = openBoxFactory,
                    )
                    OpenBoxRoute(
                        viewModel = openBoxViewModel,
                        onBack = {
                            // Stats may have changed (counter, opened total) — refetch.
                            kudosHomeViewModel.refresh()
                            destination = AppDestination.KudosHome
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
    data object KudosHome : AppDestination
    data object AllKudos : AppDestination
    data class KudoDetail(val id: String) : AppDestination
    data object KudoCompose : AppDestination
    data object SunnerSearch : AppDestination
    data object CommunityStandards : AppDestination
    data object Rules : AppDestination
    data object OpenBox : AppDestination
    data object Notifications : AppDestination
    data object DebugMenu : AppDestination
    data object NotFound : AppDestination
    data object AccessDenied : AppDestination
}
