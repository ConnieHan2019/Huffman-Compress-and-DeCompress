import java.io.*;

public class EmptyFileHenc {
    public static boolean success = false;
    public EmptyFileHenc(String sourcepath){
        String str = sourcepath+".huf";
        File emptyFile = new File(str);
        try {
            FileOutputStream output = new FileOutputStream(emptyFile);
            String detail = "emptyFile";

            for (int j = 0; j < detail.length(); j++) {
                char a = detail.charAt(j);
                output.write(a);

            }
            output.close();
            success= true;
        }catch(FileNotFoundException e)
        {
            System.out.println("Please enter proper filename..(at the beginning)");
            return;
        }catch (IOException E){
            System.out.println("IO Exception……");
            return;
        }

    }
}
