import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EmptyFolderHenc {
    public static boolean success = false;
    public EmptyFolderHenc(String sourcepath){
        String str = sourcepath+".huf";
        File emptyFile = new File(str);

            File emptyFolder = new File(sourcepath);
            File[] files = emptyFolder.listFiles();
            if(files.length!= 0){
                StringBuilder comp = new StringBuilder();
                comp.append("emptyMultiFolder");
                comp.append("\n");
                comp.append(files.length);
                comp.append("\n");
                for(File file :files){
                    String filename = file.toString();
                    String judge  = "0";
                    if(file.isDirectory()){
                        judge = "1";
                    }
                    comp.append(filename+"::"+judge);
                    comp.append("\n");
                }
                try { FileOutputStream output = new FileOutputStream(emptyFile);

                    for (int j = 0; j <comp.length(); j++) {
                        char a = comp.charAt(j);
                        output.write(a);

                    }
                    output.close();


                }catch(FileNotFoundException e)
                {
                    System.out.println("Please enter proper filename..(at the beginning)");
                    return;
                }catch (IOException E){
                    System.out.println("IO Exception……");
                    return;
                }


            }else{
                String detail = "emptyFolder";
                try {
                    FileOutputStream output = new FileOutputStream(emptyFile);

                    for (int j = 0; j < detail.length(); j++) {
                        char a = detail.charAt(j);
                        output.write(a);

                    }
                    output.close();
                }catch(FileNotFoundException e)
                {
                    System.out.println("Please enter proper filename..(at the beginning)");
                    return;
                }catch (IOException E){
                    System.out.println("IO Exception……");
                    return;
                }
            }
            System.out.println("done");
        success = true;
      }


}
