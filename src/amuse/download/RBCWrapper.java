package amuse.download;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class RBCWrapper implements ReadableByteChannel{

    private ReadableByteChannel channel;
    private DownloadTask delegateTask;
    private int bytesRead;

    public RBCWrapper(ReadableByteChannel channel, DownloadTask delegateTask) {
        this.channel = channel;
        this.delegateTask = delegateTask;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int n;
        if ((n = channel.read(dst)) > 0) {
            bytesRead += n;
            delegateTask.setProgress(bytesRead);
        }
        return n;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
