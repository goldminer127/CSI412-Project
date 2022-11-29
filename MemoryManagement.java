import java.util.BitSet;
import java.util.Random;

public class MemoryManagement implements MemoryInterface
{
    public byte[][] physicalPages = new byte[1024][1024];
    public BitSet freeList = new BitSet(1024);
    public int tlbVirtual = -1;
    public int tlbPhysical = -1;
    private int previousMemory;
    public int diskPage = 0;
    public int disk;

    public MemoryManagement()
    {
        disk = FakeFileSystem.getFileSystem().open("disk");
    }

    public void writeMemory(int address, byte value) throws RescheduleException
    {
        int vPage = Math.round(address / 1024);
        if(tlbVirtual != vPage)
            mapTLB(address);
        if(tlbPhysical == -1)
        {

            throw new RescheduleException();
        }
        else
        {
            OS.GetOS().scheduler.runningProcess.virtualMemory[tlbVirtual].isDirty = true;
            int offset = address % 1024;
            physicalPages[tlbPhysical][offset] = value;
        }
    }

    public byte readMemory(int address) throws RescheduleException
    {
        int vPage = Math.round(address / 1024);
        if(tlbVirtual != vPage)
            mapTLB(address);
        if(tlbPhysical == -1)
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
            VirtualToPhysicalMapping map = OS.GetOS().scheduler.runningProcess.virtualMemory[tlbVirtual];
            if(map.physicalPage == -1) //Was promised a page, thanks for following the rules
            {
                //Has data written on disk, guess it was old school notes
                if(map.diskPage != -1)
                {
                    map.physicalPage = assignPage();
                }
                else
                {
                    map.physicalPage = assignPage();
                }
            }
            tlbPhysical = map.physicalPage;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new RescheduleException();
        }
    }
    public int stealMemory()
    {
        KernelandProcess process = null;
        Random rand = new Random();
        int maxRange = 0; //Used to determine size of virtual memory for random page selection
        int virtual = 0;
        int physicalPage = -1;
        while(physicalPage == -1)
        {
            process = OS.GetOS().scheduler.getRandomProcess();
            for(int i = 0; i < 1024 && process.virtualMemory[i] != null; i++)
            {
                maxRange++;
            }
            if(maxRange > 0)
            {
                virtual = rand.nextInt(maxRange);
                if(process.virtualMemory[virtual] != null && process.processID != OS.GetOS().scheduler.runningProcess.processID)
                    physicalPage = process.virtualMemory[virtual].physicalPage;
            }
            maxRange = 0;
        }
        //Shows if the page is being written to disk
        //System.out.println("isDirty: " + process.virtualMemory[virtual].isDirty);
        if(process.virtualMemory[virtual].isDirty)
        {
            if(process.virtualMemory[virtual].diskPage == -1)
            {
                process.virtualMemory[virtual].diskPage = diskPage;
                System.out.println(diskPage);
                diskPage++;
            }
            FakeFileSystem.getFileSystem().seek(disk, process.virtualMemory[virtual].diskPage * 1024);
            FakeFileSystem.getFileSystem().write(disk, physicalPages[physicalPage]);
            process.virtualMemory[virtual].isDirty = false;
            process.virtualMemory[virtual].physicalPage = -1;
        }
        //Used to debug, shows which process is being stolen from, what process is running and what page is being stolen.
        //System.out.println(process.processID + " , Running: " + OS.GetOS().scheduler.runningProcess.processID + "; Page: " + physicalPage);
        return physicalPage;
    }

    public int assignPage()
    {
        int freeBit = freeList.nextClearBit(0);
        if(freeBit < 1024)
        {
            freeList.set(freeBit);
            return freeBit;
        }
        else
        {
            return stealMemory();
        }
    }

    public int sbrk(int amount)
    {
        int result = previousMemory;
        int pageAmount = (int)Math.ceil((float)amount / 1024);
        previousMemory = pageAmount * 1024;
        for(int i = 0; i < pageAmount; i++)
        {
            boolean spaceFound = false;
            for(int virtuali = 0; virtuali < 1024 && spaceFound == false; virtuali++)
            {
                if(OS.GetOS().scheduler.runningProcess.virtualMemory[virtuali] == null)
                {
                    OS.GetOS().scheduler.runningProcess.virtualMemory[virtuali] = new VirtualToPhysicalMapping();
                    spaceFound = true;
                }
            }
        }
        return result;
    }
    
    public void freeMemory(KernelandProcess process)
    {
        System.out.println("Deleting: " + process.processID);
        VirtualToPhysicalMapping[] vMemory = process.virtualMemory;
        for(int i = 0; i < vMemory.length; i++)
        {
            if(vMemory[i] != null)
            {
                freeList.clear(vMemory[i].physicalPage);
            }
        }
        tlbVirtual = -1;
        tlbPhysical = -1;
    }
}
