MovieMatch is a mobile application that wants to enhance social connections by making it easier for friends and loved ones to finally agree on a movie to watch.
At launch MovieMatch, the users are greeted with an easy onboarding process through Firebase Authentication for a quick and secure account setup.
They can also personalise their account by selecting a profile picture. Users are able to edit their profiles and can easily add friends by searching for their usernames.
After this process, the core experience revolves around a swiping screen. Users invite their friends to a matching session, before browsing through a curated selection of movies fetched from The Movie Database, swiping right to like a movie or left to pass on it. 
As users swipe through movies, MovieMatch works to keep track of liked movies so that when two or more friends all independently like the same movie, the app modifies the swiping screen and instead shows that movie as the movie they've agreed on.
It also allows users to browse a trending page and add the movies there to their favorites in their profile to come back to read more on them later.
Notifications for joining groups and friend requests are also fetched automatically and are immediately reflected in the users' accounts.

To test:
- Launch a virtual device and run the app.
- Click sign up and complete the onboarding.
- Perform the same steps on another account - two logged in users neccessary for social capability
- In the friends page, click the plus icon to open the add friend page
- Search friend by username and click the plus button to request them as a friend
- On the friend account, accept the friend request
- On the original account, add the friend to a session through the add group screen (plus icon in the groups page)
- Accept the group request on the friend account
- Navigate to the group screen and open the session by clicking its session button
- Once both users swipe right on the same movie, the session will display a match
