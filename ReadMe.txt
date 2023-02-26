The purpose of this project is completing Angular demonstration successfully in functional and design aspects.

Lofi project will provide a user with a playlist management features in a web application with stored lofies.
Users can make their own playlist by pulling lofies from lofi pools or search lofies and assign it to their desired playlists.

Angular framework has been used for building client-side.
Spring-boot framework has been used for building server-side.

To build the application in both sides.
    1. Spring boot CLI / Angular CLI should be installed
    1. My SQL setup is required / refer to application.properties
        -> For windows OS, login mysql by ~mysql.exe -uYourUserName -pYourPassword
        -> find and run ddl / dml
    2. Run the commands below after locating to the backend folder
        -> the build command is ~ mvn spring-boot:run
        -> the packaging command is ~ mvn package -> jar file will be created
    3. Run the build command below after locating to the frontend folder
        -> the build command is ~ ng serve --host 0.0.0.0
        -> the packaging command is ~ ng build --configuration production -aot
        -> To deploy, go to dist/project_lofi and ~ ng serve --host 0.0.0.0