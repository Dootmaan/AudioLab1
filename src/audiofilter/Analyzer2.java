package audiofilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class Frame {
  private long energy;
  private double zero;
  private List<Integer> dots;

  public Frame(List<Integer> dots, long energy, double zero) {
    this.dots = dots;
    this.zero = zero;
    this.energy = energy;
  }

  public long getEnergy() {
    return energy;
  }

  public double getZero() {
    return zero;
  }

  public List<Integer> getDots() {
    return dots;
  }
}


public class Analyzer2 {

  public static void Analyze(AudioReader reader, int I) {
    // TODO Auto-generated method stub
    // int I;
    // for (I = 1; I <= 10; I++) {
    // AudioReader reader = new AudioReader(
    // "C:\\Users\\samma\\Desktop\\Computer Vision and Audio\\Lab1\\语料\\" + I + ".wav");
    System.out.println(reader.getData());
    List<List<Integer>> data = reader.getData();

    List<List<Integer>> after = new ArrayList<>(); // samples after processing

    List<Frame> frames = new ArrayList<>();
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

      // if(en>5000000) { //单门限法
      // after.add(temp);
      // }

      for (j = 1; j < size; j++) {
        if (temp.get(j - 1) * temp.get(j) < 0)
          ze += 1;
      }
      Frame frame = new Frame(temp, en, 1.0 * ze / size);
      frames.add(frame);
    }

    // 开始使用双门限法过滤
    int a=0,b=0,k;
    int len=frames.size();
    for (k=0;k<len;k++) {
      a=0;b=0;
      if(frames.get(k).getEnergy()>100000000) {  //寻找确定的语音部分的起始位置
        for(a=k;a>0;a--)      //向前找过零率符合要求的
        {
          if(frames.get(a).getZero()>0.04)
            break;
        }
        
        for(b=k;b<len;b++) {           //向后找确定的语音部分的截至位置
          if(frames.get(b).getEnergy()<=100000000)
            break;
        }
        int b2;
        for(b2=b;b2<len;b2++) {        //向后找过零率符合要求的末尾位置
          if(frames.get(b2).getZero()>0.04)
            break;
        }
        
        int f;
        for(f=a;f<b2;f++)
        {
          after.add(frames.get(f).getDots());   //将找到的一段加入最终结果
        }
        
        k=b2;
        
      }
    }

    // 写文件即可
    try {
      FileOutputStream fileWriter = new FileOutputStream(new File("D:\\" + I + "_en.txt"));
      OutputStreamWriter writer = new OutputStreamWriter(fileWriter);

      FileOutputStream fileWriter2 = new FileOutputStream(new File("D:\\" + I + "_zero.txt"));
      OutputStreamWriter writer2 = new OutputStreamWriter(fileWriter2);

      for (Frame f : frames) {
        writer.write(f.getEnergy() + "\r\n");
        writer2.write(f.getZero() + "\r\n");
      }

      writer.close();
      writer2.close();

      // 输出处理过的音频文件
      FileOutputStream audiofile = new FileOutputStream("D:\\" + I + ".pcm");
      for (List<Integer> list : after) {
        for (Integer integer : list) {
          byte byte1 = (byte) (integer & 0xFF);
          // System.out.println(byte1);
          byte byte2 = (byte) ((integer >> 8) & 0xFF);
          // System.out.println(byte2);
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
  // }
}
