package com.ir.project2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * This is a utility class used for Reading and Writing a File.
 */
public class FileParserUtil {

	public static class StaticFileParserUtil {
		/**
		 * @param String fileName.
		 * @return Read the file and return an ArrayList of Lines in the given file.
		 */
		public static ArrayList<String> irFileParserUtil(String fileName) {

			String line = null;

			try {
				ArrayList<String> lineList = new ArrayList<String>();
				File file=new File(fileName);
				InputStream is=new FileInputStream(file);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				bufferedReader.mark(4);
				if('\ufeff'==bufferedReader.read()){
					System.out.println("BOM Detected");
				}else{
					bufferedReader.reset();
				}

				while ((line = bufferedReader.readLine()) != null) {
					lineList.add(line);
				}

				// Always close files.
				bufferedReader.close();
				return lineList;
			} catch (FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");
				return null;
			} catch (IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");
				return null;
			}
		}
		/**
		 * @param String resultToWrite
		 * @param String outputFileName
		 * @return void.
		 */
		public static void irStringFileWritingUtil(String resultToWrite, String outputFileName) {
			try {

				File file = new File(outputFileName);
				if (!file.exists()) {
					file.createNewFile();
				}

//				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
//				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(file,"UTF-8");
				out.print(resultToWrite);
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
