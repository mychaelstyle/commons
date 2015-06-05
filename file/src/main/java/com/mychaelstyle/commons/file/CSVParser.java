package com.mychaelstyle.commons.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * csv parser
 * @author Masanori Nakashima
 */
public class CSVParser implements Iterator<List<String>> {
    /** charset */
    private String charset = "UTF-8";
    /** delimiter */
    private String delimiter = ",";
    /** join lines limit number */
    private int joinLineLimit = 30;
    /** current line number */
    private int currentLineNumber = 0;
    /** file */
    private File file = null;
    /** BufferedReader */
    private BufferedReader reader = null;
    /** current record string */
    private StringBuffer record = null;
    /**
     * constructor
     * @param file
     */
    public CSVParser(File file) {
        super();
        this.file = file;
    }

    public CSVParser(File file, String charset){
        super();
        this.file = file;
        this.charset = charset;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public CSVParser withCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter the delimiter to set
     */
    public CSVParser withDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    /**
     * @return the joinLineLimit
     */
    public int getJoinLineLimit() {
        return joinLineLimit;
    }

    /**
     * @param joinLineLimit the joinLineLimit to set
     */
    public CSVParser withJoinLineLimit(int joinLineLimit) {
        this.joinLineLimit = joinLineLimit;
        return this;
    }

    /**
     * open
     * @throws Exception 
     */
    @SuppressWarnings("resource")
    public void open() throws Exception{
        FileInputStream is = new FileInputStream(this.file);
        is = (FileInputStream) skipUTF8BOM(is, this.charset);
        InputStreamReader streamReader = new InputStreamReader(is,this.charset);
        this.reader = new BufferedReader(streamReader);
        this.readRecord();
    }
    /**
     * skip BOM of UTF-8
     * @param is
     * @param charSet
     * @return
     * @throws Exception
     */
    public static InputStream skipUTF8BOM(InputStream is, String charSet)throws Exception{
       if( !charSet.toUpperCase().equals("UTF-8") ) return is;
       if( !is.markSupported() ){
           is= new BufferedInputStream(is);
       }
       is.mark(3);
       if( is.available()>=3 ){
           byte b[]={0,0,0};
           is.read(b,0,3);
           if( b[0]!=(byte)0xEF ||
                   b[1]!=(byte)0xBB ||
                   b[2]!=(byte)0xBF ){
               is.reset();
           }
       }
       return is;
    }

    /**
     * close
     * @throws IOException
     */
    public void close() throws IOException {
        if(null!=this.reader){
            this.reader.close();
            this.reader = null;
        }
        this.record = null;
    }

    /**
     * 次のレコードをメンバ文字列バッファに読み込み
     * @throws Exception 
     */
    private void readRecord() throws Exception {
        String line = this.reader.readLine();
        this.currentLineNumber++;
        int startLine = this.currentLineNumber;
        if(null==line){
            this.record = null;
            return;
        }
        this.record = new StringBuffer();
        int counter = 1;
        while(null!=line || !isValidRecord(this.record.toString())){
            if(counter>this.joinLineLimit){
                this.record = null;
                throw new Exception("The record has more than "+this.joinLineLimit
                        +" lines! : line "+ startLine+"-"+this.currentLineNumber+ " "+this.file.getAbsolutePath()
                        +" : "+this.record.toString());
            }
            this.record.append(line);
            if(isValidRecord(this.record.toString())){
                break;
            }
            line = this.reader.readLine();
            this.currentLineNumber++;
            counter++;
        }
    }

    /**
     * レコードとして完結しているか確認
     * @param str
     * @return
     */
    public static boolean isValidRecord(String str){
         if(str.split("\"",-1).length % 2 ==1){
            // "の数が偶数の場合,フォーマットが崩れていたとしてもカラムとして完結しているはず
             return true;
        } else {
            // "の数が奇数の場合は行として完結しているか確認
            if(str.startsWith("\"") || str.contains(",\"") || str.contains("\",")){
                // "で始まるフィールドがあるなら
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 文字列をCVSレコードListに変換
     * @param text
     * @return
     * @throws Exception 
     */
    public static  List<String> splitRecords(String text) throws Exception{
        List<String> records = new ArrayList<String>();
        String body = text.replace("\r\n", "\n");
        body = body.replace("\r", "\n");
        String[] lines = body.split("\n",-1);
        StringBuffer recordBuf = new StringBuffer();
        for(String line : lines){
            recordBuf.append(line);
            String recordStr = recordBuf.toString();
            if(isValidRecord(recordStr)){
                records.add(recordStr);
                recordBuf = new StringBuffer();
            } else {
                continue;
            }
        }
        if(recordBuf.length()>0){
            throw new Exception("Invalid records!");
        }
        return records;
    }
    /**
     * 1レコードのCSVをListに変換
     * @param line
     * @param delimiter
     * @return
     */
    public static List<String> parseLine(String line, String delimiter){
        List<String> columns = new ArrayList<String>();
        String[] cols = line.split(delimiter,-1);
        StringBuffer column = new StringBuffer();
        for(String col : cols){
            column.append(col);
            String c = column.toString();
            if(c.startsWith("\"")){
                String[] elms = c.split("\"",-1);
                if(elms.length % 2 ==1){
                    // "の数が偶数の場合,フォーマットが崩れていたとしてもカラムとして完結しているはず
                    columns.add(c);
                    column = new StringBuffer();
                } else {
                    // "の数が奇数の場合はカラムとして完結していないので次の要素とつなげる
                    continue;
                }
            } else {
                // 内部が何であろうとクォートされていないのでカラムとして完結
                columns.add(c);
                column = new StringBuffer();
            }
        }
        if(line.endsWith(delimiter) || column.length()>0){
            columns.add(column.toString());
        }
        return columns;
    }
    @Override
    public boolean hasNext() {
        if(null==this.reader || null==this.file){
            throw new RuntimeException("CSV file is not opened!!");
        }
        if(null==this.record){
            return false;
        }
        return true;
    }
    @Override
    public List<String> next() {
        String rec = this.record.toString();
        try {
            this.readRecord();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parseLine(rec, this.delimiter);
    }
    @Override
    public void remove() {
        try {
            this.readRecord();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
