package com.mericberber.leaderboard;

import com.mericberber.leaderboard.constants.LeaderboardConstants;
import com.mericberber.leaderboard.streams.LeaderboardStreamingTopology;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class App {

    public static void main(String[] args) {

        Topology topology = LeaderboardStreamingTopology.build();
        Properties props = new Properties();


        String endpoint = String.format("%s:%d", LeaderboardConstants.APPLICATION_SERVER_HOST, LeaderboardConstants.APPLICATION_SERVER_PORT);

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "leaderboard-streaming-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, endpoint);
        props.put(StreamsConfig.STATE_DIR_CONFIG, LeaderboardConstants.STREAMING_STATE_DIR);

        KafkaStreams streams = new KafkaStreams(topology, props);
        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        // start streaming!
        streams.start();
    }
}
