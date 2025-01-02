package qingcloud.utils;

public class SimpleRedisLock implements Lock{
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void unlock() {

    }
}
