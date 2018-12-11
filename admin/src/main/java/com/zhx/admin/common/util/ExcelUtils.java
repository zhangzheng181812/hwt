package com.zhx.admin.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 14-12-9.
 */
public class ExcelUtils {

    //成功
    public static final String SUCCESS="SUCCESS";
    //失败
    public static final String ERROR="ERROR";
    //错误信息
    public static final String ERRORMESSAGE="ERRORMESSAGE";
    //失败行数
    public static final String ERRORNUM="ERRORNUM";
    //失败行数
    public static final String ERRORSHEET="ERRORSHEET";
    //失败字段
    public static final String ERRORFIELD="ERRORFIELD";
    //数据
    public static final String DATA="DATA";


    /**
     * 导出excel文件
     * @param os 文件输出流
     * @param list 导出文件流
     * @param paramOrder 文件头  参数顺序 id&name&age&sex  参数间用&隔开    如果这个参数为null 那么取文件第一行  作为键
     * @param fileType 文件类型 （xls、xlsx、csv）    默认xlsx
     * @return
     */
    public static OutputStream writeFile(OutputStream os, List<Map> list, String paramOrder, String fileType){
    	if(fileType==null || "".equals(fileType)){
    		fileType="xlsx";
    	}
    	if(list==null || list.size()<1){
    		return null;
    	}
    	if("xls".equals(fileType)){
    		return null;
    	}
    	if("csv".equals(fileType)){
    		return null;
    	}
    	if("xlsx".equals(fileType)){
    		return writeExcel(os,list,paramOrder);
    	}
    	return null;
    }

	/**
     * 读取excel(xls、xlsx ) 、 csv 文件
     * @param storefile  文件
     * @param fileName   文件名称
     * @param paramOrder 参数顺序 id&name&age&sex    参数间用&隔开    如果这个参数为null 那么取文件第一行  作为键
     * @return Map 错误信息及返回的list<Map>  ERRORNUM   错误行数   ；   ERRORSHEET错误的sheet数。 如果没错。  则 ERRORMESSAGE 里面不包含ERRORMESSAGE
     */
    public static Map readFile(File storefile, String fileName, String paramOrder){
        Map map = new HashMap();
        if(storefile==null||!storefile.isFile()||fileName==null||"".equals(fileName)){
            map.put(ERROR,"文件或者文件名不符合要求");
            return map;
        }
        InputStream is=null;
        try{
            List list = new ArrayList();
            is = new FileInputStream(storefile);
            if(fileName.lastIndexOf(".xlsx")!=-1){                                  //2007
                System.out.println("2007");
                list = readExcelnew(is,paramOrder);
            }else if(fileName.lastIndexOf(".xls")!=-1){                            //2003
                System.out.println("2003");
                list = readExcelold(is,paramOrder);
            }else if(fileName.lastIndexOf(".csv")!=-1){                            //csv
                System.out.println("csv");
                list = readCsv(storefile,paramOrder);
            }
            //错误信息放最后面
            Map messageinfo = (Map) list.get(list.size()-1);
            if(list.size()>1){
                list.remove(list.size()-1);
                map.put(DATA,list);
            }
            map.put(ERRORMESSAGE,messageinfo);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 2003
     * @param is   文件流
     * @param paramOrder 解析字段顺序
     * @return
     * @throws IOException
     */
    public static List readExcelold(InputStream is, String paramOrder) throws IOException {
        //返回结果
        List<Map> list = new ArrayList<Map>();
        //数据
        Map paramap;
        //错误信息
        Map<String,String> messageinfo = new HashMap<String,String>();
        //错误行数
        int errornum=0;
        //错误sheet
        int errorsheet=0;
        //是否有序
        boolean isOrder=true;
        String[]param=new String[]{};
        if(paramOrder==null||"".equals(paramOrder)){
            isOrder=false;
        }else{
            param= paramOrder.split("&");
        }
        //读取excel
        Workbook wb = new HSSFWorkbook(is);
        int sheetnum=wb.getNumberOfSheets();
        if(sheetnum<1){
            messageinfo.put(ERROR,"空文件");
        }
        int rowNum=0;
        try{
            for (int numSheet = 0; numSheet < sheetnum ; numSheet++) {
                HSSFSheet hssfSheet = (HSSFSheet) wb.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                rowNum=hssfSheet.getLastRowNum();
                if (rowNum <1 ) {
                    continue;
                }
                if(isOrder){
                    for (int n = 0; n <= rowNum; n++) {
                        HSSFRow hssfRow = hssfSheet.getRow(n);
                        int cellNum = hssfRow==null?0:hssfRow.getLastCellNum();
                        paramap=new HashMap();
                        for(int i=0;i<cellNum;i++){
                            hssfRow.getCell(i).setCellType(HSSFCell.CELL_TYPE_STRING);
                            HSSFCell columnval =  hssfRow.getCell(i);
                            paramap.put(param[i],getValue(columnval));
                        }
                        list.add(paramap);
                        errornum=n;
                    }
                }else{
                    List cellName = new ArrayList();
                    if(rowNum>1){
                        HSSFRow hssfRow = hssfSheet.getRow(0);//默认第一行是字段
                        int cellNum = hssfRow==null?0:hssfRow.getLastCellNum();
                        for(int i=0;i<cellNum;i++){
                            HSSFCell column =  hssfRow.getCell(i);
                            cellName.add(getValue(column));
                        }
                    }
                    for (int n = 1; n <= rowNum; n++) {
                        HSSFRow hssfRow = hssfSheet.getRow(n);
                        paramap=new HashMap();
                        for(int i=0;i<cellName.size();i++){
                            HSSFCell columnval =  hssfRow.getCell(i);
                            paramap.put(cellName.get(i),getValue(columnval));
                        }
                        list.add(paramap);
                        errornum=n;
                    }
                }
                errorsheet=numSheet;
            }
            list.add(messageinfo);
        }catch(Exception e){
            e.printStackTrace();
            if(!isOrder){
                errornum++;
            }
            messageinfo.put(ERRORNUM, String.valueOf(errornum+1));
            messageinfo.put(ERRORSHEET, String.valueOf(errorsheet+1));
            list.add(messageinfo);
        }
        return list;
    }
    /**
     * 2007
     * @param is   文件流
     * @param paramOrder 解析字段顺序
     * @return
     * @throws IOException
     */
    public static List readExcelnew(InputStream is, String paramOrder) throws IOException {
        //返回结果
        List<Map> list = new ArrayList<Map>();
        //数据
        Map paramap;
        //错误信息
        Map<String,String> messageinfo = new HashMap<String,String>();
        //错误行数
        int errornum=0;
        //错误sheet
        int errorsheet=0;
        //是否有序
        boolean isOrder=true;
        String[]param=new String[]{};
        if(paramOrder==null||"".equals(paramOrder)){
            isOrder=false;
        }else{
            param= paramOrder.split("&");
        }
        //读取excel
        Workbook wb = new XSSFWorkbook(is);
        int sheetnum=wb.getNumberOfSheets();
        if(sheetnum<1){
            messageinfo.put(ERROR,"空文件");
        }
        int rowNum=0;
        try{
            for (int numSheet = 0; numSheet < sheetnum ; numSheet++) {
                XSSFSheet xssfSheet = (XSSFSheet) wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                rowNum=xssfSheet.getLastRowNum();
                if (rowNum <1 ) {
                    continue;
                }
                if(isOrder){
                    for (int n = 0; n <= rowNum; n++) {
                        XSSFRow xssfRow = xssfSheet.getRow(n);
                        int cellNum = xssfRow==null?0:xssfRow.getLastCellNum();
                        paramap=new HashMap();
                        for(int i=0;i<cellNum;i++){
                            xssfRow.getCell(i).setCellType(HSSFCell.CELL_TYPE_STRING);
                            XSSFCell columnval =  xssfRow.getCell(i);
                            paramap.put(param[i],getValue(columnval));
                        }
                        list.add(paramap);
                        errornum=n;
                    }
                }else{
                    List cellName = new ArrayList();
                    if(rowNum>1){
                        XSSFRow xssfRow = xssfSheet.getRow(0);//默认第一行是字段
                        int cellNum = xssfRow==null?0:xssfRow.getLastCellNum();
                        for(int i=0;i<cellNum;i++){
                            XSSFCell column =  xssfRow.getCell(i);
                            cellName.add(getValue(column));
                        }
                    }
                    for (int n = 1; n <= rowNum; n++) {
                        XSSFRow xssfRow = xssfSheet.getRow(n);
                        paramap=new HashMap();
                        for(int i=0;i<cellName.size();i++){
                            XSSFCell columnval =  xssfRow.getCell(i);
                            paramap.put(cellName.get(i),getValue(columnval));
                        }
                        list.add(paramap);
                        errornum=n;
                    }
                }
                errorsheet=numSheet;
            }
            list.add(messageinfo);
        }catch(Exception e){
            e.printStackTrace();
            if(!isOrder){
                errornum++;
            }
            messageinfo.put(ERRORNUM, String.valueOf(errornum+1));
            messageinfo.put(ERRORSHEET, String.valueOf(errorsheet+1));
            list.add(messageinfo);
        }
        return list;
    }
    /**
     * csv
     * @param file 文件
     * @param paramOrder 解析字段顺序
     * @return
     * @throws IOException
     */
    public static List readCsv(File file, String paramOrder) throws IOException {
        //返回结果
        List<Map> list = new ArrayList<Map>();
        //数据
        Map paramap;
        //错误信息
        Map<String,String> messageinfo = new HashMap<String,String>();
        //是否有序
        boolean isOrder=true;
        String[]param=new String[]{};
        if(paramOrder==null||"".equals(paramOrder)){
            isOrder=false;
        }else{
            param= paramOrder.split("&");
        }
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int readnum=1;
        String[]oneLines=new String[]{};
        try{
            while ((line = br.readLine()) != null) {
                oneLines=line.split(",");
                if(isOrder){
                    paramap=new HashMap();
                    for(int i=0;i<param.length;i++){
                        paramap.put(param[i],oneLines[i]);
                    }
                    list.add(paramap);
                }else{
                    if(readnum==1){
                        param=oneLines;
                    }else{
                        paramap=new HashMap();
                        for(int i=0;i<param.length;i++){
                            paramap.put(param[i],oneLines[i]);
                        }
                        list.add(paramap);
                    }
                }
                readnum++;
            }
        }catch(Exception e){
            e.printStackTrace();
            if(!isOrder){
                readnum++;
            }
            messageinfo.put(ERRORNUM, String.valueOf(readnum));
            list.add(messageinfo);
        }finally {
            br.close();
            fr.close();
        }
        list.add(messageinfo);
        return list;
    }

    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }
    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    private static OutputStream writeExcel(OutputStream os, List<Map> list, String paramOrder) {
    	int num=0;
    	try {
	    	//创建工作表
    		SXSSFWorkbook wb = new SXSSFWorkbook(100);
    		Sheet sheet = wb.createSheet();
	    	Row row=null;
	    	String[]item =paramOrder.split("&");
	    	Map map = null;
	    	for(int i=0;i<list.size();i++){
	    		row = sheet.createRow(i);
	    		Cell cell=null;
	    		map = list.get(i);
	    		for(int j=0;j<item.length;j++){
	    			cell = row.createCell(j);
	    			cell.setCellValue(String.valueOf(map.get(item[j])));
	    		}
	    		num=i;
	    	}
            //写入
            wb.write(os);
        } catch (Exception e) {
        	System.out.println(num);
            e.printStackTrace();
        }
		return os;
	}

}
