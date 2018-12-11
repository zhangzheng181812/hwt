package com.zhx.admin.fileUtil;

import com.sun.rowset.internal.Row;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CORBA.INTERNAL;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * gaoxiang by 2018/12/5 10:20
 */


public class fileUtil {


    /**
     * 调用模板生产excel
     * @param orderNo       采购单订单号
     * @param goodsList     商品订单列
     * @param orgList       公司金额列
     * @param filePath      文件保存地址 (/xxxx/xxxxx/xxxx/)保留最后斜杠
     * @return
     * @throws Exception
     */
    public boolean newXleFile(String orderNo, List<Map<String,String>> goodsList, List<Map> orgList, String filePath) throws Exception {

        String path = ResourceUtils.getURL("classpath:").getPath()+"static/file/examplesFile.xls";
        InputStream is = new FileInputStream(path);
        HSSFWorkbook wb = new HSSFWorkbook(is);

        Sheet sheet = wb.getSheetAt(1);



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        //设置订单号
        sheet.getRow(2).getCell(2).setCellValue(sdf.format(new Date()));
        sheet.getRow(3).getCell(2).setCellValue(orderNo);
        //从11行开始，设置商品列表
        int r = 11;
        Row oleRow = sheet.getRow(r);
        int i = 1;
        for(Map map : goodsList){
            if(i ==1){
                oleRow.getCell(1).setCellValue(String.valueOf(map.get("goodsName")));
                oleRow.getCell(2).setCellValue(String.valueOf(map.get("goodsId")));
                oleRow.getCell(3).setCellValue(Double.valueOf(map.get("amount").toString()));
                oleRow.getCell(4).setCellValue(Integer.valueOf(map.get("count").toString()));
                oleRow.getCell(5).setCellValue(Double.valueOf(map.get("price").toString()));
                oleRow.getCell(6).setCellValue(Double.valueOf(map.get("totalAmount").toString()));
                oleRow.getCell(7).setCellValue(String.valueOf(map.get("Discount").toString()));
                oleRow.getCell(8).setCellValue(Double.valueOf(map.get("realAmount").toString()));
            }else{
                //开始添加行
                sheet.shiftRows(r+i-1, sheet.getLastRowNum(), 1,true,false);
                sheet.createRow(r+i-1);
                Row newRow = sheet.getRow(r+i-1);
                newRow.createCell(0).setCellValue(i);
                newRow.getCell(0).setCellStyle(oleRow.getCell(0).getCellStyle());

                newRow.createCell(1).setCellValue(String.valueOf(map.get("goodsName")));
                newRow.getCell(1).setCellStyle(oleRow.getCell(1).getCellStyle());

                newRow.createCell(2).setCellValue(String.valueOf(map.get("goodsId")));
                newRow.getCell(2).setCellStyle(oleRow.getCell(2).getCellStyle());

                newRow.createCell(3).setCellValue(Double.valueOf(map.get("amount").toString()));
                newRow.getCell(3).setCellStyle(oleRow.getCell(3).getCellStyle());

                newRow.createCell(4).setCellValue(Integer.valueOf(map.get("count").toString()));
                newRow.getCell(4).setCellStyle(oleRow.getCell(4).getCellStyle());

                newRow.createCell(5).setCellValue(Double.valueOf(map.get("price").toString()));
                newRow.getCell(5).setCellStyle(oleRow.getCell(5).getCellStyle());

                int s = r+i;
                newRow.createCell(6);
                newRow.getCell(6).setCellValue(Double.valueOf(String.valueOf(map.get("totalAmount"))));
                newRow.getCell(6).setCellStyle(oleRow.getCell(6).getCellStyle());

                newRow.createCell(7).setCellValue(String.valueOf(map.get("Discount").toString()));
                newRow.getCell(7).setCellStyle(oleRow.getCell(7).getCellStyle());

                newRow.createCell(8);
                newRow.getCell(8).setCellValue(Double.valueOf(String.valueOf(map.get("realAmount"))));
                newRow.getCell(8).setCellStyle(oleRow.getCell(8).getCellStyle());
            }
            i++;
        }
        //设置总计
        int nr = r + goodsList.size() ;
        sheet.getRow(nr).getCell(4).setCellFormula("SUM(E10:E"+nr+")");
        sheet.getRow(nr).getCell(6).setCellFormula("SUM(G10:G"+nr+")");
        sheet.getRow(nr).getCell(8).setCellFormula("SUM(I10:I"+nr+")");
        //从n+3行开始，设置公司列表
        i = 1;
        int or = nr + 3;
        for(Map<String,String> map : orgList){
            if(i ==1){
                sheet.getRow(or).getCell(1).setCellValue(String.valueOf(map.get("orgName")));

                sheet.getRow(or).getCell(8).setCellValue(Double.valueOf(String.valueOf(map.get("realAmount"))));
            }else{
                //开始添加行
                sheet.shiftRows(or+i-1, sheet.getLastRowNum(), 1,true,false);
                sheet.createRow(or+i-1);
                Row newRow = sheet.getRow(or+i-1);
                newRow.createCell(0).setCellValue(i);
                newRow.getCell(0).setCellStyle(oleRow.getCell(0).getCellStyle());

                newRow.createCell(1).setCellValue(String.valueOf(map.get("orgName")));
                newRow.getCell(1).setCellStyle(oleRow.getCell(1).getCellStyle());

                newRow.createCell(2);
                newRow.getCell(2).setCellStyle(oleRow.getCell(2).getCellStyle());

                newRow.createCell(3);
                newRow.getCell(3).setCellStyle(oleRow.getCell(3).getCellStyle());

                newRow.createCell(4);
                newRow.getCell(4).setCellStyle(oleRow.getCell(4).getCellStyle());

                newRow.createCell(5);
                newRow.getCell(5).setCellStyle(oleRow.getCell(5).getCellStyle());

                newRow.createCell(6);
                newRow.getCell(6).setCellStyle(oleRow.getCell(6).getCellStyle());

                newRow.createCell(7);
                newRow.getCell(7).setCellStyle(oleRow.getCell(7).getCellStyle());

                newRow.createCell(8).setCellValue(Double.valueOf(String.valueOf(map.get("realAmount"))));
                newRow.getCell(8).setCellStyle(oleRow.getCell(8).getCellStyle());
            }
            i++;
        }
        //设置总计
        nr = or + orgList.size() ;
        sheet.getRow(nr).getCell(8).setCellFormula("SUM(I"+or+":I"+nr+")");

        //计算公式
        sheet.setForceFormulaRecalculation(true);
        //保存
        /*String newPath = ResourceUtils.getURL("classpath:").getPath()+"static/file/";
//        FileOutputStream fileOut = new FileOutputStream(new File(newPath + orderNo + ".xls"));
        FileOutputStream fileOut = new FileOutputStream(new File(filePath + orderNo + ".xls"));*/
        //本地测试
//        String newPath = ResourceUtils.getURL("classpath:").getPath()+"static/file/";

        File fpath = new File(filePath);
        if (!fpath.exists()) {
            fpath.mkdirs();
        }
        FileOutputStream fileOut = new FileOutputStream(new File(filePath + orderNo + ".xls"));
        wb.write(fileOut);
        fileOut.close();
        return false;
    }


    public static void main(String[] args) {
        String orderNO ="2018789798456";
        List goodsList = new ArrayList();
        Map goodsMap = new HashMap();
        int i = 0;
        while (i< 4) {
            goodsMap = new HashMap();
            goodsMap.put("goodsName", "星巴克"+i);
            goodsMap.put("goodsId", "12312321312"+i);
            goodsMap.put("amount", 50.1+i);
            goodsMap.put("count", 10+i);
            goodsMap.put("price", 50.1+i);
            goodsMap.put("totalAmount", (50.1+i) * (10+i));
            goodsMap.put("Discount", "97%");
            goodsMap.put("realAmount", (48.1+i) * ((10+i)));
            goodsList.add(goodsMap);
            i++;
        }

        List orgList = new ArrayList();
        Map orgMap = new HashMap();
        i = 0;
        while (i< 2) {
            orgMap = new HashMap();
            orgMap.put("orgName", "工商"+i);
            orgMap.put("realAmount", (48+i) * (10+i));
            orgList.add(orgMap);
            i++;
        }
        fileUtil file = new fileUtil();
        try {
            file.newXleFile(orderNO,goodsList,orgList,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
