package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        try (var jedis = jedisPool.getResource()) {
            redisStringDataType(jedis);
        }
    }

    public static void redisStringDataType(Jedis jedis) {
        /*
        SET : key에 value를 저장합니다. 이미 key가 존재하면 덮어씁니다.
        */
        jedis.set("jaebeom:age", "11");

        /*
        SETNX : key가 존재하지 않을 때만 value를 저장합니다.
        */
        jedis.setnx("jaebeom:age", "12");

        /*
        GET : key에 저장된 값을 조회합니다.
        */
        System.out.println("jaebeom:age = " + jedis.get("jaebeom:age"));

        /*
        MGET : 여러 key의 값을 한 번에 조회합니다.
        */
        jedis.set("jaebeom:name", "jaebeom");
        System.out.println(jedis.mget("jaebeom:age", "jaebeom:name"));

        /*
        INCR : key에 저장된 숫자 값을 1 증가시킵니다.
        INCRBY : key에 저장된 숫자 값을 지정한 만큼 증가시킵니다.
        */
        jedis.incr("jaebeom:age");
        System.out.println("jaebeom:age after incr = " + jedis.get("jaebeom:age"));
        jedis.incrBy("jaebeom:age", 5);
        System.out.println("jaebeom:age after incrby = " + jedis.get("jaebeom:age"));

        /*
        DECR : key에 저장된 숫자 값을 1 감소시킵니다.
        DECRBY : key에 저장된 숫자 값을 지정한 만큼 감소시킵니다.
        */
        jedis.decr("jaebeom:age");
        System.out.println("jaebeom:age after decr = " + jedis.get("jaebeom:age"));
        jedis.decrBy("jaebeom:age", 3);
        System.out.println("jaebeom:age after decrby = " + jedis.get("jaebeom:age"));
    }

}