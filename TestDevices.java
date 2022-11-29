import java.util.Arrays;
import java.util.Random;

public class TestDevices extends UserlandProcess
{
    public RunResult run() throws RescheduleException
    {
        int randomId = OS.GetOS().open("random " + new Random().nextInt(1000));
        byte[] bytes = OS.GetOS().read(randomId, 500);
        OS.GetOS().memoryManagement.sbrk(1000);
        for(int i = 0; i < bytes.length; i++)
        {
            OS.GetOS().memoryManagement.writeMemory(i, bytes[i]);
        }
        //System.out.println("Array Bytes vvv");
        System.out.println(Arrays.toString(bytes));
        //System.out.println("Return Bytes vvv");
        for(int i = 0; i < bytes.length; i++)
        {
            //System.out.print(OS.GetOS().memoryManagement.readMemory(i) + " ");
        }
    
        OS.GetOS().close(randomId);

        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 480;
        return result;
    }
    
}
