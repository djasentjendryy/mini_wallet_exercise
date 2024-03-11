# Mini Wallet Excercise

## How to run the project

1. intalls the following dependencies
    - Docker
    - Java 17 or higher
2. Clone the repository
3. Run the following command in the terminal
```bash
docker-compose up #to initialize the database and redis

./gradlew bootRun #to run the application
```

## Project Structure
    ├── build.gradle                                              #Gradle dependencies
    ├── docker-compose.yml                                        #Docker compose file to run the database and redis
    ├── gradle.lockfile                                           #Gradle lock file
    ├── migrations
    │   └── init.sql                                              #Database schema
    ├── readme.md
    └── src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── mini_wallet_exercise
        │   │           ├── MiniWalletExerciseApplication.java    #Springboot application
        │   │           ├── apis                                  #APIs request and response classes
        │   │           ├── configs                               #Config application and Bean
        │   │           ├── constants                             #Constants
        │   │           ├── controllers                           #Controllers to handle the request (API)
        │   │           ├── dao                                   #Data Access Object contains the repository and entity
        │   │           ├── filter                                #Filter to handle the request and response
        │   │           ├── services                              #Services to handle the business logic
        │   │           └── utils                                 #Utility classes
        │   └── resources
        │       ├── application.properties                        #Application properties or configuration
        └── test                                                  #TODO: add unit test 
