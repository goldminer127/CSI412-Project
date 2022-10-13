public class OS implements OSInterface
{
    private static OS osInstance = null;
    PriorityScheduler scheduler = new PriorityScheduler();

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
}
