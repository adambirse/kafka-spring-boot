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

### To create a topic

- docker exec onto on of the kafka servers.

`docker-compose exec -ti bash <container>`
`kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 3 --partitions 10 --topic projects`


`kafkacat -b kafka1:9092 -L`
TODO add command for kafka connect and how to query cluster
TODO script the creation of topics
TODO add multiple zookeepers
TODO handle restart of kafka servers
TODO add instructions for restart etc
TODO restart handling / error handling etc
TODO separate services

`docker-compose -f docker-compose/docker-compose.yml restart kafka1`



