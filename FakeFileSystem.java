import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device
{
    private static FakeFileSystem fileSystem = null;
    public RandomAccessFile[] randomAccessFiles = new RandomAccessFile[10];
    
    private FakeFileSystem()
    {
        
    }

    public static FakeFileSystem getFileSystem()
    {
        if(fileSystem == null)
        {
            fileSystem = new FakeFileSystem();
        }
        return fileSystem;
    }

    public int open(String s)
    {
        try
        {
            for(int i = 0; i < randomAccessFiles.length; i++)
            {
                if(randomAccessFiles[i] == null)
                {
                    randomAccessFiles[i] = new RandomAccessFile(s, "rw");
                    return i;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
    public void close(int id)
    {
        try
        {
            randomAccessFiles[id].close();
            randomAccessFiles[id] = null;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public byte[] read(int id, int size)
    {
        byte[] buffer = new byte[size];
        try
        {
            randomAccessFiles[id].read(buffer);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
    public void seek(int id, int to)
    {
        try
        {
            randomAccessFiles[id].seek(to);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public int write(int id, byte[] data)
    {
        try
        {
            randomAccessFiles[id].write(data);
            return 0;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return 1;
        }
    }
}
