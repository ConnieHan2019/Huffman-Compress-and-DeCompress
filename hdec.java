//Decoding & Decompression
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class hdec{

	public static boolean success = false;
	static HashMap<String, Byte> CodetoSymb2 = new HashMap<String,Byte>();
    static int line1;
    static int line2;
    public static int frequencyCounter = 0;
    public static int numberOfFile;
    public static int[] lenOfmultiStream;
    public static String[]  nameOfFile;
    public static int [] emptyFileName;
    public static void main(String[]args){
    	String sourcepath = "E:/Project 1/Test Cases1/test3 - empty file and empty folder.huf";
		hdec depress = new hdec(sourcepath);
	}

	public hdec(String path)
	{long startTime = System.nanoTime();
		try
		{
			String sourcepath =path;
			System.out.println(path);
			//String sourcepath = args[0];
		File file = new File(sourcepath);
		recreateMap_de(file);

			System.out.println("File created..");
		file.delete();
		success = true;
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Please give proper .huf file..");
		}
		catch (ArrayIndexOutOfBoundsException a) 
		{
			System.out.println("Please enter file name");
		}
		catch (IOException E){
			System.out.println("IO exception");
		}
		long endTime = System.nanoTime();
		long runTime = endTime - startTime;
		System.out.println("运行时间： "+ runTime);
		
	}
	public static void fileEmpty_de(File f) throws IOException
	{
		
		String str = f.toString();
		str = str.substring(0, str.length()-4);
		BufferedOutputStream ewrite = new BufferedOutputStream(new FileOutputStream(str));
		ewrite.close();
		//f.delete();
		System.exit(0);
	}

	 public static void buildEmptyFolder(File f)throws IOException{
		 String path = f.toString();
		 String str = path.substring(0, path.length()-4);
		 File file = new File(str);
		 file.mkdirs();
		 if(file.isDirectory()){
		 	System.out.println("success");
		 }
	 }
    public static void buildEmptyFolder(String str)throws IOException{
        File file = new File(str);
        file.mkdirs();
        if(file.isDirectory()){
            System.out.println("success");
        }
    }
	 public static void buildEmptyFile(File f)throws IOException{
        String path = f.toString();
		String str = path.substring(0, path.length()-4);
		 File file = new File(str);
		 file.createNewFile();
	 }
	public static void recreateMap_de(File f) throws IOException
	{

		if(f.length()!= 0) {
			BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			if (f.length() == 0) {
				fileEmpty_de(f);
			}
			String mapp = b.readLine();
			//System.out.println(mapp);
			if (mapp.equals("emptyFolder")) {
				System.out.println("is emptyFolder");
				buildEmptyFolder(f);
				f.delete();
			} else if (mapp.equals("emptyFile")) {
				System.out.println("is emptyFile");
				buildEmptyFile(f);
				f.delete();
			} else if(mapp.equals("emptyMultiFolder")){
				int freq = Integer.parseInt(b.readLine());
				for(int i = 0;i<freq;i++){
					String[]temp = b.readLine().split("::");
					if(temp[1].equals("1")){
						File dir = new File(temp[0]);
						dir.mkdirs();
					}else{
						String name = temp[0];
						File file = new File(temp[0]);
						if (!file.exists()) {
							String[] rename = name.split("\\\\");
							int l = rename.length - 1;
							String parent = "";
							for (int m = 0; m < l - 1; m++) {
								parent = parent + rename[m] + "//";
							}
							parent += rename[l - 1];
							//String leaf = rename[l];
							File parentFile = new File(parent);
							if (!parentFile.exists()) {
								parentFile.mkdirs();
							}

							file.createNewFile();
						}
					}
				}
				//System.out.println("done");
			}
			else {
				line1 = mapp.length();

				String arr[] = mapp.split(" ");
				for (int i = 0; i < arr.length; i++) {
					String regx = arr[i];
					String a[] = regx.split("=");
					CodetoSymb2.put(a[0], Byte.parseByte(a[1]));
				}

				String strFre = b.readLine();
				if (strFre.equals("")) {
					/**
					 * 多个文件，需要拆分
					 */
					line2 += strFre.length();
					String temp = b.readLine();
					numberOfFile = Integer.parseInt(temp);
					//System.out.println(numberOfFile);
					line2 += temp.length();
					emptyFileName = new int[numberOfFile];
					lenOfmultiStream = new int[numberOfFile];
					nameOfFile = new String[numberOfFile];

					String detail[] = null;

					String read = null;
					for (int j = 0; j < numberOfFile; j++) {
						read = b.readLine();
						line2 += read.length();

						//E:\'�\Project 1\Test Cases\test2 - folder\1\6917291.htm=55 64552
						detail = read.split("::", 3);
						nameOfFile[j] = detail[0];
						emptyFileName[j] = Integer.parseInt(detail[1]);
						lenOfmultiStream[j] = Integer.parseInt(detail[2]);
					}
					//for (int i = 0; i < numberOfFile; i++) {
					//	System.out.println("文件名: " + nameOfFile[i] + " 文件名长度: " + lenOfFileName[i] + " byte数: " + lenOfmultiStream[i]);

					//}

					reformMui_de(nameOfFile, lenOfmultiStream, f);
				} else {//单个文件

					int freq = Integer.parseInt(strFre);
					reformTextfile_de(CodetoSymb2, f, freq);
				}
				b.close();
			}
		}
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
        }

        int freq;
        String name = null;
        int empty = 0;
        int y = 0;
        int i = 0;
        int lenOffile;

        empty = emptyFileName[i];
        while(empty ==1){
            name = nameOfFile[i];
            buildEmptyFolder(name);
            i++;
            empty = emptyFileName[i];
            if(i>= numberOfFile)
                break;
        }
        freq = lenOfmultiStream[i];
        name = nameOfFile[i];
            File childFile = new File(name);

            if (!childFile.exists()) {
                String[] rename = name.split("\\\\");
                int l = rename.length - 1;
                String parent = "";
                for (int m = 0; m < l - 1; m++) {
                    parent = parent + rename[m] + "//";
                }
                parent += rename[l - 1];
                //String leaf = rename[l];
                File parentFile = new File(parent);
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                childFile.createNewFile();
            }


            if (freq != 0) {
                FileOutputStream out = new FileOutputStream(childFile);
                BufferedOutputStream bwrite = new BufferedOutputStream(out);
                int temp;
                while ((y = b1.read()) != -1) {
                    lenOffile = (int) childFile.length();
                    temp = frequencyCounter;
                    if ((frequencyCounter >= freq) || (lenOffile >= freq)) {

                        bwrite.close();
                        //System.out.println("length of " + name + "is: " + childFile.length());
                        i++;
                        while(empty ==1){
                            buildEmptyFolder(name);
                            i++;
                            empty = emptyFileName[i];
                            if(i>= numberOfFile)
                            return;
                        }
                        frequencyCounter = 0;

                        freq = lenOfmultiStream[i];
                        name = nameOfFile[i];
                        childFile = new File(name);

                        if (!childFile.exists()) {
                            String[] rename = name.split("\\\\");
                            int l = rename.length - 1;
                            String parent = "";
                            for (int m = 0; m < l - 1; m++) {
                                parent = parent + rename[m] + "//";
                            }
                            parent += rename[l - 1];
                            //String leaf = rename[l];
                            File parentFile = new File(parent);
                            if (!parentFile.exists()) {
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
            }


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
		//System.out.println("line1: "+line1+"         line2: "+line2);


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


			b1.close();
			bwrite.close();


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