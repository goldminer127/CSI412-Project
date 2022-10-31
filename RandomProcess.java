import java.util.Arrays;

public class RandomProcess extends UserlandProcess
{
    public RunResult run()
    {
        int randomId = OS.GetOS().open("random 4");
        //System.out.println(Arrays.toString(OS.GetOS().read(randomId, 10)));
        OS.GetOS().close(randomId);
        
        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 40;
        return result;
    }
}
