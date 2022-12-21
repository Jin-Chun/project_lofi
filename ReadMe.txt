The purpose of this project is completing Angular demonstration successfully in functional and design aspect.

To build the application
    1. My SQL setup is required / refer to application.properties
    2. Run the commands below after locating to the backend folder
        -> the build command is ~ mvn spring-boot:run
        -> the packaging command is ~ mvn package
    3. Run the build command below after locating to the frontend folder
        -> the build command is ~ ng serve --host 0.0.0.0
        -> the packaging command is ~ ng build --configuration production -aot
        -> To deploy, go to dist/project_lofi and ~ ng serve --host 0.0.0.0
