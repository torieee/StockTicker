# StockTicker
JavaFX application that displays the percent change and price of the major stock indices and updates continuously.

This application was made with JavaFX and the Jsoup API and uses data from CNBC. It tracks the four major US stock indices: S&P 500 (US large cap), Nasdaq (US technology), Dow Jones Industrial Average (US industrials), and Russell 2000 (US small cap). The application displays the day's percent change in price and the current price of each of the indices and updates every 10 seconds. 

QuoteReader.java scrapes data from CNBC using Jsoup for a generic CNBC stock. The constructor of this class takes a string, which is a stock ticker symbol (ex: .SPX for the S&P 500, APPL for Apple, etc.), so the class can be used for future projects relating to stock data without many changes. 

IndexTicker_Threads.java creates the GUI, calls applicable methods for objects of QuoteReader.java, formats the data, and creates threads for updates to the data every 10 seconds. 
