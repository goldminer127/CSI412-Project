public class Background extends UserlandProcess
{
    public RunResult run()
    {
        System.out.println("Background Process");
        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 40;
        OS.GetOS().sleep(120);
        return result;
    }
}