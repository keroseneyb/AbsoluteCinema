package com.kerosene.absolutecinema.presentation.navigation

sealed class Screen(
    val route: String,
) {

    object Home : Screen(ROUTE_HOME)
    object Search : Screen(ROUTE_SEARCH)
    object Library : Screen(ROUTE_LIBRARY)

    object MovieDetails : Screen(ROUTE_MOVIE_DETAILS) {

        private const val KEY_MOVIE_ID = "movieId"
        private const val ROUTE_PATH = "details"

        fun getRoutePattern(graphRoute: String): String {
            return "$graphRoute/$ROUTE_PATH/{$KEY_MOVIE_ID}"
        }

        fun getRouteWithArgs(graphRoute: String, movieId: String): String {
            return "$graphRoute/$ROUTE_PATH/$movieId"
        }
    }

    object EditNote : Screen(ROUTE_EDIT_NOTE) {

        private const val ROUTE_BASE = "edit_note"

        fun getRouteWithArgs(movieId: String): String {
            return "$ROUTE_BASE/$movieId"
        }
    }

    object HomeGraph : Screen(ROUTE_HOME_GRAPH)
    object SearchGraph : Screen(ROUTE_SEARCH_GRAPH)
    object LibraryGraph : Screen(ROUTE_LIBRARY_GRAPH)

    companion object {

        const val KEY_MOVIE_ID = "movieId"

        const val ROUTE_HOME = "home"
        const val ROUTE_SEARCH = "search"
        const val ROUTE_LIBRARY = "library"
        const val ROUTE_MOVIE_DETAILS = "details/{$KEY_MOVIE_ID}"
        const val ROUTE_EDIT_NOTE = "edit_note/{$KEY_MOVIE_ID}"

        const val ROUTE_HOME_GRAPH = "home_graph"
        const val ROUTE_SEARCH_GRAPH = "search_graph"
        const val ROUTE_LIBRARY_GRAPH = "library_graph"
    }
}