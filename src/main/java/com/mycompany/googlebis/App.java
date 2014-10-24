package com.mycompany.googlebis;

import java.io.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       try {
        System.out.println( "Hello World!" );
        File input = new File("D:\\D1.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        Element title = doc.select("title").first();
        System.out.println(title.text());
        Element meta = doc.select("meta[name=description]").first();
        System.out.println(meta.attr("content"));

       } catch(Exception exp){
           System.out.println("erreur");
       }
    }
}
