/**
 * ファイルを扱うユーティリティクラス
 * @author Masanori Nakashima
 */
package com.mychaelstyle.commons.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイルを扱うユーティリティクラス
 * @author Masanori Nakashima
 */
public class MFile {

    /**
     * constructor
     */
    private MFile(){
        super();
    }
    private FileOutputStream fileOutputStream = null;
    private OutputStreamWriter outputStreamWriter = null;
    private BufferedWriter bufferedWriter = null;

    public MFile(File file,String charset) throws FileNotFoundException, UnsupportedEncodingException{
        fileOutputStream = new FileOutputStream(file);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream,charset);
        bufferedWriter = new BufferedWriter(outputStreamWriter);
    }
    public void print(String str) throws IOException{
        bufferedWriter.write(str);
        bufferedWriter.flush();
    }
    public void println(String str) throws IOException{
        this.print(str+"\n");
    }
    public void close() throws IOException{
        if(null!=bufferedWriter){
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        if(null!=outputStreamWriter){
            outputStreamWriter.close();
        }
        if(null!=fileOutputStream){
            fileOutputStream.close();
        }
    }

    /**
     * UTF-8で一時ファイルに文字列を書き込んで保存しFileオブジェクトを返す
     * @param body
     * @param prefix
     * @param suffix
     * @return
     * @throws IOException
     */
    public static File tmp(String body, String prefix, String suffix) throws IOException{
        String encBody = new String(body.getBytes("UTF-8"), "UTF-8");
        return tmp(encBody, prefix, suffix, "UTF-8");
    }

    /**
     * 一時ファイルに文字列を書き込んで保存しFileオブジェクトを返す
     * @param body
     * @param prefix
     * @param suffix
     * @param charset
     * @return
     * @throws IOException
     */
    public static File tmp(String body, String prefix, String suffix, String charset) throws IOException{
        File tmpFile = File.createTempFile(prefix,suffix);
        filePutContents(tmpFile,body,charset);
        return tmpFile;
    }

    /**
     * ファイルパスを指定して文字列を書き込む
     * @param filePath
     * @param body
     * @param charset
     * @throws IOException
     */
    public static void filePutContents(String filePath, String body, String charset) throws IOException {
        File file = new File(filePath);
        filePutContents(file,body,charset);
    }

    /**
     * ファイルを指定して文字列を書き込む
     * @param file
     * @param body
     * @param charset
     * @throws IOException
     */
    public static void filePutContents(File file, String body, String charset) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter writer = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos,charset);
            writer = new BufferedWriter(osw);
            writer.write(body);
        } finally {
            if(null!=writer){
                writer.close();
            }
            if(null!=osw){
                osw.close();
            }
            if(null!=fos){
                fos.close();
            }
        }
    }

    /**
     * get string list from a file rows.
     * 
     * @param filePath
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<String> fileGetContentsAsLines(String filePath, String charset) throws IOException {
        return fileGetContentsAsLines(new File(filePath),charset);
    }

    /**
     * get string list from a file rows.
     * 
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<String> fileGetContentsAsLines(File file, String charset) throws IOException {
        List<String> lines = new ArrayList<String>();
        FileInputStream is = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            streamReader = new InputStreamReader(is,charset);
            reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while(null!=line){
                lines.add(line);
                line = reader.readLine();
            }
            return lines;
        } finally {
            if(null!=reader){
                try{
                reader.close();
                } finally{
                    try {
                        if(null!=streamReader){
                            streamReader.close();
                        }
                    } finally{
                        if(null!=is){
                            is.close();
                        }
                    }
                }
            }
        }
    }

    /**
     * file get contents
     * @param filePath
     * @param charset
     * @return
     * @throws IOException
     */
    public static String fileGetContents(String filePath, String charset)
            throws IOException {
        return fileGetContents(new File(filePath),charset);
    }

    /**
     * file get contents
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String fileGetContents(File file, String charset)
            throws IOException {
        List<String> lines = MFile.fileGetContentsAsLines(file, charset);
        StringBuffer buf = new StringBuffer();
        for(String line:lines){
            if(buf.length()!=0){
                buf.append("\n");
            }
            buf.append(line);
        }
        return buf.toString();
    }

    /**
     * get resource file contents as String
     * @param <T>
     * @param resourceName resource path strings
     * @param charset character set strings, e.g) UTF-8
     * @param c Class for get ClassLoader
     * @return
     * @throws IOException
     */
    public static <T> String getResourceAsString(String resourceName, String charset, Class<T> c) throws IOException{
        URL url = c.getClassLoader().getResource(resourceName);
        File file = new File(url.getPath());
        return fileGetContents(file,charset);
    }
}
