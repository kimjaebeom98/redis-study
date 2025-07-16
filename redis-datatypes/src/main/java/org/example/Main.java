package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        try (var jedis = jedisPool.getResource()) {
            // redisStringDataType(jedis);
            // redisListDataType(jedis);
            redisSetDataType(jedis);
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

    public static void redisListDataType(Jedis jedis) {
        /*
        LPUSH : 리스트의 왼쪽에 값을 추가합니다.
        RPUSH : 리스트의 오른쪽에 값을 추가합니다.
        */
        jedis.lpush("jaebeom:friends", "alice");
        jedis.rpush("jaebeom:friends", "bob");

        /*
        LRANGE : 리스트의 특정 범위의 값을 조회합니다.
        */
        System.out.println("jaebeom:friends = " + jedis.lrange("jaebeom:friends", 0, -1));

        /*
        LPOP : 리스트의 왼쪽에서 값을 제거하고 반환합니다.
        RPOP : 리스트의 오른쪽에서 값을 제거하고 반환합니다.
        */
        System.out.println("LPOP jaebeom:friends = " + jedis.lpop("jaebeom:friends"));
        System.out.println("RPOP jaebeom:friends = " + jedis.rpop("jaebeom:friends"));

        /*
        LLEN : 리스트의 길이를 반환합니다.
         */
        jedis.lpush("jaebeom:friends", "alice", "bob", "charlie");
        System.out.println("Length of jaebeom:friends = " + jedis.llen("jaebeom:friends"));

        /*
        LTRIM : 리스트의 특정 범위의 값을 잘라냅니다.
         */
        jedis.ltrim("jaebeom:friends", 0, 1);
        System.out.println("jaebeom:friends after ltrim = " + jedis.lrange("jaebeom:friends", 0, -1));

    }

    public static void redisSetDataType(Jedis jedis) {
        /*
        SADD : 집합에 값을 추가합니다. 중복된 값은 무시됩니다.
        */
        jedis.sadd("jaebeom:skills", "java", "python", "redis");

        /*
        SMEMBERS : 집합의 모든 값을 조회합니다.
        */
        System.out.println("jaebeom:skills = " + jedis.smembers("jaebeom:skills"));

        /*
        SREM : 집합에서 값을 제거합니다.
        */
        jedis.srem("jaebeom:skills", "python");
        System.out.println("jaebeom:skills after srem = " + jedis.smembers("jaebeom:skills"));

        /*
        SISMEMBER : 집합에 특정 값이 있는지 확인합니다.
         */
        System.out.println("Is 'java' a member of jaebeom:skills? " + jedis.sismember("jaebeom:skills", "java"));

        /*
        SINTERSECTION : 두 집합의 교집합을 구합니다.
         */
        jedis.sadd("jaebeom:other_skills", "java", "c++");
        System.out.println("Intersection of jaebeom:skills and jaebeom:other_skills = " +
                jedis.sinter("jaebeom:skills", "jaebeom:other_skills"));

        /*
        SCARD : 집합의 크기를 반환합니다.
         */
        System.out.println("Size of jaebeom:skills = " + jedis.scard("jaebeom:skills"));

    }
}