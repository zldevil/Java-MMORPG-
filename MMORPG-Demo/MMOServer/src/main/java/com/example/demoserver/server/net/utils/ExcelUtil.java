package com.example.demoserver.server.net.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import sun.tools.tree.ThisExpression;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {


    public static <T> List<T> readExcel(String excelpath, T t) throws Exception {

        ClassPathResource resource =new ClassPathResource(excelpath);
        InputStream is =resource.getInputStream();

        //InputStream is = new FileInputStream(new File(excelpath));
        Workbook wb= WorkbookFactory.create(is);

        List<T> list = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);

        //获取对象的所有属性
        Field[] fields = t.getClass().getDeclaredFields();

        if (sheet != null) {
            //读取每行内容
            for(int rowN = 1; rowN <= sheet.getLastRowNum(); ++rowN) {
                Row row = sheet.getRow(rowN);
                if (row != null) {
                    T obj =  (T)t.getClass().newInstance();

                    //循环次数小于对象的元素个数且小于表格列数
                    for(int i = 0; i < fields.length && i<row.getLastCellNum(); ++i) {

                        Cell cell = row.getCell(i);
                        String fi = fields[i].getName();
                        String sname = "set" + fi.substring(0, 1).toUpperCase() + fi.substring(1);
                        //利用反射注入读取的值
                        //获取属性的set方法
                        Method mt = obj.getClass().getMethod(sname, fields[i].getType());
                        //获取单元格的string类型的值

                        //注入数据
                        Object object = getCellFormatValue(row.getCell(i),fields[i]);

                        if(object==null||object.equals("")){

                            //Object转化
                           mt.invoke(obj,(Object)null);

                        }else{

                            Object objects=TypeUtil.returnCorrectType(object,fields[i]);

                            mt.invoke(obj, objects);
                        }

                    }
                    list.add((T) obj);
                }
            }
           }

        return list;
    }


    private  static Object getCellFormatValue(Cell cell,Field field) {

        Object cellValue=null ;

        if (cell != null) {

            if(field.getName().equals("neighborScence")||field.getName().equals("scenceEntityId")
                    ||field.getName().equals("goodsOnSale")){
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cellValue=cell.getStringCellValue();
            }
            else{

            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    // 如果当前Cell的Type为NUMERIC
                    cellValue = (int)cell.getNumericCellValue();
                    //logger.debug("数字类型 "+cellValue);
                    break;
                }

                case Cell.CELL_TYPE_STRING: {
                    // 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellValue = cell.getStringCellValue();
                    // logger.debug("字符类型 "+cellValue);
                    break;
                }

                default:
                    cellValue=null;
                    break;
            }
        }
        }
        return cellValue;
    }

}
