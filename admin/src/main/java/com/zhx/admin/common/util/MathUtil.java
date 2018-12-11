package com.zhx.admin.common.util;



import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class MathUtil {
    /**
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
     */
    private static final int DEF_DIV_SCALE = 10; // 这个类不能实例化

    private MathUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        if(StringUtils.isEmpty(v1)){
            v1="0";
        }
        if(StringUtils.isEmpty(v2)){
            v2="0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.add(b2));
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */
    public static String sub(String v1, String v2) {
        if(StringUtils.isEmpty(v1)){
            v1="0";
        }
        if(StringUtils.isEmpty(v2)){
            v2="0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.subtract(b2));
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2) {
        if(StringUtils.isEmpty(v1) || StringUtils.isEmpty(v2)){
            return "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.multiply(b2));
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public static String div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static String div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if(StringUtils.isEmpty(v1) || StringUtils.isEmpty(v2)){
            return "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return String.valueOf(b.divide(one, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 比较a,b大小
     * @param a
     * @param b
     * @return
     */
    public static int compare(String a,String b){
        String result = sub(a,b);
        if(Integer.parseInt(result)>0){
            return 1;
        }else if(Integer.parseInt(result)==0){
            return 0;
        }else {
            return -1;
        }
    }

    /**
     * 判断是不是某个长度内的整数
     * @param str
     * @param length
     * @return
     */
    public static boolean isInteger(String str,int length) {
        if(str.length()>length){
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        System.out.println(isInteger("111111111111111111111111111111111111111111111111111111111111111111111111111111111111",5));
    }

}
