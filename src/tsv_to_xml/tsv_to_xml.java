package tsv_to_xml;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
/* 1. Every image file has to be in jpg format
 * (No capital letter JPG allowed, only jpg)*/
public class tsv_to_xml {
	static String[] listofclass = new String[100];
	static String[] listofsize = new String[100];
	static int numberofclass = 0;
	static int numberofsize = 0;

	public static void main(String[] args) {
		int numofsrcfiles = 0;
		int numofimages = 0;
		
		
		System.out.println(
				  "*******************************************\n"
				+ "*******************************************\n"
				+ "********** TSV to XMLs Converter **********\n"
				+ "*******************************************\n"
				+ "*******************************************\n"
				+ "       Made by Heemoon Yoon in 2019\n"
				+ "    Contact : heemoon.yoon@utas.edu.au\n"
				+ "          UNIVERSITY OF TASMANIA\n"
				+ "*******************************************\n"
				+ "Enter folder path which includes TSV (ex :C:/Users/user/Documents/) : "
				);
		
		Scanner sc = new Scanner(System.in);
		String srcpath = sc.nextLine();
		if(!srcpath.endsWith("\\"))
			srcpath = srcpath + "\\";
		File srcfolder = new File(srcpath);
		
		if(!srcfolder.exists()) 
			System.out.println("Invalid Path");
		
		//get list of files in the folder
		File[] listOfFiles = srcfolder.listFiles();
		numofsrcfiles = listOfFiles.length;
		numofimages = numofsrcfiles/3;
		
		/////////////////////// Start Converting//////////////////////
		int numofprocessedfiles = 0;

		for (File file : listOfFiles) {
			if(file.getName().endsWith("jpg")) {
				numofprocessedfiles++;
				
				String filename_jpg = file.getName();
				String filename = filename_jpg.substring(0,filename_jpg.lastIndexOf('.'));
				
				String labelfile = srcpath+ filename + ".bboxes.labels.tsv";
				String posfile = srcpath+ filename + ".bboxes.tsv";
				
				String xmlData[][] = GetXMLData(srcpath,filename,labelfile,posfile);
				GenerateXML(srcpath,filename, xmlData);
				
				System.out.println("["+numofprocessedfiles+"/"+numofimages+" created]"+srcpath+filename+ ".xml");
			}
		}
		
		printsummary(numofimages,numofprocessedfiles);
		
		//test folder validity
		if(numofsrcfiles<1)
			System.out.println("[warning] The folder is empty!");
		if(numofsrcfiles%3!=0)
			System.out.println("[warning] Some images are not pair");
		if(numofprocessedfiles<1)
			System.out.println("[warning] folder must be consisted of *.jpg, *.bboxes.tsv and *.bboxes.labels.tsv files");
		
		System.out.println("\nPress Any key to close");
		sc.nextLine();
	}
	
	private static String[][] GetXMLData(String srcpath, String filename, String labelfile, String posfile) {
		BufferedReader reader;
		BufferedImage bimg;
		String classes[][] = null;
		
		try{
			//read label file
			reader = new BufferedReader(new FileReader(labelfile));
			String line = reader.readLine();
			
			//get number of lines
			int numboflines = 0;
			while(line != null){
				numboflines++;
				line = reader.readLine();
			}
		
			//define array length
			classes = new String[numboflines][7];
			
			//read img file
			bimg = ImageIO.read(new File(srcpath + filename+".jpg"));
			int width = bimg.getWidth();
			int height = bimg.getHeight();
			checksize(Integer.toString(width));//
			
			//read label files again
			reader.close();
			reader = new BufferedReader(new FileReader(labelfile));
			line = reader.readLine();
			
			//read classes
			int i = 0 ;
			while(line != null){
				classes[i][0] = line;
				checkclass(line);//
				i++;
				line = reader.readLine();
			}
			reader.close();
			
			//read posfile
			reader =  new BufferedReader(new FileReader(posfile));
			line = reader.readLine();
			
			//read positions
			i = 0;
			while(line != null) {
				String positions[] = line.split("\t");
				classes[i][1] = positions[0];//xmin
				classes[i][2] = positions[1];//ymin
				classes[i][3] = positions[2];//xmax
				classes[i][4] = positions[3];//ymax
				classes[i][5] = Integer.toString(width);
				classes[i][6] = Integer.toString(height);
				i++;
				line = reader.readLine();
			}
			
		} catch (IOException e){
			System.out.println(e);
		}
		
		//return
		return classes;
	}
	
	private static void GenerateXML(String srcpath, String filename, String[][] xmlDATA) {
		String result = "";
		result = "<annotation>"+"\n"
				+"\t"+"<folder>ratdog</folder>"+"\n"
				+"\t"+"<filename>"+ filename+".jpg" +"</filename>"+"\n"
				+"\t"+"<path>"+ srcpath + filename+ ".jpg" +"</path>"+"\n"
				+"\t"+"<source>"+"\n"
				+"\t"+"\t"+"<database>Unknown</database>"+"\n"
				+"\t"+"</source>"+"\n"
				+"\t"+"<size>"+"\n"
				+"\t"+"\t"+"<width>"+ xmlDATA[0][5] +"</width>"+"\n"
				+"\t"+"\t"+"<height>"+ xmlDATA[0][6] +"</height>"+"\n"
				+"\t"+"\t"+"<depth>3</depth>"+"\n"
				+"\t"+"</size>"+"\n"
				+"\t"+"<segmented>0</segmented>"+"\n";
		
		for(int i = 0 ; i < xmlDATA.length ; i++) {
			result = result +"\t"+"<object>"+"\n"
					+"\t"+"\t"+"<name>"+ xmlDATA[i][0] +"</name>"+"\n"
					+"\t"+"\t"+"<pose>Unspecified</pose>"+"\n"
					+"\t"+"\t"+"<truncated>0</truncated>"+"\n"
					+"\t"+"\t"+"<difficult>0</difficult>"+"\n"
					+"\t"+"\t"+"<bndbox>"+"\n"
					+"\t"+"\t"+"\t"+"<xmin>"+ xmlDATA[i][1] +"</xmin>"+"\n"
					+"\t"+"\t"+"\t"+"<ymin>"+ xmlDATA[i][2] +"</ymin>"+"\n"
					+"\t"+"\t"+"\t"+"<xmax>"+ xmlDATA[i][3] +"</xmax>"+"\n"
					+"\t"+"\t"+"\t"+"<ymax>"+ xmlDATA[i][4] +"</ymax>"+"\n"
					+"\t"+"\t"+"</bndbox>"+"\n"
					+"\t"+"</object>"+"\n";
		}
		
		result = result +"</annotation>";
	
		//write file
		try (PrintWriter out = new PrintWriter(srcpath+filename+".xml")) {
		    out.println(result);
		}catch (IOException e){
			System.out.println(e);
		}
	
	}
	
	private static void printsummary(int numofimages, int numofcreatedimages) {
		System.out.println("\n<<<<<<<<<<<<<<<<< SUMMARY >>>>>>>>>>>>>>>>>\n"
				 + "Total Read images: "+ numofimages +"\n"
				 + "Total created xmls: "+numofcreatedimages+"\n"
				 + "*******************************************\n");
		System.out.println("number of class: " + numberofclass);
		for(int i = 0; i < numberofclass ; i++) {
			System.out.print("  " + listofclass[i]);
		}
		System.out.println("\nnumber of size: " + numberofsize);
		for(int i = 0; i < numberofsize ; i++) {
			System.out.print("  w:" + listofsize[i]);
		}
		System.out.println("");
	}
	
	private static void checkclass(String classname) {
		boolean newclass = true;
		for(int i = 0 ; i < numberofclass ; i++) {
			if(listofclass[i].equals(classname)) {
				i = numberofclass;
				newclass = false;
			}
		}
		
		if(newclass) {
			listofclass[numberofclass]= classname;
			numberofclass++;
			System.out.println(classname);
		}
		
	}
	
	private static void checksize(String classname) {
		boolean newclass = true;
		for(int i = 0 ; i < numberofsize ; i++) {
			if(listofsize[i].equals(classname)) {
				i = numberofsize;
				newclass = false;
				System.out.println(classname);
			}
		}
		
		if(newclass) {
			listofsize[numberofsize]= classname;
			numberofsize++;
		}
		
	}

}
