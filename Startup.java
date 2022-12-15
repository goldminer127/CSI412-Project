public class Startup
{
    public static void main (String [] args)
    {
        OS.GetOS().createProcess(new TestDevices(), PriorityEnum.RealTime);
        OS.GetOS().createProcess(new TestDevices2(), PriorityEnum.RealTime);
        OS.GetOS().run();
    }
}
