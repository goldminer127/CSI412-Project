public class OS implements OSInterface
{
    private static OS osInstance = null;
    PriorityScheduler scheduler = new PriorityScheduler();
    MemoryManagement memoryManagement = MemoryManagement.getMemoryManagement();
    MutexManager mutexManager = MutexManager.getMutexManager();

    /* Makes OS Singleton */
    private OS()
    {

    }

    public static OS GetOS()
    {
        if(osInstance == null)
        {
            osInstance = new OS();
        }
        return osInstance;
    }

    public int createProcess(UserlandProcess process, PriorityEnum priority)
    {

        return scheduler.createProcess(process, priority);
    }
    public boolean deleteProcess(int processId)
    {
        
        return scheduler.deleteProcess(processId);
    }
    public void sleep(int milliseconds)
    {
        switch(scheduler.runningProcess.priority)
        {
            case RealTime:
            scheduler.realtimeProcesses.remove(scheduler.runningProcess.processID);
            break;
            case Interactive:
            scheduler.interactiveProcesses.remove(scheduler.runningProcess.processID);
            break;
            case Background:
            scheduler.backgroundProcesses.remove(scheduler.runningProcess.processID);
            break;
        }
        scheduler.runningProcess.timeLeftToSleep = milliseconds;
        scheduler.sleepingProcesses.add(scheduler.runningProcess);
    }
    public void run()
    {
        scheduler.run();
    }

    public int open(String s)
    {
        int id = VFS.getVFS().open(s);
        scheduler.runningProcess.vfsId.add(id);
        return id;
    }
    public void close(int id)
    {
        scheduler.runningProcess.vfsId.remove(Integer.valueOf(id));
        VFS.getVFS().close(id);
    }
    public byte[] read(int id, int size)
    {
        return VFS.getVFS().read(id, size);
    }
    public void seek(int id, int to)
    {
        VFS.getVFS().seek(id, to);
    }
    public int write(int id, byte[] data)
    {
        return VFS.getVFS().write(id, data);
    }

    public void writeMemory(int address, byte value) throws RescheduleException
    {
        memoryManagement.writeMemory(address, value);
    }

    public byte readMemory(int address) throws RescheduleException
    {
        return memoryManagement.readMemory(address);
    }

    public int sbrk(int amount)
    {
        return memoryManagement.sbrk(amount);
    }

    public int attachToMutex(String name)
    {
        return mutexManager.attachToMutex(name);
    }
    public boolean lock(int mutexId)
    {
        return mutexManager.lock(mutexId);
    }
    public void unlock(int mutexId)
    {
        mutexManager.unlock(mutexId);
    }
    public void releaseMutex(int mutexId)
    {
        mutexManager.releaseMutex(mutexId);
    }
}
