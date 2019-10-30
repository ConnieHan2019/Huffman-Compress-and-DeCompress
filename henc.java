//Encoding & Compression java

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class henc {
	
	static HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
	static HashMap<Integer, String> symboltoCode = new HashMap<Integer,String>();
	//static HashMap<Integer, Byte> symboltoCode = new HashMap<Integer,Byte>();
	static HashMap<String, Integer> CodetoSymb = new HashMap<String,Integer>();
	static PriorityQueue<Node> priorityQ;
	static int nodeCount = 0;
	public static void main(String[] args) throws IOException
	{
		try
		{
			String sourcepath ="E:/Project 1/Test Cases/4/3.jpg";
		//String sourcepath = args[0];
		File file = new File(sourcepath);
           if(file.isDirectory()){
           	MultiHence multihence = new MultiHence(sourcepath);
           	multihence.start();
		   }
           else if(file.isFile()){
           	SingleHence singleHence = new SingleHence(sourcepath);
           	singleHence.start();
		   }




		}catch(FileNotFoundException e)
		{
		System.out.println("Please enter proper filename..(at the beginning)");
			return;
		}
	}

	/**
	 * 获取路径下的所有文件/文件夹
	 * @param directoryPath 需要遍历的文件夹路径
	 * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
	 * @return
	 */
	public static ArrayList<String> getAllFile(String directoryPath,boolean isAddDirectory) {
		ArrayList<String> list = new ArrayList<String>();
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			return list;
		}
		File[] files = baseFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if(isAddDirectory){
					list.add(file.getAbsolutePath());
				}
				list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
			} else {
				list.add(file.getAbsolutePath());
			}
		}
		return list;
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
		byte[] byteStream= displayByte(file);
		buildTree(map);
		int f =setPrefixcodes();
		Node node = priorityQ.peek();//frequency最低，huffman码最长的node
		genPrefixcodes(node ,"");
		mapCodes(node);
		realCompress(byteStream,file,f);
	}
	
	public static void realCompress(byte[] byteStream,File file, int freq) throws IOException {

		StringBuilder comp = new StringBuilder();
		StringBuilder code = new StringBuilder();
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
		/**
		 * 换行 压缩文件夹思路：单独文件时，第一个字符就是数字；
		 * 压缩文件夹时，能不能空出第一行以表示是文件夹？
		 * 连空两行为一个小文件结束
		 */
		comp.append('\n');
		//byte数 int freq = setPrefixed
		comp.append(freq);
		comp.append('\n');
		BufferedOutputStream br = null;



		br = new BufferedOutputStream(new FileOutputStream(file.toString() + ".huf"));
		for (int j = 0; j < comp.length(); j++) {
			char a = comp.charAt(j);
			br.write(a);

		}
		String str ="";
		String copyStr = "";
		for(int i =0; i<byteStream.length;i++)
		{
			int data = byteStream[i];
			//code.append(symboltoCode.get(data));
			str +=symboltoCode.get(data);
			int size = str.length();
			if(str.length() >= 8){
				copyStr = str.substring(0,8);
				int c = Integer.parseInt(copyStr,2);
				str = str.substring(8,str.length());
                 int d = c;
				br.write(c);
				copyStr = "";
			}
		}


		while(str.length()>=8) {
			copyStr = str.substring(0, 8);
			int c = Integer.parseInt(copyStr, 2);
			str = str.substring(8, str.length());
			int d = c;
			br.write(c);
			copyStr = "";
		}
		if(str!="")
		{
			String formatted = (str+"00000000").substring(0,8);
			int format= Integer.parseInt(formatted,2);
			br.write(format);

		}
		br.close();


		/**for(int k =0; k<code.length();k++)
		{
			st= st + code.charAt(k);

			if(st.length()==8)
			{
				int c = Integer.parseInt(st,2);
				System.out.println("parsseInt");
				System.out.println(st+" "+c);
				br.write(c);
				st="";
			}
		}*/

		/**
		 for(int i =0; i<byteStream.length;i++)
		 {
		 int data = byteStream[i];
		 // System.out.println("byte["+i+"] :"+data+"code: "+symboltoCode.get(data));
		 code.append(symboltoCode.get(data));//data symbol get(data)得到huffman code （string）

		 }

		 for(int k =0; k<code.length();k++)
		 {
		 st= st + code.charAt(k);

		 if(st.length()==8)
		 {
		 int c = Integer.parseInt(st,2);
		 //System.out.println(st+" "+c);
		 br.write(c);
		 st="";
		 }
		 }
		 *
		 */

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
			//Integer intCode = Integer.parseInt(node.code,2);
			//Byte B=(byte)(0XFF & intCode);
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
	
	public static byte[] displayByte(File file)
	{
		FileInputStream fis = null;
		int i = (int) file.length();//返回字节数(byte)
		byte[] bye = new byte[i];
		try
		{
			fis = new FileInputStream(file);
			fis.read(bye);

			getFreq(bye,file);
			
		}
			catch (Exception e) {
				System.out.println("File Not found in the path..");
			}
		return bye;		
	}
}
//int first= byteStream[0];
//int last = byteStream[byteStream.length-1];
//System.out.println("byteStream length "+byteStream.length+"     byteStream[0]:"+byteStream[0]+"     byteStream[last]"+byteStream[byteStream.length-1]);
//System.out.println("byteStream[0].get(data):"+symboltoCode.get(first)+"      byteStream[last].getdata:"+symboltoCode.get(last));
