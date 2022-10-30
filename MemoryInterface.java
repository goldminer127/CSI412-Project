public interface MemoryInterface
{
    void writeMemory(int address, byte value) throws RescheduleException;
    byte readMemory(int address) throws RescheduleException;
    int sbrk(int amount);
}
