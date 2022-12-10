public class MutexManager implements Mutex
{
    private static MutexManager mutexManager = null;
    public MutexObject[] mutexArr = new MutexObject[10];

    private MutexManager()
    {
        
    }

    public static MutexManager getMutexManager()
    {
        if(mutexManager == null)
        {
            mutexManager = new MutexManager();
        }
        return mutexManager;
    }

    //Does not dynamically allocate. Assumes there will never be more than 10 mutex objects required.
    //Attaches process to defined mutex. If mutex does not exist, a new one will be created.
    public int attachToMutex(String name)
    {
        boolean mutexExists = false;
        int nextNull = -1; //For if mutex does not exist
        int i = 0;
        for(i = 0; i < mutexArr.length && (mutexExists = (mutexArr[i] == null) ? false : mutexArr[i].name.equals(name)) == false; i++)
        {
            if(mutexArr[i] == null && nextNull == -1)
                nextNull = i;
        }

        if(mutexExists == false && nextNull != -1)
        {
            mutexArr[nextNull] = new MutexObject(name, nextNull);
            mutexArr[nextNull].attachedProcessesIds.add(OS.GetOS().scheduler.runningProcess.processID);
            mutexArr[nextNull].id = nextNull;
            return nextNull;
        }
        else
        {
            mutexArr[i].attachedProcessesIds.add(OS.GetOS().scheduler.runningProcess.processID);
            return mutexArr[i].id;
        }
    }

    //Locks mutex if mutex is not locked. Assigns the PID of the process calling lock as the process locking mutex if the process is
    //attached.
    //Returns true if the process locked the mutex, returns false if it is not attached or is placed on the wait queue.
    public boolean lock(int mutexId)
    {
        try
        {
            MutexObject mutex = mutexArr[mutexId];
            int PID = OS.GetOS().scheduler.runningProcess.processID;

            if(mutex.attachedProcessesIds.contains(PID))
            {
                if(mutex.inUse == false)
                {
                    mutex.inUse = true;
                    mutex.processLocked = OS.GetOS().scheduler.runningProcess.processID;
                    return true;
                }
                else
                {
                    mutex.waitQueue.add(OS.GetOS().scheduler.runningProcess);
                    OS.GetOS().scheduler.deleteProcess(OS.GetOS().scheduler.runningProcess.processID);
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            System.out.println("Mutex index "  + mutexId + " does not exist");
            return false;
        }
    }

    //Unlocks mutex and removes the process calling unlock as the process locking mutex. Locks mutex
    //to the next process in the queue. Will not run if the process calling unlock is not the process locking mutex
    public void unlock(int mutexId)
    {
        MutexObject mutex = mutexArr[mutexId];
        if(OS.GetOS().scheduler.runningProcess.processID == mutex.processLocked)
        {
            if(mutex.waitQueue.isEmpty())
            {
                mutex.inUse = false;
                mutex.processLocked  = -1;
            }
            else
            {
                KernelandProcess process = mutex.waitQueue.poll();
                mutex.processLocked = process.processID;
                switch(process.priority)
                {
                    case RealTime:
                        OS.GetOS().scheduler.realtimeProcesses.put(process.processID, process);
                        break;
                    case Interactive:
                        OS.GetOS().scheduler.interactiveProcesses.put(process.processID, process);
                        break;
                    default:
                        OS.GetOS().scheduler.backgroundProcesses.put(process.processID, process);
                        break;
                }
            }
        }
    }
    //Releases the process calling from the mutex specified. Will unlock the mutex if the process is locking the mutex.
    //Mutex is deleted if no process is attached to the mutex
    public void releaseMutex(int mutexId)
    {
        MutexObject mutex = mutexArr[mutexId];
        unlock(mutexId);
        mutex.attachedProcessesIds.remove(Integer.valueOf(OS.GetOS().scheduler.runningProcess.processID));
        if(mutex.attachedProcessesIds.size() == 0)
        {
            mutexArr[mutexId] = null;
        }
    }

    //Used by the scheduler if a process is being deleted
    public void deleteProcessFromMutex()
    {
        int processID = OS.GetOS().scheduler.runningProcess.processID;
        for(int i = 0; i < mutexArr.length; i++)
        {
            MutexObject mutex = mutexArr[i];
            if(mutex != null)
            {
                if(mutex.attachedProcessesIds.contains(processID))
                {
                    releaseMutex(i);
                }
            }
        }
    }
}
