 ## Getting Started


##### to build

- ./gradlew clean buildDocker (buildDocker includes build)

##### to run

- ./gradlew composeUp

##### to stop 

- ./gradlew composeDown

##### To create a topic

- `docker-compose -f docker-compose/docker-compose.yml exec kafka1 bash`
- `kafka-topics --create --zookeeper zookeeper1:2181 --replication-factor 3 --partitions 10 --topic projects`

##### To query the cluster

- `docker-compose -f docker-compose/docker-compose.yml exec worker bash`
- `kafkacat -b kafka1:9092 -L`

##### to send a message

- GET: http://localhost:8080/send

#####to see received messages

- `docker-compose -f docker-compose/docker-compose.yml logs -f  kafka-service`


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

### TODO 

TODO script the creation of topics


TODO handle restart of kafka servers


TODO restart handling / error handling etc

TODO separate services




