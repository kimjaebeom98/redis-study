package org.example;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;
import redis.clients.jedis.resps.Tuple;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        try (var jedis = jedisPool.getResource()) {
            // redisStringDataType(jedis);
            // redisListDataType(jedis);
            // redisSetDataType(jedis);
            // redisHashDataType(jedis);
            // redisSortedSetDataType(jedis);
            redisGeospatialDataType(jedis);
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

    public static void redisHashDataType(Jedis jedis) {
        /*
        HSET : 해시에 필드와 값을 추가합니다.
        */
        jedis.hset("jaebeom:profile", "name", "jaebeom");
        jedis.hset("jaebeom:profile", "age", "11");

        /*
        HGET : 해시에서 특정 필드의 값을 조회합니다.
        */
        System.out.println("jaebeom:profile name = " + jedis.hget("jaebeom:profile", "name"));

        /*
        HGETALL : 해시의 모든 필드와 값을 조회합니다.
        */
        System.out.println("jaebeom:profile = " + jedis.hgetAll("jaebeom:profile"));

        /*
        HINCRBY : 해시의 특정 필드에 저장된 숫자 값을 1 증가시킵니다.
         */
        jedis.hincrBy("jaebeom:profile", "age", 10);
        System.out.println("jaebeom:profile after hincrby = " + jedis.hgetAll("jaebeom:profile"));

        /*
        HDEL : 해시에서 특정 필드를 제거합니다.
         */
        jedis.hdel("jaebeom:profile", "age");
        System.out.println("jaebeom:profile after hdel = " + jedis.hgetAll("jaebeom:profile"));
    }

    public static void redisSortedSetDataType(Jedis jedis) {
        /*
        ZADD : 정렬된 집합에 값을 추가합니다. 점수(score)를 함께 저장합니다.
        */
        jedis.zadd("jaebeom:scores", 100.0, "alice");
        jedis.zadd("jaebeom:scores", 200.0, "bob");
        jedis.zadd("jaebeom:scores", 500.0, "jb");
        jedis.zadd("jaebeom:scores", 300.0, "charlie");

        /*
        ZRANGE : 정렬된 집합의 특정 범위의 값을 조회합니다.
        */
        System.out.println("jaebeom:scores = " + jedis.zrange("jaebeom:scores", 0, -1));

        /*
        ZRANGE WITHSCORES : 정렬된 집합의 특정 범위의 값을 점수와 함께 조회합니다.
         */
        List<Tuple> tuples = jedis.zrangeWithScores("jaebeom:scores", 0, Long.MAX_VALUE);
        tuples.forEach(i-> System.out.println("Member: " + i.getElement() + ", Score: " + i.getScore()));

        /*
        ZREM : 정렬된 집합에서 값을 제거합니다.
         */
        jedis.zrem("jaebeom:scores", "alice");
        System.out.println("jaebeom:scores after zrem = " + jedis.zrange("jaebeom:scores", 0, -1));

        /*
        ZINCRBY : 정렬된 집합에서 특정 값의 점수를 증가시킵니다.
         */
        jedis.zincrby("jaebeom:scores", 600.0, "bob");
        jedis.zrangeByScoreWithScores("jaebeom:scores", 0, Long.MAX_VALUE)
                .forEach(i -> System.out.println("Member: " + i.getElement() + ", Score: " + i.getScore()));

        /*
        ZSCORE : 정렬된 집합에서 특정 값의 점수를 조회합니다.
         */
        System.out.println("Score of bob in jaebeom:scores = " + jedis.zscore("jaebeom:scores", "bob"));

        /*
        ZCARD : 정렬된 집합의 크기를 반환합니다.
         */
        System.out.println("Size of jaebeom:scores = " + jedis.zcard("jaebeom:scores"));
    }

    public static void redisGeospatialDataType(Jedis jedis) {
        /*
        GEOADD : 지리적 위치를 추가합니다. (경도, 위도, 이름)
        */
        jedis.geoadd("jaebeom:locations", 126.9784, 37.5665, "Seoul");
        jedis.geoadd("jaebeom:locations", 127.0246, 37.5326, "Incheon");
        jedis.geoadd("jaebeom:locations", 128.5916, 35.1796, "Busan");

        /*
        GEODIST : 두 위치 간의 거리를 조회합니다.
         */
        System.out.println("Distance between Seoul and Busan = " +
                jedis.geodist("jaebeom:locations", "Seoul", "Busan", GeoUnit.KM));

        /*
        GEORADIUS : 특정 위치를 중심으로 반경 내의 위치를 조회합니다.
         */
        List<GeoRadiusResponse> responses = jedis.geosearch("jaebeom:locations", new GeoSearchParam().fromLonLat(new GeoCoordinate(126.9784, 37.5665))
                .byRadius(300, GeoUnit.KM)
                .withCoord());

        responses.forEach(response -> System.out.println("%f, %f : %s"
                .formatted(response.getCoordinate().getLongitude(), response.getCoordinate().getLatitude(), response.getMemberByString())));

    }
}