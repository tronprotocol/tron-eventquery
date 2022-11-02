package org.tron.common.crypto;
import static org.tron.common.utils.LogConfig.LOG;

import java.util.regex.Pattern;

public class Crypto {

  private final static long SECRET_KEY = 5658116;
  private final static String CONVERT_KEY = "DeCATbJIzM";
  private final static String CONFUSED_WORDS_KEY = "FxYNgq";
  private final static int LEN_KEY = 32;


  static public String encrypt(String str){
    Long time = new Long(157551410);
    if(!isNumber(str)){
      LOG.info("not number");
      return null;
    }

    long number = Long.parseLong(str);
    long newNumber = (number + time) * SECRET_KEY;
    String[] numArr = String.valueOf(newNumber).split("");
    String[] initArr = CONVERT_KEY.split("");
    int len = numArr.length;
    StringBuffer buffer = new StringBuffer();

    for(int i = 0; i < len; i++){
      int inx = Integer.parseInt(numArr[i]);
      buffer.append(initArr[inx]);
    }

    String[] cwkArr = CONFUSED_WORDS_KEY.split("");
    if(len < LEN_KEY){
      int l = LEN_KEY - len;
      for(int i = 0; i < l; i++){
        int index = (int)(Math.random()*buffer.length());
        int inx = (int)(Math.random()*(CONFUSED_WORDS_KEY.length()));
        buffer.insert(index,cwkArr[inx]);
      }
    }
    String result = buffer.toString();
    return result;
  }

  public static int decrypt(String str){
    Long time = new Long(157551410);
    if(null == str || "".equals(str)){
      return 0;
    }
    int l = CONFUSED_WORDS_KEY.length();
    String[] cwkArr = CONFUSED_WORDS_KEY.split("");
    for(int i = 0; i < l; i++){
      str = str.replaceAll(cwkArr[i],"");
    }
    String[] initArr = str.split("");
    int len = initArr.length;
    StringBuffer result = new StringBuffer();
    for(int i = 0; i < len; i++ ){
      int k = CONVERT_KEY.indexOf(initArr[i]);
      if(k == -1){
        return 0;
      }
      result.append(k);
    }
    Long number;
    try {
      long total = Long.parseLong(result.toString());
      long sum = total/SECRET_KEY;
      number = sum - time;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0;
    }
    return number.intValue();
  }

  public static boolean isNumber(String value) {
    String pattern = "^[0-9]*[1-9][0-9]*$";
    boolean isMatch = Pattern.matches(pattern, value);
    return isMatch;
  }
}
