package com.api.helpers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;



public class CSVTable {
	
	public static ArrayList<String[]> csvRead(File filename) throws IOException {
		   FileReader filereader = new FileReader(filename);
		   CSVReader csvReader = new CSVReaderBuilder(filereader).build();
		   ArrayList<String[]> allData = (ArrayList<String[]>) csvReader.readAll();
		   return allData;
		}
	
	public static void writeCsv(ArrayList<String[]> testArr, String fileName) throws IOException {
		FileWriter outputfile = new FileWriter(fileName);
		CSVWriter writer = new CSVWriter(outputfile);
		for(String[] data: testArr){
			writer.writeNext(data);
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeCsvMultipleFiles(ArrayList<ArrayList<String[]>> testArr, String[] files) throws IOException {
		
		for(int i=0;i<files.length;i++) {
			if(testArr.get(i).isEmpty()) {
				continue;
			}
			File file = new File(files[i]);
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			
			for(String[] data: testArr.get(i)){
				writer.writeNext(data);
			}
					
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

