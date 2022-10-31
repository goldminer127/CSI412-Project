public class TestDevices extends UserlandProcess
{
    public RunResult run() throws RescheduleException
    {
        int randomId = OS.GetOS().open("random 3");
        byte[] bytes = OS.GetOS().read(randomId, 1900);
        System.out.println(OS.GetOS().memoryManagement.sbrk(2000));
        for(int i = 0; i < bytes.length; i++)
        {
            OS.GetOS().memoryManagement.writeMemory(10+i, bytes[i]);
        }

        for(int i = 0; i < bytes.length; i++)
        {
            //System.out.print(OS.GetOS().memoryManagement.readMemory(10+i) + " ");
        }
    

        OS.GetOS().close(randomId);
        
        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 480;
        return result;
    }
    
}
