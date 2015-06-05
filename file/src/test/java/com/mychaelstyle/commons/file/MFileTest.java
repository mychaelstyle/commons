/**
 * 
 */
package com.mychaelstyle.commons.file;

import static org.junit.Assert.*;

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
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#tmp(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testTmpStringStringStringString() {
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#filePutContents(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testFilePutContentsStringStringString() {
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#filePutContents(java.io.File, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testFilePutContentsFileStringString() {
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#fileGetContents(java.io.File, java.lang.String)}.
     */
    @Test
    public void testFileGetContents() {
    }

    /**
     * Test method for {@link com.mychaelstyle.commons.file.MFile#getResourceAsString(java.lang.String, java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testGetResourceAsString() {
        try {
            String contents = MFile.getResourceAsString("resource.txt", "UTF-8", MFileTest.class);
            assertEquals("Hello\nMy name is masa!\nNice to meet you!", contents);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

}
