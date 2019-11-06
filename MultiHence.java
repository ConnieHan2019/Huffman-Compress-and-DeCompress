import sun.nio.cs.UTF_32;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MultiHence {
    //Encoding & Compression java

     public static boolean success = false;
      static String sourcepath;
      public static int numberOfFile;
      static ArrayList<Integer>lengthOfStream = new ArrayList<>();
    static ArrayList<String>emptyFileName = new ArrayList<>();
       static ArrayList<File> multifile = new ArrayList<>();
       static ArrayList<byte[]> multiByteStream = new ArrayList<>();
        static HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
        static HashMap<Integer, String> symboltoCode = new HashMap<Integer,String>();
        static ArrayList<String>fileName = new ArrayList<>();
        static HashMap<String, Integer> CodetoSymb = new HashMap<String,Integer>();
        static PriorityQueue<Node> priorityQ;
        static  int nodeCount = 0;
        static int[]multiNodeCount ;


        public MultiHence(String source){
            this.sourcepath = source;
        }


        public static void start()throws IOException{
            File file = new File(sourcepath);

           if(setAllFiles()) {
               compress(file);
           }else{
               String str = file+".huf";
               File emptyFile = new File(str);
               emptyFile.createNewFile();
           }
            System.out.println("Compression done..");
          file.delete();
            System.out.println(file + ".huf created..");
            success = true;

        }
        public static boolean setAllFiles() throws IOException
        {
                  fileName = getAllFile(sourcepath,true);
                  numberOfFile = fileName.size();
            multiNodeCount = new int[numberOfFile];
            ArrayList<String> getAll = getAllFile(sourcepath,true);
            if(getAll.size() == 0) {
            return false;
            }
                for (String fileName : getAll) {
                    //System.out.println(fileName);
                    File childfile = new File(fileName);
                    multifile.add(childfile);
                    //	System.out.println(filename);
                }
                return true;
        }

        /**
         * 获取路径下的所有文件/文件夹
         * @param directoryPath 需要遍历的文件夹路径
         * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
         * @return
         */
        public static ArrayList<String> getAllFile(String directoryPath, boolean isAddDirectory) {
            ArrayList<String> list = new ArrayList<String>();
            File baseFile = new File(directoryPath);
            if (baseFile.isFile() || !baseFile.exists()) {
                return list;
            }
            if(baseFile.isDirectory()&&emptyFile(baseFile)){

            }
            File[] files = baseFile.listFiles();
            //numberOfFile = files.length;
            //multiNodeCount = new int[numberOfFile];
            for (File file : files) {
                if (file.isDirectory()) {
                  if(emptyFile(file)) {
                      if (isAddDirectory) {
                          list.add(file.getAbsolutePath());
                          emptyFileName.add(file.getAbsolutePath());
                      }
                  }else {
                      list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
                  }
                } else {
                    list.add(file.getAbsolutePath());
                }
            }
            return list;
        }

        public static boolean emptyFile(File file){
            if(file.listFiles().length ==0){
                return true;
            }

            return false;
        }


        public static void fileEmpty(File f) throws IOException
        {
            String str = f+".huf";
            BufferedOutputStream ewrite = new BufferedOutputStream(new FileOutputStream(str));
            ewrite.close();
            f.delete();
            System.exit(0);
        }

        public static void compress(File file) throws IOException
        {
            multiByteStream = displayByte(multifile);
           // System.out.println("multifiles元素个数:  "+multifile.size());
            //System.out.println("multiByteStream元素个数:  "+multiByteStream.size());
            buildTree(map);
            int f =setPrefixcodes();
            Node node = priorityQ.peek();//frequency最低，huffman码最长的node
            genPrefixcodes(node ,"");//建树
            mapCodes(node);
            realCompress(multiByteStream,file,f);
        }

        public static void realCompress(ArrayList<byte[]>multiByteStream,File file, int freq) throws IOException {

            StringBuilder comp = new StringBuilder();

            //返回此映射的字符串表示形式。该字符串表示形式由键-值映射关系列表组成，按照该映射 entrySet 视图的迭代器返回的顺序排列，
            // 并用括号 ("{}") 括起来。相邻的映射关系是用字符 ", "（逗号加空格）分隔的。每个键-值映射关系按以下方式呈现：键，后面是一个等号 ("=")，
            // 再后面是关联的值。键和值都通过 String.valueOf(Object) 转换为字符串。
            String test = CodetoSymb.toString();
            //System.out.println(test);
            test = test.replace(", ", " ");
            //{0=115, 11=49, 101=108, 1001=53, 10000=10, 10001=13}去掉前后大括号
            test = test.substring(1, test.length() - 1);
            //comp开始有元素
            comp.append(test);
            comp.append('\n');
            //空行，表示是多文件
            comp.append('\n');
            //存储文件总数
            int numberOfFile = multifile.size();
            comp.append(numberOfFile);
            comp.append('\n');


            /**
             * 至此，CodeToSymb 已经写入文件，且已经换行两次，（line1:空行，用以解压时判断是单独文件还是文件夹）已写
             * 接下来写
             *
             * line2:root文件名
             * ling3:root的finalFrequcney（总byte数）
             * line4:root的byteStream
             * line5:空行，表示一个子文件写完
             */

            /**
             * 第二个版本
             * root文件数
             * 文件名=文件名长度 byteStream长度
             */
            String detail = null;
            for(int j =0;j<numberOfFile;j++) {
                //文件名
                //byte[] childname = multifile.get(j).toString().getBytes("UTF-8");
                String childname = multifile.get(j).toString();
                int status;
               if(emptyFileName.contains(childname)){
                   status = 1;
               }else{
                   status = 0;
               }
                childname = new String(childname.getBytes(),"UTF-8");
                //到emptyFileName里面找

                int  lenOfChildFile = multiByteStream.get(j).length;
               // System.out.println(lenOfChildFile);
                detail = childname+"::"+status+"::"+lenOfChildFile;

                comp.append(detail);
                comp.append('\n');

            }
            BufferedOutputStream  br = new BufferedOutputStream(new FileOutputStream(file.toString() + ".huf"));
         for (int j = 0; j < comp.length(); j++) {
                    char a = comp.charAt(j);
                   br.write(a);

                }

               String str = "";
               String copyStr = "";
               int count=0;
            for(int j =0;j<numberOfFile;j++) {
                byte[] byteStream = multiByteStream.get(j);

                for (int i = 0; i < byteStream.length; i++) {
                    int data = byteStream[i];

                    str += symboltoCode.get(data);
                    //int size = str.length();
                    if (str.length() >= 8) {
                        copyStr = str.substring(0, 8);
                        int c = Integer.parseInt(copyStr, 2);
                        str = str.substring(8, str.length());
                        int d = c;
                        br.write(c);

                        copyStr = "";
                        count++;
                    }
                }

                while (str.length() >= 8) {
                    copyStr = str.substring(0, 8);
                    int c = Integer.parseInt(copyStr, 2);
                    str = str.substring(8, str.length());
                    count++;
                    br.write(c);
                    copyStr = "";
                }
                if (str != "") {
                    String formatted = (str + "00000000").substring(0, 8);
                    int format = Integer.parseInt(formatted, 2);
                    br.write(format);
                    count++;

                }
                lengthOfStream.add(count);
            //  br.write('\n');
               // System.out.println("count"+j+": "+count);
            }

            br.close();
        }

        public static void mapCodes(Node node)
        {
            if(node.left!=null)
            {
                mapCodes(node.left);
            }
            if(node.right!=null)
            {
                mapCodes(node.right);
            }
            if (node.left ==null && node.right == null)
            {
                symboltoCode.put(node.symbol, node.code);
                CodetoSymb.put(node.code, node.symbol);
            }
        }

        //设置Huffman编码
        public static void genPrefixcodes(Node node, String codes)
        {
            if(node.left!=null)
            {
                node.left.code=node.code+"0";
                genPrefixcodes(node.left, node.left.code);
                node.right.code=node.code+"1";
                genPrefixcodes(node.right, node.right.code);
            }
        }


        public static int setPrefixcodes()
        {

            for (int w=0;w<nodeCount-1;w++)
            {
                Node parent = new Node (0);
                Node  leftNode = priorityQ.poll();
                Node  rightNode = priorityQ.poll();
                parent.setLeft(leftNode);
                parent.setRight(rightNode);
                parent.frequency = leftNode.getFrequency()+ rightNode.getFrequency();
                parent.symbol = leftNode.getSymbol() + rightNode.getSymbol();
                leftNode.code="0";
                rightNode.code="1";
                priorityQ.add(parent);
            }
            //队首元素
            Node freq = priorityQ.peek();
            //多少byte（所有频率之和）
            int finalfreq = freq.getFrequency();
            System.out.println("finalfreq: "+finalfreq);
            return finalfreq;
        }

        public static void getFreq(byte[] bye,File file) throws IOException
        {
            if(file.length()==0)
            {
                fileEmpty(file);


            }
            for (int i=0; i<bye.length;i++)
            {
                //将byte转化位int存入map
                int value = bye[i];

                if (!map.containsKey(value))
                    map.put(value,1);
                else
                    map.put(value,map.get(value)+1);
            }
        }

        public static void buildTree(HashMap<Integer,Integer> map)
        {
            priorityQ = new PriorityQueue<Node>();
            for (int i : map.keySet())//返回此映射中所包含的键的 Set 视图。
            {
                Node e = new Node(i,map.get(i));//i:symbol(byte) get(i)frequency
                nodeCount++;
                priorityQ.add(e);

            }
        }

        public static ArrayList<byte[]> displayByte(ArrayList<File> multifile) {
            FileInputStream fis = null;
            int j =0;
            for (File file : multifile) {
              int i = (int) file.length();//返回字节数(byte)
                if(i ==0){
                   // continue;
                }
                multiNodeCount[j] = (int) file.length();

               // System.out.println("len of file"+j+":  "+i);
                byte[] bye = new byte[i];
                try {
                    fis = new FileInputStream(file);
                    fis.read(bye);

                    getFreq(bye, file);

                } catch (Exception e) {
                    System.out.println("File Not found in the path..");
                }
                //设想，就算是一个空文件也会存一个byte[]
               multiByteStream.add(bye);
                j++;
            }
            return multiByteStream;
        }

/**
 * for( String files : allFileName ){
 * 			File fileToCompress = new File( files ) ;
 * 			String fileType = "";
 * 			output.write( files.getBytes().length );
 * 			//String singleFilePath = new String( files.getBytes() ) ;
 * 			//System.out.println( singleFilePath ) ;
 * 			//System.out.println( files.length() );
 * 		    for( int i = 0 ; i < files.getBytes().length ; i++ )
 * 		    	output.write( files.getBytes()[ i ] );
 *
 * 		    for( int i = 0 ; i < fileNumber ; i++ ) {
 * 			int singleFileNameLen = input.read();
 * 			String singleFilePath = "" ;
 * 			int fileTypeLen = 0 ;
 * 			String fileType = "";
 * 			byte[] b = new byte[ singleFileNameLen ] ;
 * 			input.read( b , 0 , singleFileNameLen ) ;
 * 			singleFilePath = new String( b ) ;
 * 			String newSingleFilePath = TargetPos + singleFilePath.substring( firstIndexofFile ) ;
 * 			allFileName.add( newSingleFilePath ) ; 
 *
 */
}
