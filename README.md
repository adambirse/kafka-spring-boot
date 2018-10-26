 # Kafka Cluster with Spring Boot.

This project is a little demonstrator of a multi node Kafka cluster with a spring boot producer and consumer.

This [link](https://better-coding.com/building-apache-kafka-cluster-using-docker-compose-and-virtualbox/
) provides a good overview of the kafka cluster setup that I have created.

In addition it makes use of a couple of a couple of gradle plugins from previous projects that I find useful for building docker images (buildDocker) and running them (composeUp / composeDown)

##### To build

- `./gradlew clean buildDocker` (buildDocker depends on build)

##### To run

- `./gradlew composeUp`

##### To stop

- `./gradlew composeDown`

##### To create a topic

Only required when kafka containers are newly created.

- `docker-compose -f docker-compose/docker-compose.yml exec kafka1 bash`
- `kafka-topics --create --zookeeper zookeeper1:2181 --replication-factor 3 --partitions 10 --topic projects`

##### To query the cluster

- `docker-compose -f docker-compose/docker-compose.yml exec worker bash`
- `kafkacat -b kafka1:9092 -L`

##### To send a message to the produder

- GET: http://localhost:8080/send

##### To see received messages

- `docker-compose -f docker-compose/docker-compose.yml logs -f  kafka-consumer`


##### Stop a cluster node
`docker-compose -f docker-compose/docker-compose.yml stop kafka1`

##### Restart a cluster node
`docker-compose -f docker-compose/docker-compose.yml restart kafka1`

##### Stop a Zookeeper

`docker-compose -f docker-compose/docker-compose.yml stop zookeeper1`

Note the system will still work if you stop all zookeepers, but once kafka clusters are stopped the system will
be unable to elect new leaders and co-ordinate.

##### Restart Zookeeper

`docker-compose -f docker-compose/docker-compose.yml restart zookeeper1`


##### Stop the Consumer

- `docker-compose -f docker-compose/docker-compose.yml stop kafka-consumer`
- Send a few messages and watch them successfully send on the producer  
- Restart the consumer and watch the sent messages appear 
`docker-compose -f docker-compose/docker-compose.yml restart kafka-consumer`
 

### Functionality still to add


- script the creation of topic to remove manual step
- Consumer error handling example
- Architecture diagram
- Update README with example of when the consumer is down / scaled up
- Update producer to send messages in bulk rather than simple GET request
- Multiple producers
