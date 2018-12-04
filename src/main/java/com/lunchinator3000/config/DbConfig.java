package com.lunchinator3000.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.Lunchinator3000Application;
import com.lunchinator3000.controllers.BallotController;
import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.db.SheetsDb;
import com.lunchinator3000.service.BallotService;
import com.lunchinator3000.service.DbService;
import org.openqa.selenium.chrome.ChromeDriver;
import com.lunchinator3000.service.RestaurantService;
import com.lunchinator3000.service.VoteService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Configuration
public class DbConfig {

    @Value("${db.sheets.url}")
    private String SPREADSHEET_URL;

    @Bean
    public WebDriver webDriver() {
        String path = Lunchinator3000Application.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("target/classes/", "src/main/resources/chromedriver");
        if (System.getProperty("os.name").contains("Mac"))
            System.setProperty("webdriver.chrome.driver",path);
        else
            System.setProperty("webdriver.chrome.driver",path + ".exe");

        ChromeOptions options = new ChromeOptions();
//		options.setHeadless(true);
		options.addArguments("window-size=1920, 1080");
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.navigate().to(SPREADSHEET_URL);
        ArrayList<String> params = new ArrayList<String>();
        params.add("11/15/18");
        params.add("5");
        Actions actions = new Actions(webDriver);
        int xOffset = 105;
        int yOffset = 20;
        int yOffsetStart = 145;
        int xOffsetStart =  50;
        //the first line
        /*actions.moveByOffset(xOffset, yOffset + 145).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();
        actions.moveByOffset(0, 0).click().build().perform();

        actions.moveByOffset(140, 0).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(1)).build().perform();
        actions.moveByOffset(-xOffset, 0).click().build().perform();*/

        //the second line from 0
        /*actions.moveByOffset(xOffset, yOffset*2 + 145).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();
        actions.moveByOffset(0, 0).click().build().perform();

        actions.moveByOffset(140, 0).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(1)).build().perform();
        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back to beginning of row*/

        //the 3rd line from 0
        /*actions.moveByOffset(xOffset, yOffset*3 + 145).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();
        actions.moveByOffset(0, 0).click().build().perform();

        actions.moveByOffset(140, 0).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(1)).build().perform();
        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back to beginning of row*/

        //the 4th line from 0
        /*actions.moveByOffset(xOffset, yOffset*4 + 145).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();

        actions.moveByOffset(140, 0).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(1)).build().perform();
        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back to beginning of row*/

        /*//the nth line from 0
        int n = 16;
        //setFirstCellInRow
        actions.moveByOffset(xOffsetStart, yOffset*n + yOffsetStart).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();

        actions.moveByOffset(xOffset, 0).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(1)).build().perform();

        //todo call this at the end of all current processes
        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back to beginning of row*/


        //todo: 2 rows in a row from 0
        //todo: n columns from 0-colunm

        return webDriver;
    }

    @Bean
    public SheetsDb sheetsDb(WebDriver driver){
        return new SheetsDb(driver);
    }

    @Bean
    public DbService dbService(SheetsDb sheetsDb){
        return new DbService(sheetsDb);
    }

    //todo set up a config for postgresql server with a lunchinator3000 db, and create a restaurant table if there is not already one there
}
