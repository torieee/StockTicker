
package IndexGUI;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QuoteReader {
    
    private String open;
    private String current;
    private String ytd;
    private String dayChange;
    String cnbcURL;
  
    public QuoteReader(String index) throws IOException 
    {
        cnbcURL = "https://www.cnbc.com/quotes/" + index;
        
        try
        {
            this.open = getOpen();
            this.current = getCurrent();
            this.ytd = getYTD();
            this.dayChange = getDayChange();
            
            System.out.println(index);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Market is closed!");
        }
    }
    
    //Gets daily OPENING quote (updated on CNBC daily)
    private String getOpen() throws IOException
    {
        String open = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("li[class=Summary-stat Summary-prevClose]");
        
        for (Element e : divs) 
        {
            open = e.getElementsByClass("Summary-value").text().substring(0, 8);
            break;
        }
        
        return open;
    } 
    
    public String currentOpen()
    {
        return this.open;
    }
    
    public String toStringOpen()
    {
        return open + "";
    }
    
    //Gets CURRENT quote (updated on CNBC every few seconds)
    private String getCurrent() throws IOException 
    {
        String current = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("div[class=QuoteStrip-lastPriceStripContainer]");
        
        for (Element e : divs) 
        {
            current = e.getElementsByClass("QuoteStrip-lastPrice").text();
        }
        
        return current;
    }
    
    public String currentQuote()
    {
        return this.current;
    }
    
    public String toStringCurrent()
    {
        return current + "";
    }
    
    //Gets the current daily PERCENT CHANGE (updated every few seconds on CNBC)
    private String getDayChange() throws IOException
    {
        //Get rid of commas so we can parse string values to double values
        String currentVar = getCurrent().replace(",", "");
        String openVar = getOpen().replace(",", "");

        
        String dayChange = "";
        String change = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("div[class=QuoteStrip-lastPriceStripContainer]");
        
        for (Element e : divs) 
        {
            if(Double.parseDouble(currentVar) > Double.parseDouble(openVar))
            {
                dayChange = e.getElementsByClass("QuoteStrip-changeUp").text();
                change = dayChange.substring(dayChange.indexOf("(") + 1, 
                        dayChange.indexOf(")") - 1);
            }
            else if(Double.parseDouble(currentVar) == Double.parseDouble(openVar))
            {
                dayChange = e.getElementsByClass("QuoteStrip-changeUp").text();
                change = dayChange.substring(dayChange.indexOf("(") + 1, 
                        dayChange.indexOf(")") - 1);
            }
            else
            {
                dayChange = e.getElementsByClass("QuoteStrip-changeDown").text();
                change = dayChange.substring(dayChange.indexOf("(") + 1, 
                         dayChange.indexOf(")") - 1);
            }
            
            break;
        }
        
        System.out.println("\n" + change);
        
        return change + "%";
        
    }
    
    public String currentDayChange()
    {
        return this.dayChange;
    }
    
    public String toStringDayChange()
    {
        return dayChange + "";
    }
    
    
    private String getYTD() throws IOException
    {
        String janOpen = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("li[class=Summary-stat Summary-ytdPercChange]");
        
        for (Element e : divs) 
        {
            janOpen = e.getElementsByClass("Summary-value").text();
            break;
        }
        
        return janOpen;
    } //eo getYTD()
    
    public String currentYTD()
    {
        return this.ytd;
    }
    
    public String toStringYTD()
    {
        return "" +  ytd + "%";
    }
}
