//Decoding & Decompression
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class hdec{
	static HashMap<String, Byte> CodetoSymb2 = new HashMap<String,Byte>();
    static int line1;
    static int line2;
    public static int frequencyCounter = 0;
    public static int numberOfFile;
    public static int[] lenOfmultiStream;
    public static String[]  nameOfFile;
    public static int [] lenOfFileName;
	public static void main(String[] args) throws IOException
	{
		try
		{
			String sourcepath ="E:/Project 1/Test Cases/test2 - folder/3.huf";
			//String sourcepath = args[0];
		File file = new File(sourcepath);
		recreateMap_de(file);
		System.out.println("File created..");
		//file.delete();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Please give proper .huf file..");
			
		}
		catch (ArrayIndexOutOfBoundsException a) 
		{
			System.out.println("Please enter file name");
		}
	
		
	}
	public static void fileEmpty_de(File f) throws IOException
	{
		
		String str = f.toString();
		str = str.substring(0, str.length()-4);
		BufferedOutputStream ewrite = new BufferedOutputStream(new FileOutputStream(str));
		ewrite.close();
		f.delete();
		System.exit(0);
	}

	public static void recreateMap_de(File f) throws IOException
	{
		
		BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
		if(f.length()==0)
		{
			fileEmpty_de(f);
		}
		String mapp =b.readLine();
		line1 = mapp.length();
		
		String arr[] =mapp.split(" ");
		for (int i=0; i<arr.length;i++)
		{
			String regx= arr[i];
			String a[] = regx.split("=");
			CodetoSymb2.put(a[0],Byte.parseByte(a[1]));
		}

		String strFre = b.readLine();
		if(strFre.equals("")) {
			/**
			 * 多个文件，需要拆分
			 */
			line2 += strFre.length();
			String temp = b.readLine();
			numberOfFile = Integer.parseInt(temp);
			System.out.println(numberOfFile);
            line2 += temp.length();
			lenOfFileName= new int[numberOfFile];
			lenOfmultiStream = new int[numberOfFile];
			nameOfFile = new String[numberOfFile];

			String detail[] = null;

			String read = null;
			for (int j = 0; j <numberOfFile; j++) {
				read = b.readLine();
				line2 += read.length();

			//E:\'�\Project 1\Test Cases\test2 - folder\1\6917291.htm=55 64552
			 detail= read.split("::",3);
			 nameOfFile[j] = detail[0];
			 lenOfFileName[j]=Integer.parseInt(detail[1]);
			lenOfmultiStream[j] = Integer.parseInt(detail[2]);
			}
			for(int i = 0; i<numberOfFile;i++){
				System.out.println("文件名: "+nameOfFile[i]+" 文件名长度: "+lenOfFileName[i]+" byte数: "+lenOfmultiStream[i]);

			}

			reformMui_de(nameOfFile,lenOfmultiStream,f);
		}
		else {//单个文件

			int freq = Integer.parseInt(strFre);
            reformTextfile_de(CodetoSymb2, f, freq);
		}
		b.close();
	}

	public static void reformMui_de(String[]nameOfFile, int[]lenOfmultiStream,File file) throws IOException {
		//int frequency = freq;
		StringBuilder buildCode = new StringBuilder();
		BufferedInputStream b1 = new BufferedInputStream(new FileInputStream(file));
		//b1出现了
		//b1.skip(line1);
		for (int i = 0; i < numberOfFile + 3; i++) {
			int j = 0;
			while (b1.read() != '\n') {
				j++;
			}
			;
			System.out.println(j);
		}

		int freq;
		String name = null;
        int y = 0;
        int i = 0;
        int lenOffile;
		freq = lenOfmultiStream[i];
		name = nameOfFile[i];

		File childFile = new File(name);
		if(!childFile.exists()){
			String [] rename = name.split("\\\\");
			int l = rename.length-1;
			String parent = "";
			for(int m = 0;m < l-1;m++){
				parent =parent+rename[m]+"//";
			}
			parent+=rename[l-1];
			//String leaf = rename[l];
			File parentFile = new File(parent);
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}

			 childFile.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(childFile);
		BufferedOutputStream bwrite = new BufferedOutputStream(out);
		int temp;
		while (( y =b1.read()) != -1) {
			lenOffile = (int)childFile.length();
			temp = frequencyCounter;
			if((frequencyCounter >= freq)||(lenOffile >= freq)){

				bwrite.close();
				System.out.println("length of "+name+"is: "+childFile.length());
				i++;
				frequencyCounter = 0;

				freq = lenOfmultiStream[i];
				name = nameOfFile[i];
				childFile = new File(name);

				if(!childFile.exists()){
					String [] rename = name.split("\\\\");
					int l = rename.length-1;
					String parent = "";
					for(int m = 0;m < l-1;m++){
						parent =parent+rename[m]+"//";
					}
					parent+=rename[l-1];
					//String leaf = rename[l];
					File parentFile = new File(parent);
					if(!parentFile.exists()){
						parentFile.mkdirs();
					}
					childFile.createNewFile();
				}
				out = new FileOutputStream(childFile);
			bwrite = new BufferedOutputStream(out);

			}

			String formStr = Integer.toBinaryString(y);
			String format = ("00000000" + formStr).substring(formStr.length());
			buildCode.append(format);//problem  buildCode :空的stringBuilder
			int len = buildCode.length();
			if (len >= 8) {
				buildCode.delete(0, writeback(bwrite, buildCode, freq));
			}
		}
		if (buildCode.length() > 0) {
			writeback(bwrite, buildCode, freq);
		}
		bwrite.close();
				buildCode.delete(0, buildCode.length());
		b1.close();
	}


	public static void reformTextfile_de(HashMap<String, Byte> map, File f, int freq) throws IOException
	{
		StringBuilder buildCode = new StringBuilder(); 
       BufferedInputStream b1 = new BufferedInputStream(new FileInputStream(f));
		String str = f.toString();
		//源文件名
		str = str.substring(0, str.length()-4);
		File singleFile = new File(str);
		BufferedOutputStream bwrite = new BufferedOutputStream(new FileOutputStream(singleFile));
		System.out.println("line1: "+line1+"         line2: "+line2);


		while(b1.read()!='\n');
		while(b1.read()!='\n');

			int x = 0;
            int lenOfFile = (int)singleFile.length();
			while ((x = b1.read()) != -1) {

				String formStr = Integer.toBinaryString(x);
				String format = ("00000000" + formStr).substring(formStr.length());
				buildCode.append(format);//problem  buildCode :空的stringBuilder
				int len = buildCode.length();
				if(len >= 8){

					buildCode.delete(0,writeback(bwrite,buildCode,freq));

				}
			}
            if(buildCode.length() > 0){
				writeback(bwrite,buildCode,freq);
			}
			//frequencyCounter = 0
		lenOfFile = (int)singleFile.length();
            System.out.println("freq: "+freq);
            System.out.println("before: "+lenOfFile);
			b1.close();
			bwrite.close();
		lenOfFile = (int)singleFile.length();
		System.out.println("after: "+lenOfFile);
}
public static int writeback(BufferedOutputStream bwrite, StringBuilder buildCode, int freq)throws IOException{
	int presentP = 0;

	String check ="";
	for (int i=0 ; i< buildCode.length();i++)
	{
		check = check + buildCode.charAt(i);
		if(frequencyCounter<freq && CodetoSymb2.containsKey(check))
		{
			bwrite.write(CodetoSymb2.get(check));
			presentP += check.length();
			check="";
			frequencyCounter++;
		}
	}
	return presentP;
}
}
/**while(b1.read()!='\n'){
 loop1++;
 }
 while(b1.read()!='\n'){
 loop2++;
 };


 System.out.println("loop1: "+loop1+"           loop2: "+loop2);
 */
