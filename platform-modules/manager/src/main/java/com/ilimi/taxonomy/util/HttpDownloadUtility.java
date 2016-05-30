package com.ilimi.taxonomy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.ekstep.ecml.slugs.Slug;

/**
 * A utility that downloads a file from a URL.
 * 
 * @author Mohammad Azharuddin
 *
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     * 
     * @param fileURL
     *            HTTP URL of the file to be downloaded
     * @param saveDir
     *            path of the directory to save the file
     */
    public static File downloadFile(String fileURL, String saveDir) {
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(fileURL);
            httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                httpConn.getContentType();
                httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                }

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                File saveFile =  new File(saveDir);
                if (!saveFile.exists()) {
					saveFile.mkdirs();
				}
                String saveFilePath = saveDir + File.separator + fileName;

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                File file = new File(saveFilePath);
                file = Slug.createSlugFile(file);
                return file;
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HttpDownloadUtility error: " + e.getMessage());
        } finally {
            if (null != httpConn)
                httpConn.disconnect();
        }
        return null;
    }
    
    public static boolean isValidUrl(Object url) {
        if (null != url) {
            try {
                new URL(url.toString());
            } catch (MalformedURLException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    public static void DeleteFiles(List<File> files) {
        for (File file : files) {
            if (file.exists() && !file.isDirectory()) {
                if (file.delete()) {
                    System.out.println(file.getName() + " is deleted!");
                }
            }
        }
    }
}
