public class DeviceData
{
    public Device device = null;
    public int deviceId;

    public DeviceData(Device d, int id)
    {
        device = d;
        deviceId = id;
    }

    public Device getDevice()
    {
        return device;
    }
    public int getDeviceId()
    {
        return deviceId;
    }
}