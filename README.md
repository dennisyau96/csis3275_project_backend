# csis3275_project_backend
The backend developed using Spring Boot to provide API functionality. The frontend of this project can be found on [the frontend repository](https://github.com/dennisyau96/csis3275_project_frontend).  
API base URL: `http://localhost:8082/api`

## Guide to run the backend application
* Clone this repository
* Create `env.properties` file in the root folder using the following template
```
DB_URL=<mongoDB URL>
DB_NAME=<database name>
DB_USER=<database username>
DB_PASSWORD=<database password>

JWT_SECRET_KEY=<secret for JWT>
# 1h in millisecond
JWT_EXP_TIME=<JWT expiry time>
```
* Open the project using IntelliJ IDEA
* Sync the Maven
* Run the project

