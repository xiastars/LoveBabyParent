package com.summer.helper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式相关
 * @author xiaqiliang
 *
 */
public class PatternUtils {
	
	public static void main(String[] args) {
		boolean result = stringStartWith("-78989","-");
		System.out.print(result);
		
	}
	public static boolean stringStartWith(String content, String prefix ){
		return content.matches(prefix);
	}

	/**
	 * 替换两段之间的文本
	 * @param content
	 * @param headContent
	 * @param bottomContent
	 * @param replaceContent
	 * @return
	 */
	public static String replaceWithHeadAndBottom(String content, String headContent, String bottomContent, String replaceContent){
		Pattern r = Pattern.compile(headContent+".*?"+bottomContent);
        Matcher m = r.matcher(content);
        return content = m.replaceAll(replaceContent);
	}

}
