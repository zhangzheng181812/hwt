package com.zhx.admin.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片压缩工具类
 * Created by admin on 2016/5/26.
 */
public class ImageUtil {
    public static void main(String args[])  {
        try {
            zoom("D:\\config - 副本.ini", "D:\\ccc.png",0.667f);
        } catch (Exception e) {
            System.out.println(e);
        }
//        zoom("D:\\imgs\\20160422\\aa.jpg", "D:\\imgs\\20160422\\aaa.jpg",0.667f);
    }

    /**
     * 图像缩放
     *
     * @param srcPath               原图地址
     * @param targetPath           压缩后保存地址（全路径）
     * @param scale                 压缩比例
     */
    public static void zoom(String srcPath, String targetPath, float scale) throws Exception {
        int width = 0;
        int height = 0;
        try {
            File file = new File(srcPath);
            System.out.print(file.getPath());
            String fileType = srcPath.substring(srcPath.indexOf(".")+1,srcPath.length());
            BufferedImage src=null;// 读入源图像
            try{
                //是否为图片
                 src= ImageIO.read(file);
                if(null == src){
                    throw new Exception();
                }
            } catch (Exception e){
              throw new Exception();
            }
                width = src.getWidth();        // 源图宽
                height = src.getHeight();        // 源图高
            //缩放图像
            BufferedImage tag = new BufferedImage((int) (width * scale), (int) (height * scale), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = tag.createGraphics();

            if(srcPath.endsWith(".png")||srcPath.endsWith(".PNG")){
                tag = g.getDeviceConfiguration().createCompatibleImage((int) (width * scale),(int) (height * scale),Transparency.TRANSLUCENT);
                g.dispose();
                g = tag.createGraphics();
            }
            //  获取一个宽、长是原来scale的图像实例
            Image from = src.getScaledInstance((int) (width * scale),(int) (height * scale), src.SCALE_AREA_AVERAGING);
            g.drawImage(from, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            OutputStream out = new FileOutputStream(targetPath);
            ImageIO.write(tag,fileType , out);// 输出
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片添加后缀名
     * @param fileName  文件名称
     * @param endStr    后缀   aa.jpg   结果 aa_1080.jpg
     * @return
     */
    public static String getPicStr(String fileName,String endStr){
        String start =  fileName.substring(0,fileName.indexOf("."));
        String end =  fileName.substring(fileName.indexOf("."),fileName.length());
        return start+"_"+endStr+end;
    }
    /**
     * 复制图片
     * @param oldPath  旧地址
     * @param newPath  新地址
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File( newPath.substring(0, newPath.lastIndexOf("/")));
            if (oldfile.exists()) { //文件存在时
                newfile.mkdirs();
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }
}
