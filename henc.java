//Encoding & Compression java

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class henc {

	/**
	 * E:/Project 1/Test Cases/4/1.jpeg
	 * @param path
	 */
	public boolean success =false;
	public henc(String path){
		long startTime = System.nanoTime();
		try
		{
			String sourcepath = path;
			//String sourcepath = args[0];
			System.out.println(path);

			File file = new File(sourcepath);
			System.out.println(file.length());
			if(file.isDirectory()){
				System.out.println("is Directory");

				if(emptyFile(file)){

					EmptyFolderHenc folder = new EmptyFolderHenc(path);
					success = folder.success;
					System.out.println("empty folder");

				}else{
					MultiHence multihence = new MultiHence(sourcepath);
					multihence.start();
					success = multihence.success;
				}

				file.delete();
			}
			else if(file.isFile()){
				System.out.println("is File");
				if(file.length()==0){
					EmptyFileHenc emptyFile = new EmptyFileHenc(path);
					success = emptyFile.success;
				}else {
					SingleHence singleHence = new SingleHence(sourcepath);
					singleHence.start();
					success = singleHence.success;

				}
				file.delete();
			}

		}catch(FileNotFoundException e)
		{
			System.out.println("Please enter proper filename..(at the beginning)");
			return;
		}catch (IOException E){
			System.out.println("IO Exception……");
			return;
		}
		long endTime = System.nanoTime();
		long runTime = endTime - startTime;
		System.out.println("运行时间： "+ runTime);
	}


	public static void main(String[] args) throws IOException
	{
	henc pres= new henc("E:/Project 1/Test Cases1/test3 - empty file and empty folder");
	}

	public static boolean emptyFile(File file){
		File files[] = file.listFiles();
		if(files.length ==0){
			return true;
		}

		for (File f:files
			 ) {
			if(f.length()>0)
				return false;
		}

		return true;
	}


}
//int first= byteStream[0];
//int last = byteStream[byteStream.length-1];
//System.out.println("byteStream length "+byteStream.length+"     byteStream[0]:"+byteStream[0]+"     byteStream[last]"+byteStream[byteStream.length-1]);
//System.out.println("byteStream[0].get(data):"+symboltoCode.get(first)+"      byteStream[last].getdata:"+symboltoCode.get(last));