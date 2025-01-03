package qingcloud.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements Lock{
    private StringRedisTemplate stringRedisTemplate;
    private String key;
    public SimpleRedisLock(StringRedisTemplate stringRedisTemplate,String key) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.key = key;
    }
    String pre= UUID.randomUUID().toString(true)+"-";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }
    @Override
    public boolean tryLock(long timeoutSec) {
        //唯一表示一个jvm中的线程
        String threadId  = pre+Thread.currentThread().getId();
        Boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent("lock:" + key, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(isLock);
    }


    @Override
    public void unlock() {
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList("lock:" + key),pre+Thread.currentThread().getId());
    }
//    @Override
//    public void unlock() {
//        //获取锁中的线程标识
//        String id = stringRedisTemplate.opsForValue().get("lock:" + key);
//        String threadId = pre+Thread.currentThread().getId();
//        //判断
//        if(threadId.equals(id)){
//            stringRedisTemplate.delete("lock:"+key);
//        }
//
//    }
}
