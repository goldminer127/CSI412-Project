public class Pipe1Process extends UserlandProcess
{
    public RunResult run()
    {
        int randomId = OS.GetOS().open("random 7");
        int pipe = OS.GetOS().open("pipe test-pipe");
        OS.GetOS().write(pipe, OS.GetOS().read(randomId, 10000));
        OS.GetOS().close(randomId);

        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 40;
        return result;
    }
}
