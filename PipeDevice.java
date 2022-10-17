public class PipeDevice implements Device
{
    private static PipeDevice pipeDevice = null;
    public PipeInstance[] pipeInstances = new PipeInstance[10];
    
    private PipeDevice()
    {
        
    }

    public static PipeDevice getPipeDevice()
    {
        if(pipeDevice == null)
        {
            pipeDevice = new PipeDevice();
        }
        return pipeDevice;
    }
    public int open(String s)
    {
        int firstEmpty = -1;
        for(int i = 0; i < pipeInstances.length; i++)
        {
            if(pipeInstances[i] == null && firstEmpty == -1)
            {
                firstEmpty = i;
            }
            else if(pipeInstances[i] != null && pipeInstances[i].getName().equals(s))
            {
                pipeInstances[i].addConnection();
                return i;
            }
        }
        if(firstEmpty != -1)
        {
            pipeInstances[firstEmpty] = new PipeInstance(s);
            return firstEmpty;
        }
        else
        {
            return -1;
        }
    }
    public void close(int id)
    {
        pipeInstances[id].closeConnection();
        if(pipeInstances[id].getConnections() < 0)
        {
            pipeInstances[id] = null;
        }
    }
    public byte[] read(int id, int size)
    {
        if(pipeInstances[id] == null)
        {
            return null;
        }
        return pipeInstances[id].read(size);
    }
    public void seek(int id, int to)
    {
        pipeInstances[id].seek(to);
    }
    public int write(int id, byte[] data)
    {
        if(pipeInstances[id] != null)
        {
            pipeInstances[id].writeData(data);
            return 0;
        }
        return 1;
    }
    
}