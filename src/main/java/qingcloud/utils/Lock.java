package qingcloud.utils;

public interface Lock {
    public boolean tryLock();

    public void unlock();
}
