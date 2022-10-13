public class Interactive extends UserlandProcess
{
    public RunResult run()
    {
        System.out.println("Interactive Process");
        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 40;
        OS.GetOS().sleep(40);
        return result;
    }
}