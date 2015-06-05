/**
 * 
 */
package com.mychaelstyle.commons.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV writer
 * @author Masanori Nakashima
 */
public class CSVWriter {

    /** UTFのByte Order Mark */
    public static final Map<String,byte[]> BOM_MAP = new HashMap<String,byte[]>(){
        private static final long serialVersionUID = 6354569408916706865L;
        {
            put("UTF-8", new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
            put("UTF-16BE", new byte[]{(byte)0xFE, (byte)0xFF});
            put("UTF-16LE", new byte[]{(byte)0xFF, (byte)0xFE});
            put("UTF-32BE", new byte[]{0x00, 0x00, (byte)0xFE, (byte)0xFF});
            put("UTF-32LE", new byte[]{(byte)0xFF, (byte)0xFE, 0x00, 0x00});
        }
    };

    private File targetFile = null;

    private String charset = "UTF-8";
    
    private String delimiter = ",";

    private boolean addBOM = false;

    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;
    private BufferedWriter writer = null;

    /**
     * ファイルパスを指定するコンストラクタ
     * @param path ファイルパス
     */
    public CSVWriter(String path) {
        this.targetFile = new File(path);
    }
    /**
     * ファイルパスと出力文字コードを指定するコンストラクタ
     * @param path ファイルパス
     * @param charset 出力文字コード ex. UTF-8
     */
    public CSVWriter(String path, String charset) {
        this.targetFile = new File(path);
        this.charset = charset;
    }
    /**
     * ファイルパスと文字コードと区切り文字を指定するコンストラクタ
     * @param path ファイルパス
     * @param charset 出力文字コード ex. UTF-8
     * @param delimiter 区切り文字
     */
    public CSVWriter(String path, String charset, String delimiter) {
        this.targetFile = new File(path);
        this.charset = charset;
        this.delimiter = delimiter;
    }

    public CSVWriter(String path, String charset, String delimiter, boolean addBOM){
        this.targetFile = new File(path);
        this.charset = charset;
        this.delimiter = delimiter;
        this.addBOM = addBOM;
    }

    /**
     * ファイルを書き込みオープン
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void open() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        fos = new FileOutputStream(this.targetFile);
        if(this.addBOM){
            byte[] bom = BOM_MAP.get(this.charset.toUpperCase());
            fos.write(bom);
        }
        osw = new OutputStreamWriter(fos,charset);
        writer = new BufferedWriter(osw);
    }

    /**
     * ファイルをクローズ
     * @throws IOException
     */
    public void close() throws IOException {
        if(null!=writer){
            this.writer.close();
        }
        if(null!=this.osw){
            this.osw.close();
        }
        if(null!=this.fos){
            this.fos.close();
        }
    }

    /**
     * レコード書き込み
     * @param columns
     * @throws IOException 
     */
    public void writeRecord(List<String> columns) throws IOException{
        boolean firstCol = true;
        StringBuffer lineBuf = new StringBuffer();
        for(String col : columns){
            if(firstCol){
                firstCol=false;
            } else {
                lineBuf.append(this.delimiter);
            }
            String val = col.replace("\"", "\"\"");
            lineBuf.append("\"").append(val).append("\"");
        }
        lineBuf.append("\n");
        writer.write(lineBuf.toString());
    }

    /**
     * Mac/WindowsのExcelダブルクリックでそのまま開ける形式のCSV出力設定
     * open前にコールする必要があります。
     */
    public void excelCsvFormat() {
        this.addBOM = true;
        this.charset = "UTF-16LE";
        this.delimiter = "\t";
    }
}
