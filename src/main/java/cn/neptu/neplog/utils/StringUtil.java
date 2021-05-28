package cn.neptu.neplog.utils;

import cn.hutool.core.util.CharUtil;
import org.springframework.util.StringUtils;

public class StringUtil {

    public static int firstCamel(String s){
        char[] chars = s.toCharArray();
        for(int i=0;i<chars.length;i++){
            if(CharUtil.isLetterUpper(chars[i])){
                return i;
            }
        }
        return -1;
    }

    public static String trim(String s, char c){
        return StringUtils.trimTrailingCharacter(StringUtils.trimLeadingCharacter(s,c),c);
    }
}
