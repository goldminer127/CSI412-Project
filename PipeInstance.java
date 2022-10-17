import java.util.ArrayList;

public class PipeInstance
{
    private ArrayList<Byte> data = new ArrayList<Byte>();
    private int startingPos = 0;
    private String name = null;
    private int totalConnections;

    public PipeInstance(String pipeName)
    {
        name = pipeName;
        totalConnections++;
    }

    public void writeData(byte[] pipeData)
    {
        for(int i = 0; i < pipeData.length; i++)
        {
            data.add(pipeData[i]);
        }
    }

    public byte[] read(int length)
    {
        byte[] retrievedData = new byte[length];
        for(int i = 0; i < length; i++)
        {
            retrievedData[i] = data.get(startingPos + i);
        }
        startingPos += length;
        return retrievedData;
    }

    public void seek(int num)
    {
        startingPos += num;
    }

    public void addConnection()
    {
        totalConnections++;
    }

    public void closeConnection()
    {
        totalConnections--;
    }

    public String getName()
    {
        return name;
    }

    public int getConnections()
    {
        return totalConnections;
    }
}
