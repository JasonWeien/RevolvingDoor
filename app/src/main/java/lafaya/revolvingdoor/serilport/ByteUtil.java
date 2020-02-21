package lafaya.revolvingdoor.serilport;

import java.io.UnsupportedEncodingException;

public class ByteUtil {

    public static String bytesToAscii(byte[] bytes, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (dateLen <= 0)) {
            return null;
        }
        if (bytes.length < dateLen) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes,0,data,0,dateLen);
        try {
            asciiStr = new String(data, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asciiStr;
    }

    public static char[] stringToHexChar(String strData){
        char[] tmpdata = strData.toCharArray();
        char[] hexdata = new char[strData.length() / 2];

        for(int i = 0; i < strData.length();i+=2){
            hexdata[i /2] = (char)((tmpdata[i] << 4) | (tmpdata[i+1]));
        }
        return hexdata;
    }

    public static String parseHexStr2Binary(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        String result = "";
        for(int i = 0; i < hexStr.length(); i++){
            switch (hexStr.charAt(i)){
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }


}