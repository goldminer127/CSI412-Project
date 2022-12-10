import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MutexObject
{
    public String name;
    public int id;
    public int processLocked = -1; //-1 is no proccess locked
    public ArrayList<Integer> attachedProcessesIds = new ArrayList<Integer>();
    public boolean inUse;
    public Queue<KernelandProcess> waitQueue = new LinkedList<KernelandProcess>();

    public MutexObject(String mutexName, int mutexId)
    {
        name = mutexName;
        id = mutexId;
    }
}
