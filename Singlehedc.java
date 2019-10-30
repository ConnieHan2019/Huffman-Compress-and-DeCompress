import java.io.*;
import java.util.HashMap;

public class Singlehedc {
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
            String sourcepath ="E:/Project 1/Test Cases/4/3.jpg.huf";
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

        BufferedReader b = new BufferedReader(new FileReader(f));
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
        /**
         * int k = 20;
         * 		String temp = null;
         * 		String temp1 = null;
         * 		String temp2 = null;
         * 		while(k>0) {
         * 			temp= b.readLine();
         * 			temp1= b.readLine();
         * 			temp2= b.readLine();
         * 			k--;
         *                }
         */

        String strFre = b.readLine();
        if(strFre.equals("")) {
            /**
             * 多个文件，需要拆分
             */
            line2 += 1;
            String temp = b.readLine();
            numberOfFile = Integer.parseInt(temp);
            System.out.println(numberOfFile);
            line2 += temp.length();
            lenOfFileName= new int[numberOfFile];
            lenOfmultiStream = new int[numberOfFile];
            nameOfFile = new String[numberOfFile];

            String detail[] = null;
            String fileDetail[] = null;
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

          //  reformMui_de(nameOfFile,lenOfmultiStream,f);
        }
        else {//单个文件

            int freq = Integer.parseInt(strFre);
            reformTextfile_de(CodetoSymb2, f, freq);
        }
        b.close();
    }
    public static void reformTextfile_de(HashMap<String, Byte> map, File f, int freq) throws IOException
    {
        StringBuilder buildCode = new StringBuilder();
        BufferedInputStream b1 = new BufferedInputStream(new FileInputStream(f));
        String str = f.toString();
        //源文件名
        str = str.substring(0, str.length()-4);
        BufferedOutputStream bwrite = new BufferedOutputStream(new FileOutputStream(str));
        System.out.println("line1: "+line1+"         line2: "+line2);

       // b1.skip(line1);
        while(b1.read()!='\n');
        while(b1.read()!='\n');
        int x = 0;

        while ((x = b1.read()) != -1) {

            String formStr = Integer.toBinaryString(x);
            String format = ("00000000" + formStr).substring(formStr.length());
            buildCode.append(format);//problem  buildCode :空的stringBuilder
            int len = buildCode.length();
            if(len >= 8){

                buildCode.delete(0,writeback(bwrite,buildCode,freq,frequencyCounter));

            }
        }
        if(buildCode.length() > 0){
            writeback(bwrite,buildCode,freq,frequencyCounter);
        }
        //frequencyCounter = 0

        b1.close();
        bwrite.close();
    }public static int writeback(BufferedOutputStream bwrite, StringBuilder buildCode, int freq,int frequencyCounter)throws IOException{
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
