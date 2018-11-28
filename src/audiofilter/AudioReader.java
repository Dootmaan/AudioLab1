package audiofilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Read the audio file, with a frame whose size is 256 (or 512 Bytes)
 * @author Sam_1160801026
 *
 */
public class AudioReader {
  private String filename = null;
  private List<List<Integer>> data = new ArrayList<>();

  private FileInputStream fis = null;
  private BufferedInputStream bis = null;

  public List<List<Integer>> getData() {
    return data;
  }

  public AudioReader(String filename) {
    this.filename = filename;
    init_reader(filename);
  }

  /**
   * read two bytes at a time, and turn them into an integer
   * @return the integer that these two bytes represents
   */
  private int readInt() {
    byte[] buf = new byte[2];
    int res = 0;
    try {
      if (bis.read(buf) != 2)
        throw new IOException("no more data!!!");
      res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return res;
  }

  /**
   * start the reader. Here the reader read the file by frame.
   * @param filename
   */
  private void init_reader(String filename) {
    try {
      fis = new FileInputStream(this.filename);
      bis = new BufferedInputStream(fis);
      bis.skip(44); // Ìø¹ýÍ·²¿
      while (bis.available() >= 512) {
        List<Integer> frame = new ArrayList<>();
        for (int i = 1; i <= 256; i++) {
          frame.add(this.readInt());
        }
        data.add(frame);
      }

      List<Integer> frame = new ArrayList<>();
      if (bis.available() > 0) {
        while (bis.available() > 0) {
          frame.add(this.readInt());
        }
        data.add(frame);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bis != null)
          bis.close();
        if (fis != null)
          fis.close();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }
}
