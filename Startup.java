public class Startup
{
    public static void main (String [] args)
    {
        //OS.GetOS().createProcess(new Realtime(), PriorityEnum.RealTime);
        //OS.GetOS().createProcess(new Interactive(), PriorityEnum.Interactive);
        //OS.GetOS().createProcess(new Background(), PriorityEnum.Background);
        /* Expected output after the first iterations should look like the following
         * Hello World
         * Hello World
         * Goodbye World
         */

        //OS.GetOS().createProcess(new Pipe1Process(), PriorityEnum.RealTime);
        //OS.GetOS().createProcess(new Pipe2Process(), PriorityEnum.RealTime);
        OS.GetOS().createProcess(new VictimProcess(), PriorityEnum.RealTime);
        OS.GetOS().createProcess(new TestDevices(), PriorityEnum.RealTime);
        //OS.GetOS().createProcess(new RandomProcess(), PriorityEnum.RealTime);

        /* PLEASE READ FOR PROJECT 6
        There are some print statements in memory management to help you visualize what is happening.
        */

        OS.GetOS().run();
    }
}
