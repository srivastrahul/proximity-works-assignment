# proximity-works-assignment
App architecture:
1. The app has been developed using the MVVM pattern with dependency injection through Dagger.
2. Data has been fetched through the use of web sockets implemented using OkHttp library through an abstract layer using Kotlin Channels & Coroutines.
3. The app consists of a single view model which has been created through an injection of the ViewModelFactory in the MainActivity class.
4. The same view model instance has been shared with the Bottom Sheet Fragment through the implemention of activityViewModels() in the Fragment KTX library.

App logic:
1. Every Web Socket emit is parsed into a JSON array.
2. The AQI entry for every city is stored, along with the timestamp of emit, in an ArrayList within a HashMap where city is the key. This gives us all the AQIs for a given city.
3. An ArrayList with the city name, latest AQI index & timestamp of emit is created. This ArrayList is passed to the recycler view adapter.
4. DiffUtil has been implemented in the RecyclerView adapter to refresh the RecyclerView with minimal possible changes.
5. Data binding has been used to populate every item in the RecyclerView.
6. Item click in the RecyclerView has been detected by passing a lambda (defined in the Activity class) as a parameter in the RecyclerView adapter instead of implementing an interface for item click.
7. On item click, a Modal Bottom Sheet is displayed.
8. The Bottom Sheet shows a Bar Chart & a Sparkline of the 6 latest AQI indexes (seperated by a 30s interval) for the selected city.

Graph Libraries:
1. MPAndroid chart has been used for the Bar Chart as it is one of the most forked/downloaded libraries on Github. Also, the StackOverFlow community support is tremendous for the library.
2. The library used for generating the Sparkline has been used because of its concise implementation.
