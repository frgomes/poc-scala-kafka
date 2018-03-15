#!/bin/bash

export ZOOKEEPER_HOST=localhost
export ZOOKEEPER_PORT=2181
export KAFKA_HOST=localhost
export KAFKA_PORT=9092
export TOPIC=test

##########################
## Tested on Kafka 1.0.1 #
##########################

## Run ZooKeeper server in a separate terminal
function zookeeper_server_start {
  $KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
}

## Run Kafka server in a separate terminal
function kafka_server_start {
  $KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties
}

## Create a topic in a user terminal
function kafka_topic_create {
  $KAFKA_HOME/bin/kafka-topics.sh --zookeeper ${ZOOKEEPER_HOST}:${ZOOKEEPER_PORT} --list
  $KAFKA_HOME/bin/kafka-topics.sh --create --if-not-exists --zookeeper ${ZOOKEEPER_HOST}:${ZOOKEEPER_PORT} --partitions 1 --replication-factor 1 --topic ${TOPIC}
}

## Run a test producer in a separate terminal
function kafka_console_producer {
  $KAFKA_HOME/bin/kafka-console-producer.sh --broker-list ${KAFKA_HOST}:${KAFKA_PORT} --topic ${TOPIC}
}

## Run a test consumer in a separate terminal
function kafka_console_consumer {
  $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server ${KAFKA_HOST}:${KAFKA_PORT} --topic ${TOPIC}
}
