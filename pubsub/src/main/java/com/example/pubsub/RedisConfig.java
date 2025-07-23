package com.example.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        // Create a MessageListenerAdapter that uses the MessageListenerService
        return new MessageListenerAdapter(new MessageListenerService());
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                               MessageListenerAdapter messageListenerAdapter) {
        // Create a RedisMessageListenerContainer and set the message listener adapter
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, ChannelTopic.of("users:unregister"));
        return container;
    }
}
