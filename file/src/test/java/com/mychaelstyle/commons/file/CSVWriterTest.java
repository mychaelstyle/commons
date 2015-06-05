package com.mychaelstyle.commons.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CSVWriterTest {

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
    public void testCSVWriter() {
        String filePath = "./test.csv";
        CSVWriter writer = new CSVWriter(filePath);
        try {
            writer.open();
            String[] columns = new String[]{"Number","Category","Name","Price"};
            writer.writeRecord(Arrays.asList(columns));
            columns = new String[]{"1","TestCategory1","MyItem1","1000"};
            writer.writeRecord(Arrays.asList(columns));
            columns = new String[]{"2","TestCategory2","MyItem2","1200"};
            writer.writeRecord(Arrays.asList(columns));
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                fail(e.getMessage());
                e.printStackTrace();
            }
        }
        File file = new File(filePath);
        try {
            String contents = MFile.fileGetContents(file,"UTF-8");
            System.out.println(contents);
            String expected = "\"Number\",\"Category\",\"Name\",\"Price\"\n\"1\",\"TestCategory1\",\"MyItem1\",\"1000\"\n\"2\",\"TestCategory2\",\"MyItem2\",\"1200\"";
            assertEquals(expected,contents);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            file.delete();
        }
    }

}
