public class Startup
{
    public static void main (String [] args)
    {
        OS.GetOS().createProcess(new Realtime(), PriorityEnum.RealTime);
        OS.GetOS().createProcess(new Interactive(), PriorityEnum.Interactive);
        OS.GetOS().createProcess(new Background(), PriorityEnum.Background);
        /* Expected output after the first iterations should look like the following
         * Hello World
         * Hello World
         * Goodbye World
         */

        OS.GetOS().run();
    }
}