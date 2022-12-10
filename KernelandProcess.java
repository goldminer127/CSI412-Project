import java.util.ArrayList;

public class KernelandProcess
{
    public UserlandProcess process;
    public int processID;
    public PriorityEnum priority;
    public int timeLeftToSleep;
    public int timeOutStrikes;
    public ArrayList<Integer> vfsId = new ArrayList<Integer>();
    public VirtualToPhysicalMapping[] virtualMemory = new VirtualToPhysicalMapping[1024]; //Elements set to -1 when process is created in scheduler
    public KernelandProcess(UserlandProcess uProcess, int id, PriorityEnum processPriority)
    {
        process = uProcess;
        priority = processPriority;
        processID = id;
    }
}