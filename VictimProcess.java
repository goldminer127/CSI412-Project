public class VictimProcess extends UserlandProcess
{
    public RunResult run() throws RescheduleException
    {
        int randomId = OS.GetOS().open("random 3");
        byte[] bytes = OS.GetOS().read(randomId, 1024);
        OS.GetOS().memoryManagement.sbrk(1024 * 1024);
        for(int i = 0; i < bytes.length; i++)
        {
            OS.GetOS().memoryManagement.writeMemory(i * 1024, bytes[i]);
            //OS.GetOS().memoryManagement.writeMemory(i * 128, (byte)76);
        }

        for(int i = 0; i < bytes.length; i++)
        {
            //System.out.print(OS.GetOS().memoryManagement.readMemory(i * 128) + " ");
        }
    
        OS.GetOS().close(randomId);

        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 480;
        return result;
    }
    
}
