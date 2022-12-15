public interface Mutex
{
    int attachToMutex(String name);
    boolean lock(int mutexId);
    void unlock(int mutexId);
    void releaseMutex(int mutexId);
}
