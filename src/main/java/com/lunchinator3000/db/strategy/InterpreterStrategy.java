package com.lunchinator3000.db.strategy;

import com.lunchinator3000.db.SheetsDb;
import org.openqa.selenium.WebDriver;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class InterpreterStrategy {
    public static Method getStrategy(String query) {
        String[] split = query.split(" ");
        Method[] methods = InterpreterStrategy.class.getDeclaredMethods();
        Exception ex = null;
        for (int i = 0; i < split.length; i++) {
            for (int j = 0; j < methods.length; j++) {
                if (split[i].equalsIgnoreCase(methods[j].getName())) {
                    try {
                        return InterpreterStrategy.class.getMethod(methods[j].getName(), String.class);
                    } catch (NoSuchMethodException e) {
                        ex = e;
                    }
                }
            }
        }
        return null;
    }

//    private InterpreterStrategy instantiate(Class keyWord) {
//        Class<?> clazz = Class.forName("com.foo.MyClass");
//        Constructor<?> constructor = clazz.getConstructor(String.class, Integer.class);
//        Object instance = constructor.newInstance("stringparam", 42);
//    }

    public static void insert(String query) throws InvocationTargetException, IllegalAccessException {
        WebDriver driver = SheetsDb.getWebDriver();
        String [] s = query.trim().split(" ");
        ArrayList<String> split = new ArrayList<String>(Arrays.asList(s));
        ArrayList<String> paramNames = new ArrayList<String>();
        ArrayList<String> params = new ArrayList<String>();
        ArrayList<Object> objects = new ArrayList<Object>();
        int i = 0;
        int xOffset = 100;
        int yOffset = 20;
        int yOffsetStart = 145;
        int xOffsetStart =  50;
        int nextRowInDatabase = 1;

        //find the name of the sheet (Lunchinator3000)
        String table = split.get(1);

        //find the parameters' names and values
        objects = StrategyFunctions.findParameters(split, 1);
        paramNames = (ArrayList<String>) objects.get(0);
        i = (int) objects.get(1);

        query = StrategyFunctions.arrayToString(split.subList(i, split.size()));
        Method method =  getStrategy(query.replaceFirst(query, ""));
        params = (ArrayList<String>) method.invoke(method.getName(), query.toUpperCase().replaceFirst(method.getName().toUpperCase(), "").replaceAll("", ""));

        //by finding how many are filled/to go down
        nextRowInDatabase = StrategyFunctions.getNextRowNumber(SheetsDb.getWebDriver().getCurrentUrl());

        StrategyFunctions.setRow(paramNames, params, xOffset, xOffsetStart, yOffsetStart, yOffset, nextRowInDatabase);

    }

    //no per update bottom line/not yet
    public static void delete(String query) {
        //can right click on row and delete
    }

    //no per bottom line/not yet
    public static void update(String query) {
        //update table-name set column1 = value1, column2 = value2 where condition (id = 1)

        //can do a select then work with the ArrayList<ArrayList<String>>
            //by knowing what is what and where is where on the table, can more easily select columns and parts of columns - where
                //go through each column and each value - on where statements
                //and set what the query says in the array
                //then insert the whole array to the table - at least for this project - a row at a time / or update the specific cells whenever you get to them - which is this project too - because all lines need to be updated -row by row- - or just don't update for this project - only select (read) and insert (write) - and let the db keep growing - nah, for this project update everything to keep it smaller and simpler - or not, it might be faster to just keep a log and manually/programmatically delete it after it gets too big and so use the already created insert and select functions to "update"
    }

    public static ArrayList<ArrayList> select(String query) {
        //todo - check this to see why there are tds in ballots 3 and 4 where there are only 10 rows in the db
        ArrayList<ArrayList> lists = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(SheetsDb.getWebDriver().getCurrentUrl(), String.class);
        // <div class="row-header-wrapper" style="line-height: 20px;">16</div></th><td class="s0" dir="ltr">11/15/18</td><td class="s0" dir="ltr">5</td> - didn't do more than 10
        ArrayList<String> rows = new ArrayList<String>(Arrays.asList(result.split("<div class=\"row-header-wrapper\" style=\"line-height: 20px;\">.\\\\*"))); //</div></th><td class="s0" dir="ltr">
        for (int i = 1; i < rows.size(); i++ ) {

            //get around a bug with regex .* only doing .
            if (!rows.get(i).substring(0, 40).contains("td class"))
                break;
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<String> colunmsInRows = new ArrayList<String>(Arrays.asList(rows.get(i).split("<td "))); //find what's in the td - if there is something in a td, then regex for splitting it is "<td "
            for (int j = 1; j < colunmsInRows.size(); j++) {
                int nextLine = colunmsInRows.get(j).indexOf(">");
                colunmsInRows.set(j, colunmsInRows.get(j).substring(nextLine + 1));

                //adds whatever's inside of the <td> - there may or may not be a <span> - could refactor this
                nextLine = colunmsInRows.get(j).indexOf("</td>");
                int nextLineSpan = colunmsInRows.get(j).indexOf("</span>");
                if ((nextLineSpan < nextLine) && nextLineSpan != -1)
                    nextLine = nextLineSpan;
                if (nextLine > 0) {
                    if (nextLineSpan > 0) {
                        int lineInSpanIndex = colunmsInRows.get(j).indexOf(">");
                        String lineInSpan = colunmsInRows.get(j).substring(lineInSpanIndex + 1);
                        arrayList.add(lineInSpan.substring(0, lineInSpan.indexOf("</span>")));
                    }
                    else
                        arrayList.add(colunmsInRows.get(j).substring(0, nextLine));
                }
                else
                    arrayList.add(colunmsInRows.get(j));
            }
            lists.add(arrayList);
        }
        return lists;
        //just assume it's "from" the current table - though can add a from function in
    }

    public static ArrayList<String> values(String query) {
        ArrayList<Object> objects = StrategyFunctions.findParameters(new ArrayList<String>(Arrays.asList(query.trim().split(" "))), 0);
        return (ArrayList<String>) objects.get(0);
    }
}
