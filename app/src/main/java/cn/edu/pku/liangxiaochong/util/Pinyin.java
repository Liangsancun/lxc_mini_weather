package cn.edu.pku.liangxiaochong.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin {
    /*
    汉字转拼音，英文字符不变
    param 汉字
    return 拼音
     */
    public static String converterToSpell(String word) {
        String pinyin="";
        char[] nameChar=word.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //逐个字的获取拼音
        for(int i=0; i<nameChar.length; i++) {
            if(nameChar[i]>128) {//就是不是英文了
                try {
                    //获取汉字的拼音
                    pinyin += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];

                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyin += nameChar[i];//如果是英文的话，不变
            }
        }

        return pinyin;
    }
}
