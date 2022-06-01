
package IndexGUI;

import java.io.IOException;
import java.util.logging.*;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;


public class IndexTicker_Threads extends Application {
    
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        //BorderPane to contain GUI
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets (5, 20, 20, 20));
        pane.setStyle("-fx-background-color: #000000");
        BorderPane.setAlignment(pane, Pos.CENTER);
        
        //VBox for title (date and time)
        StackPane title = new StackPane();
        title.setAlignment(Pos.CENTER);
        pane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);
        
        //GridPane for indices and tickers
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(5);
        grid.setHgap(20);
        pane.setCenter(grid);

        //GRID PANE ADDITIONS
        //INDEX NAME text objects
        Text spText = new Text("S&P 500");
        indexFont(spText);
        grid.add(spText, 0, 0);
        
        Text nasText = new Text("NASDAQ");
        indexFont(nasText);
        grid.add(nasText, 0, 3);
        
        Text dowText = new Text("DJIA");
        indexFont(dowText);
        grid.add(dowText, 0, 6);
        
        Text rusText = new Text("Russell 2000");
        indexFont(rusText);
        grid.add(rusText, 0, 9);
        
        //StackPanes for % change placements
        StackPane spStack = new StackPane();
        StackPane nasStack = new StackPane();
        StackPane dowStack = new StackPane();
        StackPane rusStack = new StackPane();
        
        grid.add(spStack, 1, 0);
        grid.add(nasStack, 1, 3);
        grid.add(dowStack, 1, 6);
        grid.add(rusStack, 1, 9);
        
        //DAILY CHANGE for each index
        initialChanges(spStack, nasStack, dowStack, rusStack);
        updateChanges(spStack, nasStack, dowStack, rusStack);
        
        
        //PRICE CHANGING SECTION
        //StackPanes for PRICE CHANGE placements
        StackPane spPrice = new StackPane();
        StackPane nasPrice = new StackPane();
        StackPane dowPrice = new StackPane();
        StackPane rusPrice = new StackPane();
        
        grid.add(spPrice, 1, 1);
        grid.add(nasPrice, 1, 4);
        grid.add(dowPrice, 1, 7);
        grid.add(rusPrice, 1, 10);
        
        initialPrices(spPrice, nasPrice, dowPrice, rusPrice);
        updatePrices(spPrice, nasPrice, dowPrice, rusPrice);
        
        Scene scene = new Scene(pane, 250, 250);
        stage.setTitle("Stock Ticker");
        stage.setScene(scene);
        stage.show();

    }
   
    //Displays initial percent change and used in updatedChanges() to get updated price
    public void initialChanges(StackPane spStack, StackPane 
            nasStack, StackPane dowStack, StackPane rusStack) 
            throws InterruptedException, IOException
    {
        
        QuoteReader sp = new QuoteReader(".SPX");
        QuoteReader nas = new QuoteReader(".IXIC");
        QuoteReader dow = new QuoteReader(".DJI");
        QuoteReader rus = new QuoteReader(".RUT");

        Text spChange = dailyChangeFont(sp.currentDayChange());
        spStack.getChildren().add(spChange);
        System.out.println(sp.currentDayChange() + "\n");

        Text nasChange = dailyChangeFont(nas.currentDayChange());
        nasStack.getChildren().add(nasChange);
        System.out.println(nas.currentDayChange() + "\n");

        Text dowChange = dailyChangeFont(dow.currentDayChange());
        dowStack.getChildren().add(dowChange);
        System.out.println(dow.currentDayChange() + "\n");

        Text rusChange = dailyChangeFont(rus.currentDayChange());
        rusStack.getChildren().add(rusChange);
        System.out.println(rus.currentDayChange() + "\n");
        
    }
    
    //Updates the change in price (GREEN/RED part) every 10 seconds (in theory - it's taking longer than 10 seconds)
    public void updateChanges(StackPane spStack, StackPane 
            nasStack, StackPane dowStack, StackPane rusStack) throws IOException, 
            InterruptedException
    {
        Thread t;
        t = new Thread(() -> {
            
            while(true)
            {
                try 
                {
                    Thread.sleep(10000);
                } 
                catch (InterruptedException ex) 
                {
                    System.err.println("Thread interrupted!!!!");
                }
                
                Platform.runLater(() -> 
                {
                    try 
                    {
                        spStack.getChildren().clear();
                        nasStack.getChildren().clear();
                        dowStack.getChildren().clear();
                        rusStack.getChildren().clear();
                        
                        initialChanges(spStack, nasStack, dowStack, rusStack);
                    } 
                    catch (IOException e) 
                    {
                        System.err.println("Thread interrupted OR couldn't refresh");
                    }
                    catch (InterruptedException ex) 
                    {
                        Logger.getLogger(IndexTicker_Threads.class.getName()).log(Level.SEVERE, null, ex);
                    }
                       
                }   
                ); //eo Platform.runLater() lambda
            } //eo while loop    
        }); //eo thread lambda
        
        t.start();
    }
    
    //Displays initial prices and used in updatePrices() to get new price
    public void initialPrices(StackPane spPrice, StackPane 
            nasPrice, StackPane dowPrice, StackPane rusPrice) throws IOException
    {
        QuoteReader sp = new QuoteReader(".SPX");
        QuoteReader nas = new QuoteReader(".IXIC");
        QuoteReader dow = new QuoteReader(".DJI");
        QuoteReader rus = new QuoteReader(".RUT");
        
        Text spPriceT = priceFont(sp.currentQuote());
        spPrice.getChildren().add(spPriceT);
        System.out.println(sp.currentDayChange() + "\n");

        Text nasPriceT = priceFont(nas.currentQuote());
        nasPrice.getChildren().add(nasPriceT);
        System.out.println(nas.currentDayChange() + "\n");
        
        Text dowPriceT = priceFont(dow.currentQuote());
        dowPrice.getChildren().add(dowPriceT);
        System.out.println(dow.currentDayChange() + "\n");
        
        Text rusPriceT = priceFont(rus.currentQuote());
        rusPrice.getChildren().add(rusPriceT);
        System.out.println(rus.currentDayChange() + "\n");
    }
    
    //Updated prices every 10 seconds
    public void updatePrices(StackPane spPrice, StackPane nasPrice, StackPane 
            dowPrice, StackPane rusPrice) throws IOException, InterruptedException
    {
        Thread t;
        t = new Thread(() -> {
            
            while(true)
            {
                try 
                {
                    Thread.sleep(10000);
                } 
                catch (InterruptedException ex) 
                {
                    System.err.println("Thread interrupted!!!!");
                }
                
                Platform.runLater(() -> 
                {
                    try 
                    {
                        spPrice.getChildren().clear();
                        nasPrice.getChildren().clear();
                        dowPrice.getChildren().clear();
                        rusPrice.getChildren().clear();
                        
                        initialPrices(spPrice, nasPrice, dowPrice, rusPrice);
                    } 
                    catch (IOException e) 
                    {
                        System.err.println("Thread interrupted OR couldn't refresh");
                    }
                       
                }   
                ); //eo Platform.runLater() lambda
            } //eo while loop    
        }); //eo thread lambda
        
        t.start();
    }
    
    //Takes text object of an index and formats font 
    public void indexFont(Text t)
    {
        t.setFill(Color.WHITE);
        t.setStyle("-fx-font: normal 20px Arial, Helvetica, sans-serif");
    }
    
    //Takes string of [index object].currentDayChange()
    public Text dailyChangeFont(String t)
    {
        Text changeText = new Text(t);
        
        if(t.startsWith("-"))
        {
            changeText.setFill(Color.RED);
        }
        else
        {
            changeText.setFill(Color.LIMEGREEN);
        }
        
        changeText.setStyle("-fx-font: bold 21px Arial, Helvetica, sans-serif");
        
        return changeText;
    }
    
    //Takes String of the price (received from .getCurrent() in QuoteReader)
    public Text priceFont(String s)
    {
        Text t = new Text(s);
        t.setFill(Color.WHITE);
        t.setStyle("-fx-font: normal 16px Arial, Helvetica, sans-serif");
        
        return t;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
