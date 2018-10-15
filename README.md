 ## Getting Started


to build

- ./gradlew clean buildDocker (buildDocker includes build)

to run

- ./gradlew composeUp

to stop 

- ./gradlew composeDown

to send a message

- GET: http://localhost:8080/send

to see received message

- tail logs of kafka-service in docker



