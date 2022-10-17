import java.util.HashMap;

public class VFS implements Device
{
    private static VFS vfs = null;
    private int deviceIdGenerator; //Ensures device Ids don't repeat
    public HashMap<Integer,DeviceData> devices = new HashMap<Integer,DeviceData>();
    
    private VFS()
    {
        
    }

    public static VFS getVFS()
    {
        if(vfs == null)
        {
            vfs = new VFS();
        }
        return vfs;
    }

    public int open(String s)
    {
        String[] args = s.split(" ");
        switch(args[0])
        {
            case "random":
            RandomDevice randomDevice = RandomDevice.getRandomDevice();
            devices.put(deviceIdGenerator, new DeviceData(randomDevice, randomDevice.open(args[1])));
            break;
            case "pipe":
            PipeDevice pipeDevice = PipeDevice.getPipeDevice();
            devices.put(deviceIdGenerator, new DeviceData(pipeDevice, pipeDevice.open(args[1])));
            break;
            case "file":
            FakeFileSystem fileDevice = FakeFileSystem.getFileSystem();
            devices.put(deviceIdGenerator, new DeviceData(fileDevice, fileDevice.open(args[1])));
            break;
        }
        int returnVal = deviceIdGenerator;
        deviceIdGenerator++;
        return returnVal;
    }
    public void close(int id)
    {
        devices.get(id).device.close(devices.get(id).getDeviceId());
        devices.remove(id);
    }
    public byte[] read(int id, int size)
    {
        return devices.get(id).device.read(devices.get(id).deviceId, size);
    }
    public void seek(int id, int to)
    {
        devices.get(id).device.seek(devices.get(id).deviceId, to);
    }
    public int write(int id, byte[] data)
    {
        return devices.get(id).device.write(devices.get(id).deviceId, data);
    }
}
