
# NIT Raipur Classlocator Spring System

The ClassLocator Backend System is a Spring Boot-based backend that helps students and faculty manage classroom information, including names and descriptions. It provides a secure and efficient way to update room details with role-based authentication using JWT.

## 1️⃣ User Authentication & Authorization (Spring Security + JWT)

- Super Admin can approve query requests and map updates.
- Admin can raise and vote a query.
- Implements a Entry Point to handle invalid login attempts with proper error responses.

## 2️⃣ Map Management & Download System

- Handles versioned map updates, ensuring users download the latest map when required.
- Efficiently fetches the map data from a MongoDB collection using searchTool.findAll().

## 3️⃣ Logging & Debugging (SLF4J + Logback)

- Logs all critical operations with context-aware messages.
- Uses:
  - **log.info()** for successful operations.
  - **log.warn()** if unwanted operations carried out but transactional and doesn't effect the backend.
  - **log.error()** requires backend engineer to repair the system as operations carried out may not be transactional.
- Logs are saved in the **\src\Classlocator.log** directory by default. The log file path can be customized in **logback.xml**.

## Run the ClassLocator Backend System 🚀

1️⃣ Prerequisites
Before running the project, ensure you have the following installed:

- ✅ Java 17+ (Recommended)
- ✅ Maven 3+ (Build and dependency management)
- ✅ MongoDB (Database)
- ✅ Postman or cURL (For API testing)

2️⃣ Clone the Repository

```bash
git clone https://github.com/danujkumar/classlocator-backend.git
cd classlocator-backend
```

3️⃣ Configure Environment Variables

- Create a **.env** file or update **application.properties** inside src/main/resources.
- Environments
  - Development
    - **application-dev.yml** contains the development configuration
    - Mongo Configuration
      - MongoDB Compass
      - host: **localhost**
      - port: **27017**
    - Server Configuration
      - port: **8080**
      - servlet:
        - context-path: **/dev**
  - Production
    - **application-prod.yml** contains the production configuration
    - Mongo Configuration
      - MongoDB Atlas
      - uri: Atlas provided uri
    - Server Configuration
      - port: **8081**
  - General Configuration
    - **application.properties** contains the overall configuration
    - App: **Classlocator**
    - Profiles: **dev** or **prod** to be configured by developer
    - MongoDB: **Classlocator**
  - Note: **The configurations are overridden by application.properties if the same configurations are provided in both application-dev.yml or application-prod.yml and application.properties.**

4️⃣ Build the Project

```bash
mvn clean install
```

 or

```bash
./mvnw clean install
```

5️⃣ Run the Application

- Development mode:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

or

```bash
java -jar target/ClassLocator-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

- Production mode:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

or

```bash
java -jar target/ClassLocator-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

6️⃣ Verify the Application is Running

```bash
curl -X GET {uri}
```

- Expected Response:

```bash
{"status": "UP"}
```

7️⃣ Stop the Server

```bash
pkill -f ClassLocator
```

## API Reference

### Admin APIs

#### 1. Signup or register

```
  POST /admin/signup
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `rollno` | `number` | **Required:** 8 digit roll number |
| `password` | `string` | **Required** |
| `name` | `string` | **Optional** |
| `new_pass` | `string` | **Optional** |
| `department` | `string` | **Optional** |

- Expected Output
  - If new user registers, JWT token generated

    ```
    { "created": "eyJhbGciO..." }
    ```

  - If user details updated

    ```
    { "updated": "eyJhbGciO..." }
    ```

  - Note: If **new_pass** is provided and authorization is successful then the value provided to **new_pass** will be new password.

#### 2. Login or generate new token

```
  POST /admin/login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `rollno`      | `number` | **Required**. 8 digit roll number |
| `password`      | `string` | **Required**. |

- Expected Output

```
  { "Token": "eyJhbGciOiJIUzI1NiJ9..." }
```

#### 3. Query Raise

```
  POST /request/raiseQuery
```

| Authorization | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Basic`      | `Login Form` | **Required**. Rollno and Password |
| `Bearer`      | `Token` | **Required**. JWT Token|

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Roomid`      | `number` | **Required**. |
| `Name`      | `string` | **Required**. |
| `Description`      | `string` | **Required**. |

- Expected Output

```
  Query raised.
```

#### 4. Voting

```
  POST /request/vote?id={queryId}
```

| Authorization | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Basic`      | `Login Form` | **Required**. Rollno and Password |
| `Bearer`      | `Token` | **Required**. JWT Token|

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `queryId`      | `string` | **Required**. |

- Expected Output

```
  Vote successful.
```

### Super Admin APIs

#### 1. Activate or create Super Admin (Only once)

```
  POST /sadmin/signup
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | **Optional** |
| `password` | `string` | **Required** |
| `new_pass` | `string` | **Optional** |
| `department` | `string` | **Optional** |

- Expected Output
  - If new user registers, JWT token generated

    ```
    { "created": "eyJhbGciO..." }
    ```

  - If user details updated

    ```
    { "updated": "eyJhbGciO..." }
    ```

  - Note: If **new_pass** is provided and authorization is successful then the value provided to **new_pass** will be new password.

#### 2. Login or generate new token

```
  POST /sadmin/login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `password`      | `string` | **Required**. |

- Expected Output

```
  { "Token": "eyJhbGciOiJIUzI1NiJ9..." }
```

#### 3. Query Raise

```
  POST /requests/raiseQuery
```

| Authorization | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Basic`      | `Login Form` | **Required**. 1 and Password |
| `Bearer`      | `Token` | **Required**. JWT Token|

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Roomid`      | `number` | **Required**. |
| `Name`      | `string` | **Required**. |
| `Description`      | `string` | **Required**. |

- Expected Output

```
  Query raised.
```

#### 4. Voting

```
  POST /requests/approve?id={queryId}
```

| Authorization | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Basic`      | `Login Form` | **Required**. 1 and Password |
| `Bearer`      | `Token` | **Required**. JWT Token|

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `queryId`      | `string` | **Required**. |

- Expected Output

```
  Approved
```

#### 5. Deactivate Admin

```
  DELETE /requests/remove
```

| Authorization | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `Basic`      | `Login Form` | **Required**. 1 and Password |
| `Bearer`      | `Token` | **Required**. JWT Token|

- Expected Output

```
  Super Admin deactivated
```

### General APIs

#### 1. Initial Checks

```
  GET /check
```

- Expected Output

```
  Yeah..., the setup is running...
```

#### 2. All Queries

```
  GET /getAllQueries
```

- This API returns a structured list of queries raised by the user (Me) and other users (Others).
Each query contains an ID, date, description, raisedBy, and status.

- Expected Output

```
  {
    "Others": {
        "Pending": [
            {
                "id": {
                    "timestamp": 1739119651,
                    "date": "2025-02-09T16:47:31.000+00:00"
                },
                "date": "2025-02-09 22:17:31",
                "name": "S49",
                "description": "It is new.",
                "raisedBy": "1",
                "votes": {},
                "superAdmin": false,
                "Roomid": 119
            }
        ],
        "Accepted": [
            {
                "id": {
                    "timestamp": 1739119153,
                    "date": "2025-02-09T16:39:13.000+00:00"
                },
                "date": "2025-02-09 22:15:41",
                "name": "S47",
                "description": "It is new.",
                "raisedBy": "1",
                "votes": {},
                "superAdmin": true,
                "Roomid": 113
            }
        ]
    },
    "Me": {
        "Pending": [],
        "Accepted": [
            {
                "id": {
                    "timestamp": 1739106140,
                    "date": "2025-02-09T13:02:20.000+00:00"
                },
                "date": "2025-02-09 18:33:09",
                "name": "New name",
                "description": "Something 3",
                "raisedBy": "21116028",
                "votes": {},
                "superAdmin": true,
                "Roomid": 95
            },
            {
                "id": {
                    "timestamp": 1739106214,
                    "date": "2025-02-09T13:03:34.000+00:00"
                },
                "date": "2025-02-13 22:12:27",
                "name": "New name 3",
                "description": "Something 3",
                "raisedBy": "21116028",
                "votes": {
                    "21116029": true
                },
                "superAdmin": true,
                "Roomid": 195
            }
        ]
    }
}
```

- Note: If not authorized, only other queries will be shown.

#### 3. Generating Raw Map

```
  GET /generate
```

| Environment | Authorization     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `dev`      | `Not required` | **Optional**. |
| `prod`      | `Super Admin` | **Required**.|

- Expected Output

```
  Generated successfully...
```

#### 4. Generating JSON file with version

```
  GET /map
```

| Environment | Authorization     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `dev`      | `Not required` | **Optional**. |
| `prod`      | `Super Admin` | **Required**.|

- Expected Output

```
  JSON File Generated...
```

#### 5. Download Map

```
  GET /download/{version}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `version`      | `Number` | **Required**. |

- Expected Output

```
  {"230":{"name":"F52","details":"Classroom, Dept. of Biotechnology"}}
```

## Error Handling

### Error Levels

#### Positive Level

- Level 1 or 0, no action required APIs processed as required.

#### Negative Level

- Level -1: Warning - The value provided to the APIs may be inaccurate or inappropriate.
- Level -2: Warning - The query or the map fault database ACID properties intacted, no action required, client side error **4xx**.
- Level -3: Error - Internal server errors, action required, ACID property compromised, server side error **5xx**.

### Error Types

#### Authorization and Authentication errors

- Error Code : **406 (NOT_ACCEPTABLE)**
  - Token not matched or expired
  - Output:

    ```bash
    Invalid Token
    ```

- Error Code : **401 (UNAUTHORIZED)**
  - Wrong user name password
  - Output:

    ```bash
    Invalid Username or Password
    ```

- Error Code : **400 (BAD REQUEST)**
  - No Authorization either login or token provided
  - Output:

    ```bash
    No Auth Used
    ```

- Error Code : **403 (FORBIDDEN)**
  - Authentication successful but may be accessing non-permitted APIs
  - Output:

    ```bash
    {
        "timestamp": "2025-02-13T18:21:42.985+00:00",
        "status": 403,
        "error": "Forbidden",
        "message": "Forbidden",
        "path": "/dev/request/raiseQuery"
    }
    ```

#### APIs Specific errors

- List of all possible errors that may occur after successful authorization
  - Error Code: **409 (CONFLICT)**
    - APIs affected:
      - /request/raiseQuery
      - /request/vote?id={}
      - /requests/raiseQuery
      - /admin/signup
      - /admin/login
    - Possible Causes:
      - Invalid Roll number or room number

  - Error Code: **400 (BAD_REQUEST)**
    - APIs affected:
      - /request/raiseQuery
      - /request/vote?id={}
      - /requests/raiseQuery
      - /requests/approve
    - Possible Causes:
      - Name or description of room not provided
      - Query not found

  - Error Code: **406 (NOT_ACCEPTABLE)**
    - APIs affected:
      - /request/vote?id={}
    - Possible Causes:
      - Raised user can't vote

  - Error Code: **403 (FORBIDDEN)**
    - APIs affected:
      - /admin/signup
      - /admin/login
      - /sadmin/signup
      - /sadmin/login
    - Possible Causes:
      - Wrong password or not allowed for super admins.
      - Wrong roll number or password for admins.

  - Error Code: **401 (UNAUTHORIZED)**
    - APIs affected:
      - /admin/signup
      - /admin/login
      - /sadmin/signup
      - /sadmin/login
    - Possible Causes:
      - Wrong password or not allowed for super admins.
      - Wrong roll number or password for admins.

  - Error Code: **501 (NOT_IMPLEMENTED)**
    - APIs affected:
      - /requests/approve
      - /generate
      - /download/{}
    - Possible Causes:
      - Map generation or creation related, please contact the developer.

  - Error Code: **500 (INTERNAL_SERVER_ERROR)**
    - APIs affected:
      - All APIs are affected
    - Possible Causes:
      - Some unauthorized access to the database, maybe hacking, contact developer immediately.

## Deployment

This project is deployed at

### 1. Render

- URI : <https://classlocator-latest.onrender.com>
- For check : <https://classlocator-latest.onrender.com/check>

```bash
curl -X GET https://classlocator-latest.onrender.com/check
```

- Expected Output

```
{ "message": "Yeah..., the setup is running..." }
```

### 2. AWS Lambda

- Future Deployment

## Usage

### Implementation

#### 1. React-native mobile application

```javascript
import axios from 'my-project'

function App() {
  return <Component />
  axios.post(uri, payload).then(()=>{}).catch(()=>{});
}
```

#### 2. NIT Raipur Classlocator website

- To be implemented later

## Optimizations

### Future enhancements

- OTP based authentication with email id and phone number.
- Option to reject the query and send it to trash.
- Recover the queries and to rollback the map.
- Deactivate the admin account.
- Handling Super Admin raised queries efficiently.

### Your feedbacks

- Please provide your valuable feedback to further improve this project.
- Encourage juniors to take this project further; it is completely open-source.

## Support

For support, email <anuj.as828@gmail.com> or follow me on GitHub.
