/**
 * 
 */
package com.mychaelstyle.commons.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case for com.mychaelstyle.commons.file.MFile
 * 
 * @author Masanori Nakashima
 *
 */
public class MFileTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#tmp(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testTmpStringStringString() {
        String body = "Test contents";
        File tmpFile = null;
        try {
            tmpFile = MFile.tmp(body, "test-", ".txt");
            String contents = MFile.fileGetContents(tmpFile, "UTF-8");
            assertEquals(body,contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } finally {
            if(null!=tmpFile){
                tmpFile.delete();
            }
        }
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#tmp(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testTmpStringStringStringString() {
        String body = "Test contents\nTestContents";
        File tmpFile = null;
        try {
            tmpFile = MFile.tmp(body, "test1-", ".txt","UTF-16");
            String contents = MFile.fileGetContents(tmpFile,"UTF-16");
            assertEquals(body,contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } finally {
            if(null!=tmpFile){
                tmpFile.delete();
            }
        }
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#filePutContents(java.lang.String, java.lang.String, java.lang.String)}.
     * Test method for {@link com.mychaelstyle.commons.file.MFile#fileGetContents(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testFilePutContentsStringStringString() {
        String body = "Test contents\nTestContents";
        String filePath = "./testPutContent.txt";
        try {
            MFile.filePutContents(filePath, body, "UTF-8");
            String contents = MFile.fileGetContents(filePath, "UTF-8");
            assertEquals(body,contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } finally {
            new File(filePath).delete();
        }
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#filePutContents(java.io.File, java.lang.String, java.lang.String)}.
     * Test method for {@link com.mychaelstyle.commons.file.MFile#fileGetContents(java.io.File, java.lang.String)}.
     */
    @Test
    public void testFilePutContentsFileStringString() {
        String body = "Test contents\nTestContents";
        String filePath = "./testPutContentFile.txt";
        File file = new File(filePath);
        try {
            MFile.filePutContents(file, body, "UTF-8");
            String contents = MFile.fileGetContents(file, "UTF-8");
            assertEquals(body,contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#getResourceAsString(java.lang.String, java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testGetResourceAsString() {
        try {
            String contents = MFile.getResourceAsString("resource.txt", "UTF-8", MFileTest.class);
            assertEquals("Hello\nMy name is masa!\nNice to meet you!", contents);
            System.out.println(contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

}
