import java.util.BitSet;

import javax.lang.model.util.ElementScanner14;

public class MemoryManagement implements MemoryInterface
{
    public byte[][] physicalPages = new byte[1024][1024];
    public BitSet freeList = new BitSet(1024);
    public int tlbVirtual = -1;
    public int tlbPhysical = -1;
    private int previousMemory;

    public MemoryManagement()
    {

    }

    public void writeMemory(int address, byte value) throws RescheduleException
    {
        if(tlbVirtual == -1)
        {
            mapTLB(address);
        }
        else if(OS.GetOS().scheduler.runningProcess.virtualMemory[tlbVirtual] != tlbPhysical)
        {
            mapTLB(address);
        }
        if(tlbPhysical == -1)
        {
            throw new RescheduleException();
        }
        else
        {
            int offset = address % 1024;
            physicalPages[tlbPhysical][offset] = value;
        }
    }

    public byte readMemory(int address) throws RescheduleException
    {
        if(tlbVirtual == -1)
        {
            mapTLB(address);
        }
        else if(OS.GetOS().scheduler.runningProcess.virtualMemory[tlbVirtual] != tlbPhysical)
        {
            mapTLB(address);
        }
        if(tlbVirtual == -1)
        {
            throw new RescheduleException();
        }
        else
        {
            int offset = address % 1024;
            return physicalPages[tlbPhysical][offset];
        }
    }

    public void mapTLB(int address) throws RescheduleException
    {
        try
        {
            tlbVirtual = Math.round(address / 1024);
            tlbPhysical = OS.GetOS().scheduler.runningProcess.virtualMemory[tlbVirtual];
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new RescheduleException();
        }
    }

    public int sbrk(int amount)
    {
        int result = previousMemory;
        int pageAmount = (int)Math.ceil((float)amount / 1024);
        previousMemory = pageAmount * 1024;
        for(int i = 0; i < pageAmount; i++)
        {
            for(int virtuali = 0; virtuali < 1024; virtuali++)
            {
                if(OS.GetOS().scheduler.runningProcess.virtualMemory[virtuali] == -1)
                {
                    int freeBit = freeList.nextClearBit(0);
                    if(freeBit < 1024)
                    {
                        freeList.set(freeBit);
                        OS.GetOS().scheduler.runningProcess.virtualMemory[i] = freeBit;
                        break;
                    }
                    else
                    {
                        throw new OutOfMemoryError("Out of physical memory");
                    }
                }
            }
        }
        return result;
    }
    
    public void freeMemory(KernelandProcess process)
    {
        int[] vMemory = process.virtualMemory;
        for(int i = 0; i < vMemory.length; i++)
        {
            if(vMemory[i] != -1)
            {
                physicalPages[vMemory[i]] = new byte[1024];
                freeList.clear(vMemory[i]);
            }
        }
        tlbVirtual = -1;
        tlbPhysical = -1;
    }
}
