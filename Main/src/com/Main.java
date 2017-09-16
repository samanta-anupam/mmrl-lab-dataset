package com;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
	private static String FILENAME = "";
	private static String OUT_FILENAME = "out";

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

			br = new BufferedReader(new FileReader(FILENAME));
			fw = new FileWriter(OUT_FILENAME+"_"+FILENAME);
			bw = new BufferedWriter(fw);
			bw.write("[");

			while ((sCurrentLine = br.readLine()) != null) {
				String[] array = sCurrentLine.split(" ");
				System.out.print(count++);
				try {
					bw.write(sendGet(array[0], args[1]) + ",");
				} catch (Exception e) {
					System.err.println("Photo doesnt exist");
					e.printStackTrace();
				}
			}
			bw.write("]");

		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	private static String sendGet(String photoId, String api_key)
			throws Exception {
		final String USER_AGENT = "Mozilla/5.0";
		// https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=bc8f6ae315cbf3406db96933ddb26c4a&photo_id=3330096285&format=json&api_sig=289cd0612f1ca264cc31d94cf729cb4f
		// https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=0767b9908612af193b445355d1a16f68&photo_id=3330096285&format=json&nojsoncallback=1
		String url = " https://api.flickr.com/services/rest/?"
				+ "method=flickr.photos.getInfo&api_key=" + api_key
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
		return response.toString();

	}
}
