import java.util.BitSet;

public class MemoryManagement implements MemoryInterface
{
    public byte[][] physicalPages = new byte[1024][1024];
    public BitSet freeList = new BitSet(1024);
    public int tlpVirtual = -1;
    public int tlpPhysical = -1;
    public int previousMemory;

    public MemoryManagement()
    {

    }

    public void writeMemory(int address, byte value) throws RescheduleException
    {
        try
        {
            tlpVirtual = Math.round(address / 1024);
            tlpPhysical = OS.GetOS().scheduler.runningProcess.virtualMemory[tlpVirtual];
            int offset = address % 1024;
            physicalPages[tlpPhysical][offset] = value;
        }
        catch(Exception e)
        {
            throw new RescheduleException();
        }
    }

    public byte readMemory(int address) throws RescheduleException
    {
        try
        {
            tlpVirtual = Math.round(address / 1024);
            tlpPhysical = OS.GetOS().scheduler.runningProcess.virtualMemory[tlpVirtual];
            int offset = address % 1024;
            return physicalPages[tlpPhysical][offset];
        }
        catch(Exception e)
        {
            throw new RescheduleException();
        }
    }

    public int sbrk(int amount)
    {
        int result = previousMemory;
        previousMemory = (int)Math.ceil(amount / 1024) * 1024;
        for(int i = 0; i < (int)Math.ceil(amount / 1024); i++)
        {
            for(int virtuali = 0; virtuali < 1024; virtuali++)
            {
                if(OS.GetOS().scheduler.runningProcess.virtualMemory[i] == -1)
                {
                    int freeBit = freeList.nextClearBit(0);
                    freeList.set(freeBit);
                    OS.GetOS().scheduler.runningProcess.virtualMemory[i] = freeBit;
                }
            }
        }
        return result;
    }
    
}
