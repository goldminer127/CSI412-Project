import java.util.Random;
import java.lang.Integer;

public class RandomDevice implements Device
{
    private static RandomDevice randomDevice = null;
    public Random[] randomDevices = new Random[10];

    private RandomDevice()
    {
        
    }

    public static RandomDevice getRandomDevice()
    {
        if(randomDevice == null)
        {
            randomDevice = new RandomDevice();
        }
        return randomDevice;
    }

    public int open(String s) 
    {
        for(int i = 0; i < randomDevices.length; i++)
        {
            if(randomDevices[i] == null)
            {
                if(s == null)
                {
                    randomDevices[i] = new Random();
                }
                else
                {
                    randomDevices[i] = new Random(Integer.parseInt(s));
                }
                return i;
            }
        }
        return -1; //return -1 if array is full
    }
    public void close(int id) 
    {
        if(id < randomDevices.length)
        {
            randomDevices[id] = null;
        }
    }
    public byte[] read(int id, int size) 
    {
        if(randomDevices[id] != null)
        {
            byte[] arr = new byte[size];
            randomDevices[id].nextBytes(arr);
            return arr;
        }
        return null;
    }
    public void seek(int id, int to) 
    {
        Random rand = randomDevices[id];
        byte[] arr = new byte[to];
        for(int i = 0; i < to; i++)
        {
            arr[i] = (byte)randomDevices[id].nextInt();
        }
        rand.nextBytes(arr);
    }
    public int write(int id, byte[] data) 
    {
        return 0;
    }
}