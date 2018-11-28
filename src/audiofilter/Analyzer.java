package audiofilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Analyzer {

  public static void Analyze(AudioReader reader,int I) {
    // TODO Auto-generated method stub
//    int I;
//    for (I = 1; I <= 10; I++) {
//      AudioReader reader = new AudioReader(
//          "C:\\Users\\samma\\Desktop\\Computer Vision and Audio\\Lab1\\语料\\" + I + ".wav");
      System.out.println(reader.getData());
      List<List<Integer>> data = reader.getData();
      
      List<List<Integer>> after=new ArrayList<>();  //samples after processing
      
      List<Long> energy = new ArrayList<>();
      List<Double> zero = new ArrayList<>();
      int i = 0, j = 0;
      int length = data.size();
      for (i = 0; i < length; i++) // every frame's energy
      {
        long en = 0;
        int ze = 0;
        List<Integer> temp = data.get(i); // frame i
        int size = temp.size();
        for (j = 0; j < size; j++) {
          en += Math.pow(temp.get(j), 2); // energy
        }
        
        if(en>50000000) {     //单门限法
          after.add(temp);
        }

        for (j = 1; j < size; j++) {
          if (temp.get(j - 1) * temp.get(j) < 0)
            ze += 1;
        }
        energy.add(en);
        zero.add(1.0 * ze / size);
      }

      // 写文件即可
      try {
        FileOutputStream fileWriter = new FileOutputStream(new File("D:\\" + I + "_en.txt"));
        OutputStreamWriter writer = new OutputStreamWriter(fileWriter);

        for (Long en : energy) {
          writer.write(en + "\r\n");
        }

        writer.close();

        FileOutputStream fileWriter2 = new FileOutputStream(new File("D:\\" + I + "_zero.txt"));
        OutputStreamWriter writer2 = new OutputStreamWriter(fileWriter2);

        for (Double z : zero) {
          writer2.write(z + "\r\n");
        }

        writer2.close();
        
      //输出处理过的音频文件
        FileOutputStream audiofile=new FileOutputStream("D:\\"+I+".pcm");
        for(List<Integer> list:after) {
          for(Integer integer:list) {
            byte byte1= (byte)(integer&0xFF);
//            System.out.println(byte1);
            byte byte2=(byte)((integer>>8)&0xFF);
//            System.out.println(byte2);
            audiofile.write(byte1);
            audiofile.write(byte2);
          }
        }
        audiofile.close();

      } catch (FileNotFoundException e) {
         
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
//  }
}
