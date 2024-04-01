
# Contact Application

This is a sample backend application, written using [Hexagon](https://hexagontk.com) toolkit. It provides simple REST API for managing contacts. Contacts are managed on a per-user basis. So the contacts operations are available after user login.

## Usage

This project uses gradle as it's build tool. The `test` task requires mongodb instance to run on `27018 (non-default)` port. You can run this container in docker before running tests, using this command:

```
docker run --name contacts-db --rm -d -p 27018:27017 mongo:4.2.0
```

Or you can skip tests with `-x test` arguments to corresponding build command.

Usefull commands:
* Build: `./gradlew build`
* Rebuild: `./gradlew clean build`
* Assemble: `./gradlew installDist`
* Run: `./gradlew run`
* Test: `./gradlew test`

## Interaction

As this project only exposes REST API, you need some external tool to interact with it. You can use `curl`, `HTTPie`, `Postman`, etc...

## Endpoints

Register user:
```
POST `/user`
```

Login user:
```
POST `/user/login`
```

Delete user:
```
DELETE `/user`
```

List contacts:
```
GET `/contacts`
```

Create contact:
```
POST `/contacts`
```

Get contact:
```
GET `/contacts/{contactId}`
```

Update contact:
```
PUT `/contacts/{contactId}`
```

Delete contact:
```
DELETE `/contacts/{contactId}`
```
