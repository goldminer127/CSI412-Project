public class TestDevices extends UserlandProcess
{
    public RunResult run() throws RescheduleException
    {
        int mutexID = OS.GetOS().attachToMutex("camera");
        System.out.println("Mutex ID: " + mutexID);
        System.out.println("Process 1 locked mutex status: " + OS.GetOS().mutexManager.lock(mutexID));

        RunResult result = new RunResult();
        result.ranToTimeout = false;
        result.millisecondsUsed = 480;
        return result;
    }
    
}
