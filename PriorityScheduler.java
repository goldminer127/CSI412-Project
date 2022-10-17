import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class PriorityScheduler
{
    public HashMap<Integer, KernelandProcess> realtimeProcesses = new HashMap<Integer,KernelandProcess>();
    public HashMap<Integer, KernelandProcess> interactiveProcesses = new HashMap<Integer,KernelandProcess>();
    public HashMap<Integer, KernelandProcess> backgroundProcesses = new HashMap<Integer,KernelandProcess>();
    public ArrayList<KernelandProcess> sleepingProcesses = new ArrayList<KernelandProcess>();

    //Increments by 1 every new process. Will never repeat an ID
    public int realtimeTracker;
    public int interactiveTracker;
    public int backgroundTracker;
    public int processIDTracker;
    public KernelandProcess runningProcess;

    public int createProcess(UserlandProcess process, PriorityEnum priority)
    {
        KernelandProcess kernelProcess = new KernelandProcess();
        int processID = processIDTracker; 
  
        kernelProcess.process = process;
        kernelProcess.processID = processID;
        kernelProcess.priority = priority;
        insertProcess(kernelProcess);
 
        processIDTracker++;
        return processID;
    }
    public boolean deleteProcess(int processID)
    {
        if(realtimeProcesses.get(processID) != null)
        {
            removeDevices(realtimeProcesses.get(processID));
            realtimeProcesses.remove(processID);
            return true;
        }
        else
        {
            if(interactiveProcesses.get(processID) != null)
            {
                removeDevices(interactiveProcesses.get(processID));
                interactiveProcesses.remove(processID);
                return true;
            }
            else
            {
                if(backgroundProcesses.get(processID) != null)
                {
                    removeDevices(backgroundProcesses.get(processID));
                    backgroundProcesses.remove(processID);
                    return true;
                }
                else
                {
                    for (int i = 0; i < sleepingProcesses.size(); i++)
                    {
                        KernelandProcess kernelandProcess = sleepingProcesses.get(i);
                        if(kernelandProcess != null)
                        {
                            if(kernelandProcess.processID == processID)
                            {
                                removeDevices(sleepingProcesses.get(processID));
                                sleepingProcesses.remove(kernelandProcess);
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
        }
    }
    public void removeDevices(KernelandProcess kernelandProcess)
    {
        for(int i = 0; i < kernelandProcess.vfsId.size(); i++)
        {
            OS.GetOS().close(kernelandProcess.vfsId.get(i));
        }
    }
    public void sleep(int milliseconds)
    {
        ArrayList<KernelandProcess> processesToMove = new ArrayList<KernelandProcess>();
        for(int i = 0; i < sleepingProcesses.size(); i++)
        {
            sleepingProcesses.get(i).timeLeftToSleep -= milliseconds;
            if(sleepingProcesses.get(i).timeLeftToSleep <= 0)
            {
                processesToMove.add(sleepingProcesses.get(i));
            }
        }
        
        //Move processes out of sleep queue
        for(int i = 0; i < processesToMove.size(); i++)
        {
            sleepingProcesses.remove(processesToMove.get(i));
            insertProcess(processesToMove.get(i));
        }
    }
    public void insertProcess(KernelandProcess kernelProcess)
    {
        switch(kernelProcess.priority)
        {
            case RealTime:
            realtimeProcesses.put(kernelProcess.processID, kernelProcess);
            break;
            case Interactive:
            interactiveProcesses.put(kernelProcess.processID, kernelProcess);
            break;
            case Background:
            backgroundProcesses.put(kernelProcess.processID, kernelProcess);
            break;
        }
    }
    private PriorityEnum choosePriority()
    {
        Random rand = new Random();
        int bound;
        if(realtimeProcesses.size() > 0)
        {
            bound = 10;
            //System.out.println("Bound chosen " + bound);
            int priorityChoice = rand.nextInt(bound);
            if(priorityChoice < 6)
            {
                return PriorityEnum.RealTime;
            }
            else if(priorityChoice < 9)
            {
                return PriorityEnum.Interactive;
            }
            else
            {
                return PriorityEnum.Background;
            }
        }
        else
        {
            bound = 4;
            //System.out.println("Bound chosen " + bound);
            int priorityChoice = rand.nextInt(bound);
            if(priorityChoice < 3)
            {
                return PriorityEnum.Interactive;
            }
            else
            {
                return PriorityEnum.Background;
            }
        }
    }
    public void decrementPriority()
    {
        switch(runningProcess.priority)
        {
            case RealTime:
            runningProcess.priority = PriorityEnum.Interactive;
            realtimeProcesses.remove(runningProcess.processID);
            insertProcess(runningProcess);
            break;
            case Interactive:
            runningProcess.priority = PriorityEnum.Background;
            interactiveProcesses.remove(runningProcess.processID);
            insertProcess(runningProcess);
            break;
            default:
        }
    }
    public void run()
    {
        while(true)
        {
            if(!realtimeProcesses.isEmpty() || !interactiveProcesses.isEmpty() || !backgroundProcesses.isEmpty())
            {
                KernelandProcess kernalProcess = null;
                switch(choosePriority())
                {
                    //If statements will be removed in the future
                    case RealTime:
                    if(realtimeProcesses.size() == 0)
                    {
                        break;
                    }
                    kernalProcess = realtimeProcesses.get(realtimeProcesses.keySet().toArray()[realtimeTracker]);
                    realtimeTracker++;
                    if(realtimeTracker == realtimeProcesses.size())
                    {
                        realtimeTracker = 0;
                    }
                    break;
                    case Interactive:
                    if(interactiveProcesses.size() == 0)
                    {
                        break;
                    }
                    kernalProcess = interactiveProcesses.get(interactiveProcesses.keySet().toArray()[interactiveTracker]);
                    interactiveTracker++;
                    if(interactiveTracker == interactiveProcesses.size())
                    {
                        interactiveTracker = 0;
                    }
                    break;
                    case Background:
                    if(backgroundProcesses.size() == 0)
                    {
                        break;
                    }
                    kernalProcess = backgroundProcesses.get(backgroundProcesses.keySet().toArray()[backgroundTracker]);
                    backgroundTracker++;
                    if(backgroundTracker == backgroundProcesses.size())
                    {
                        backgroundTracker = 0;
                    }
                    break;
                }
                if(kernalProcess != null)
                {
                    /* Test delete function
                    if(processIDTracker == 3)
                    {
                        System.out.println("purge");
                        for(int i = 0; i < processIDTracker; i++)
                        {
                            deleteProcess(i);
                        }
                    }
                    */
                    runningProcess = kernalProcess;
                    RunResult result = kernalProcess.process.run();
                    sleep(result.millisecondsUsed);
                    if(result.ranToTimeout == true)
                    {
                        kernalProcess.timeOutStrikes++;
                        if(kernalProcess.timeOutStrikes == 5)
                        {
                            decrementPriority();
                        }
                    }
                }
            }
            else
            {
                //System.out.println("Time passed due to no active process");
                //Time passed 20 if no processes are active
                sleep(20);
            }
        }
    }
}
