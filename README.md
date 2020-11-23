# Student-Info

Sample Android app for displaying student info data as mocked single page app with minimalistic functionality (no navigation or button clicks).

Mock data is hosted on and fetched from Firestore.

Data updates every minute -

* with same class timetable sessions for same classes,
* random parking spaces (for same hardcoded parking locations)
* reduced by one minute for each shuttle bus (hard coded buses)

Data is also re-fetched when the app is re-opened.

To display 1, 2 or 3 items per section, click on a particular section.

I have done the architecture in clean layered (application, data, domain, presentation layers) MVVM pattern with Rx for communication between layers and LiveData for updating views. ViewModels contain all the view logic and view is completely "dumb".
Uses Dagger for dependency injection and Spek for unit tests.

This kind of architecture allows for ease of testing (of all use cases, view-models/presenters, repositories and utilities/mappers), and great scalability with ease of adding new features, and maintainability as features are self contained (each in own package - only one in this case).

Most of the above is an overkill for the scale of this project, and is done just for showcasing its understanding.

I have commented the code where deemed necessary, though the aim is always to use good naming and well structured and readable code, limiting the need for the comments.
