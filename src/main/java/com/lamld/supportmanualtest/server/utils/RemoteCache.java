package com.lamld.supportmanualtest.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RDeque;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RemoteCache {

  public static final int CACHE_DURATION_DEFAULT = 3600; // 1 tieng
  public static final int CACHE_6H_DURATION = 3600 * 6; // 6 tieng
  public static final int CACHE_5MIN_DURATION = 60 * 5; // 5 min
  public static final int CACHE_1W_DURATION = 3600 * 24 * 7; // 1 week
  public static final int CACHE_3M_DURATION = 3600 * 24 * 90; // 3 month
  public static final int CACHE_1DAY_DURATION = 3600 * 24; // 1 day

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  @Qualifier("redisson")
  private RedissonClient redissonClient;

  public long pfCount(String key){
    try{
      RHyperLogLog<String> hp = redissonClient.getHyperLogLog(key);
      return hp.count();
    }catch (Exception e){
      log.error(e.getMessage());
      return 0;
    }
  }

  public long pfMergeCount(List<String> keys){
    try{
      if(!keys.isEmpty()){
        RHyperLogLog<String> hp = redissonClient.getHyperLogLog(keys.getFirst());
        for(String key : keys){
          RHyperLogLog<String> mergeKey = redissonClient.getHyperLogLog(key);
          hp.mergeWith(mergeKey.getName());
        }
        return hp.count();
      }
    }catch (Exception e){
      log.error(e.getMessage());
      return 0;
    }
    return 0;
  }

  public void rDequePutId(String key, Object value){
    try{
      RDeque<Object> queue = redissonClient.getDeque(key);
      queue.addAsync(value);
    }catch (Exception e){
      log.error(e.getMessage());
    }
  }

  public void deleteKey(String key){
    try{
      RDeque<Object> queue = redissonClient.getDeque(key);
      queue.delete();
    }catch (Exception e){{
      log.error(e.getMessage());
    }}
  }

  public <T> T rDequePeekFirst(String key){
    try{
      RDeque<T> queue = redissonClient.getDeque(key);
      return queue.peekLast();
    }catch (Exception e){{
      log.error(e.getMessage());
    }}
    return null;
  }

  public <T> T rDequePoolFirst(String key){
    try{
      RDeque<T> queue = redissonClient.getDeque(key);
      return queue.pollFirst();
    }catch (Exception e){
      log.error(e.getMessage());
    }
    return null;
  }

  public <T> List<T> rDequeGetAll(String key){
    try{
      RDeque<T> queue = redissonClient.getDeque(key);
      return queue.readAllAsync().get();
    }catch (Exception e){
      log.error(e.getMessage());
    }
    return null;
  }

  public void putExpireMillis(String key, Object value, long expireTime) {
    try {
      redisTemplate.opsForValue().set(key, JsonParser.toJson(value), expireTime, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void put(String key, String value, int expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
  }

  public void put(String key, Object object, int expireTime) {
    //log.debug("=======save cache " + key + ": ", JsonParser.toJson(object));
    try {
      put(key, JsonParser.toJson(object), expireTime);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void put(String key, Object object) {
    try {
      put(key, JsonParser.toJson(object), CACHE_DURATION_DEFAULT);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public void put(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> ArrayList<T> getList(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.arrayList(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public Long addAndGetAtomicLong(String key, long amount) {
    try {
      RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key);
      return rAtomicLong.addAndGet(amount);
    } catch (Exception e) {
      log.error(e.getMessage());
      return 0L;
    }
  }

  public <T> Page<T> getPage(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.toPage(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public <T> T get(String key, Class<T> tClass) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.entity(value, tClass);
    } catch (Exception e) {
      return null;
    }
  }

  public Boolean exists(String key) {
    return redisTemplate.hasKey(key);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }

  public void del(List<String> keys) {
    redisTemplate.delete(keys);
  }

  public Set<String> keys(String pattern) {
    return redisTemplate.keys(pattern);
  }

  // set cache
  public boolean zAdd(String key, String value, Long score) {
    return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
  }

  public boolean zAdds(String key, Set<ZSetOperations.TypedTuple<String>> typedTuples) {
    redisTemplate.opsForZSet().add(key, typedTuples);
    return false;
  }

  public void zdel(String key, String value) {
    redisTemplate.opsForZSet().remove(key, value);
  }

  // get top
  public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, Long topUser) {

    return redisTemplate.boundZSetOps(key).reverseRangeWithScores(0, topUser);
  }

  public Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String key, Long from, Long topUser) {

    return redisTemplate.boundZSetOps(key).rangeWithScores(from, topUser);
  }

  public Set<String> rangeByScore(String key, Double topUser) {

    return redisTemplate.boundZSetOps(key).rangeByScore(topUser, Double.MAX_VALUE);
  }


  public Long zSize(String key) {
    return redisTemplate.boundZSetOps(key).size();
  }

  // getRank
  public Long zRank(String key, String value) {
    return redisTemplate.boundZSetOps(key).reverseRank(value);
  }

  public Double zUpdatePoint(String key, String value, double score) {
    return redisTemplate.opsForZSet().incrementScore(key, value, score);
  }

  public Double score(String key, String value) {
    return redisTemplate.opsForZSet().score(key, value);
  }

  /**
   * for Millisecond
   *
   * @param key
   * @param value
   * @param expireTime
   */
  public void setInMs(String key, String value, long expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
  }

  public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresCursor(String key, Integer to, long topUser) {

    return redisTemplate.boundZSetOps(key).reverseRangeWithScores(to, topUser);
  }
}
