import java.util.Arrays;

public class Pipe2Process extends UserlandProcess
{
    public RunResult run()
    {
        int pipe = OS.GetOS().open("pipe test-pipe");
        int fileSystem = OS.GetOS().open("file data.txt");
        for(int i = 0; i < 10000; i += 2)
        {
            OS.GetOS().write(fileSystem, Arrays.toString(OS.GetOS().read(pipe, 2)).getBytes());
        }
        System.out.print(Arrays.toString(OS.GetOS().read(fileSystem, 10)));
        OS.GetOS().close(fileSystem);

        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 40;
        return result;
    }
}
