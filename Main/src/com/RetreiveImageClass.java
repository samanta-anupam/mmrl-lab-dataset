package com;


import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URL;

/*
* Java class to download all image from a file with csv containing User id, Image id, static image url
* */
public class RetreiveImageClass {
    private static String FILENAME = "";


    public static void main(String[] args) {

        FILENAME = FILENAME + args[0];

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String sCurrentLine;

            int count=1;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] array = sCurrentLine.split(",");
                try {
                    if (array.length == 3) {
                        System.out.println(""+ count++ + " " + array[0] + "/" + array[1]);
                        retrievePhoto(array);
                    }
                } catch (Exception e) {
                    // API error when saving images to server
                    System.err.println("Error:-1");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("File doesnt exist");
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void retrievePhoto(String[] csvArray) throws IOException {
        URL url = new URL(csvArray[2]);
        String extn = FilenameUtils.getExtension(url.getPath());
        InputStream is = url.openStream();
        File imageFile = new File("./image-dataset/" + csvArray[0] + "/" + csvArray[1] + "." + extn);
        imageFile.getParentFile().mkdirs();
        OutputStream os = new FileOutputStream(imageFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();

    }
}
