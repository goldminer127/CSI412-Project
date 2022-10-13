public class Realtime extends UserlandProcess
{
    public RunResult run()
    {
        System.out.println("Realtime Process");
        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 20;
        OS.GetOS().sleep(30);
        return result;
    }
}