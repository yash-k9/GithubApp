# GithubApp 📱
An Android application to fetch and search the repositories from the Github REST API. App is built with modern Android components using **MVVM** architecture.
# Features
- View Repositories
- Query Repositories
- Loading/Error State
- Offline Caching
- Paging
# Built with ⚙️
- [Kotlin](https://kotlinlang.org/)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Navigation](https://developer.android.com/guide/navigation)
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
- [Retrofit](https://square.github.io/retrofit/)
- [Glide](https://github.com/bumptech/glide)

# Working

Application has two screens, Home and Search Fragment. This home and search screen is split to demonstrate Navigation. Viewmodel is shared between two fragments.
Hilt is used to provide dependencies

- Home Fragment 
> This shows the list of repo, For demonstration repositories of square was used, the endpoint can be modified. Data handling would remain same. Paging is included in this Fragment. The API response is made and the response is cached. The data is served from the Room Database. Loading and Error state is shown accordingly.
- Search Fragment 
> This fragment has search view, on submission, data is fetched and served. This fragment demonstrates handling different state from the response.


# Data Layer
```
Data                             
|_ Room Database                      
|_ Remote API     
```        

- Database serves as single source of truth. The data is fetched from the Github REST API and stored in the Room Database
- Remote API. There are two API endpoints, one for repository and another for querying.
  - View Repository -> This returns the repository for a user, currently used **Square** for demonstration purpose. This can be modified for other usecases. Data handling would be the same
  - Query Repository -> This queries the repository and finds the particular repo in name and description 
 - **Paging3** handles the Home Fragment's data.

# UI
Jetpack Navigation is used for the fragment navigation and Data for the home fragment is produced by the PagingSource

