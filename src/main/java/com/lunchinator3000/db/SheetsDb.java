package com.lunchinator3000.db;

import com.lunchinator3000.db.strategy.InterpreterStrategy;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SheetsDb {

    private static WebDriver webDriver;

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    @Autowired
    public SheetsDb(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public ResponseEntity<?> execute(String query) {
        Method method = InterpreterStrategy.getStrategy(query);// finds first key word
        Object object;
        try {
//            replaces first key word found
            object = method.invoke(method.getName(), query.toUpperCase().replaceFirst(method.getName().toUpperCase(), ""));
            return new ResponseEntity<>(object, HttpStatus.OK);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); //put this in a log exception
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (InvocationTargetException e) {
            e.printStackTrace();//put this in a log exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
