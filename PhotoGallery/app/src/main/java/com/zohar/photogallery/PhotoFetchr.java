package com.zohar.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoFetchr {

    public byte[] getUrlByte(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

            throw new IOException(connection.getResponseMessage() + "; with " + urlSpec);
        }

        try {
            // http连接success
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            byte[] bytes = new byte[1024];
            int byteRead = 0;
            while ((byteRead = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, byteRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlByte(urlSpec));
    }
}
