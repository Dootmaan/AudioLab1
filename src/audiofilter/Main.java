package audiofilter;

public class Main {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    int i=1;
    for(i=1;i<=10;i++)
    {
      Analyzer.Analyze(new AudioReader("C:\\Users\\samma\\Desktop\\Computer Vision and Audio\\Lab1\\ÓïÁÏ\\" + i + ".wav"),i);
    }
  }

}
