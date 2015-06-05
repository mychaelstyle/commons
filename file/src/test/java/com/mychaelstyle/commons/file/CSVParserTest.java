package com.mychaelstyle.commons.file;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CSVParserTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        URL base = this.getClass().getClassLoader().getResource("csvs");
        String path = base.getPath();
        System.out.println(path);
        File root = new File(path);
        File[] dirs = root.listFiles();
        for(int num=0; num<dirs.length; num++){
            File dir = dirs[num];
            if(!dir.isDirectory()){
                continue;
            }
            File[] files = dir.listFiles();
            int count = 0;
            for(int i=0; i< files.length; i++){
                String fileName = files[i].toString();
                if(fileName.endsWith(".csv")/* && fileName.contains("08") && fileName.contains("31")*/){
                    System.out.println(fileName);
                    count++;
                    File csvFile = new File(fileName);
                    CSVParser parser = new CSVParser(csvFile,"MS932");
                    try {
                        try {
                            parser.open();
                        } catch(Throwable e){
                            fail(e.getMessage());
                        }
                        int lastColumnCount = -1;
                        int lineNumber = 1;
                        while(parser.hasNext()){
                            try {
                                List<String> record = parser.next();
                                System.out.println((i+1)+"/"+files.length+"ファイル :"
                                        +lineNumber+"行目,  "+fileName+" "+record);
                                if(-1!=lastColumnCount){
                                    if(lastColumnCount != record.size()){
                                        System.out.println(fileName+" row "+lineNumber
                                                +". Column count is expected "+lastColumnCount
                                                +" but "+record.size()+" "+record);
                                    }
                                    assertEquals(lastColumnCount,record.size());
                                }
                                lastColumnCount = record.size();
                            } catch(Throwable e){
                                fail(e.getMessage());
                            }
                            lineNumber++;
                        }
                    } finally {
                        try {
                            parser.close();
                        } catch(Throwable e){
                            fail(e.getMessage());
                        }
                    }
                }
            }
        }
    }

}
