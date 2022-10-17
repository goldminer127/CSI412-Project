public interface ProcessInterface
{
    int createProcess(UserlandProcess myNewProcess, PriorityEnum priority);

    boolean deleteProcess(int processId);

    void sleep(int milliseconds);
    void run();
}

