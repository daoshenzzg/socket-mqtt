package com.yb.socket.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/14 11:11
 */
public class GzipCompression implements Compression {

    private int unit = 2048;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Override
    public byte[] compress(byte[] buffer) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(arrayOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);

        try {
            byte[] buf = new byte[unit];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                gzip.write(buf, 0, len);
            }

            gzip.finish();

            byte[] result = arrayOutputStream.toByteArray();

            return result;
        } catch (IOException e) {
            throw e;
        } finally {
            gzip.close();
            arrayOutputStream.close();
            inputStream.close();
        }
    }

    @Override
    public byte[] decompress(byte[] buffer) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        // Open the compressed stream
        GZIPInputStream gzip = new GZIPInputStream(inputStream);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Transfer bytes from the compressed stream to the output stream
            byte[] buf = new byte[unit];
            int len;
            while ((len = gzip.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            return out.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            // Close the file and stream
            gzip.close();
            out.close();
            inputStream.close();
        }
    }
}
