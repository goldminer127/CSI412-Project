public interface ProcessInterface {
    int CreateProcess(UserlandProcess myNewProcess, PriorityEnum priority);

    boolean DeleteProcess(int processId);

    void Sleep(int milliseconds);
    void run();
}

