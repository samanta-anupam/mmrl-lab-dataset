package com;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveImageUrlClass {
	private static String FILENAME = "";
	private static String OUT_FILENAME = "image_url";

	public static void main(String[] args) {

		FILENAME = FILENAME + args[0];

		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		int count = 1;

		try {

			fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

			String sCurrentLine;

			fw = new FileWriter(OUT_FILENAME+"_"+FILENAME);
			bw = new BufferedWriter(fw);

			while ((sCurrentLine = br.readLine()) != null) {
				String[] array = sCurrentLine.split("/");
				System.out.print(count++);
				try {
					bw.write(array[array.length-2]+","+ retrievePhotoUrl(array[array.length-1], args[1]));
					bw.newLine();
				} catch (Exception e) {
				    // Error when either not getting response, or either the image cannot be downloaded because of user
                    // restrictions.
					System.err.println("Error:-1");
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.err.println("File doesnt exist");
			e.printStackTrace();
		}  finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

	private static String retrievePhotoUrl(String photoId, String api_key)
			throws Exception {
		final String USER_AGENT = "Mozilla/5.0";
        // https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=e12f189dcfbb0b04ec73d261e3fb64df&photo_id=404831301&format=json&nojsoncallback=1&api_sig=ad52b34a91167347e6669074e1dc47fa
    	String url = " https://api.flickr.com/services/rest/?"
				+ "method=flickr.photos.getSizes&api_key=" + api_key
				+ "&photo_id=" + photoId + "&format=json&nojsoncallback=1";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.print(" GET : " + url);
		System.out.println(" Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
        JSONObject responseObject = new JSONObject(response.toString());
        System.out.println(responseObject.toString());
        JSONObject sizesObject = responseObject.getJSONObject("sizes");
        String imageFarmUrl="";
        if(responseObject.has("stat") && responseObject.getString("stat").equals("ok")) {
            int canDownload = sizesObject.getInt("candownload");
            int len = sizesObject.getJSONArray("size").length();
            if (canDownload==0) {
                JSONObject image = sizesObject.getJSONArray("size").getJSONObject(len-1);
                imageFarmUrl = image.getString("source");
                imageFarmUrl.replaceAll("\\\\","");
            }
        }
		return imageFarmUrl;

	}
}
