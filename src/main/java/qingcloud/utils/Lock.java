package qingcloud.utils;

public interface Lock {
    public boolean tryLock(long timeoutSec);

    public void unlock();
}
