package com.lunchinator3000.db.strategy;

import com.lunchinator3000.db.SheetsDb;
import org.openqa.selenium.interactions.Actions;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lunchinator3000.db.strategy.InterpreterStrategy.select;

public class StrategyFunctions {

    public static String arrayToString(List<String> arrayList) {
        String string = "";
        for (int i = 0; i < arrayList.size(); i++){
            string += arrayList.get(i).toString() + " ";
        }
        return string;
    }

    public static ArrayList<Object> findParameters(ArrayList<String> arrayList, int i) {
        ArrayList<String> params = new ArrayList<String>();
        ArrayList<Object> obj = new ArrayList<Object>();
        for (; i < arrayList.size(); i++) {
            if (arrayList.get(i).contains("(")) {
                do {
                    ArrayList<String> possibleParams = new ArrayList<String>(Arrays.asList(arrayList.get(i).replace("(", "").replace(")", "").split(",")));
                    String temp = "";
                        for (int j = 0; j < possibleParams.size(); j++) {
                            String possibleParamsString = possibleParams.toString().substring(1).substring(0, possibleParams.get(j).length());
                            if (possibleParamsString.contains("[")) {
                                do {
                                    temp += possibleParamsString + ", ";
                                    possibleParams = new ArrayList<String>(Arrays.asList(arrayList.get(++i).replace("(", "").replace(")", "").split(",")));
                                } while (!possibleParams.toString().substring(1).substring(0, possibleParams.get(j).length()).contains("]"));
                                possibleParamsString = possibleParams.toString().substring(1).substring(0, possibleParams.get(j).length());
                                params.add(temp + possibleParamsString);
                            } else
                                params.add(possibleParams.get(j));
                        }
                } while (!arrayList.get(i++).contains(")"));
                break;
            }
        }
        obj.add(params);
        obj.add(i);
        return obj;
    }

    public static void setRow(ArrayList<String> paramNames, ArrayList<String> params, int nextRowInDatabase) {
        Actions actions = new Actions(SheetsDb.getWebDriver());
        int xOffset = 100;
        int yOffset = 20;
        int yOffsetStart = 145;
        int xOffsetStart =  50;

        //these todos help by initializing where the selected square is at the startup of the app and not change
        //where this function starts inputting values every time it is called.
        //todo move: actions.moveByOffset(xOffsetStart, yOffsetStart).click().build().perform(); to dbConfig for initialization
        //todo change line 62 to: actions.moveByOffset(0, yOffset*nextRowInDatabase).click().build().perform();
        //can do a find column by name (if there are names in the first cells in the column/know the names of the columns - could do a private variable of column names)
        actions.moveByOffset(xOffsetStart, yOffset*nextRowInDatabase + yOffsetStart).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();

        for (int i = 1; i < paramNames.size(); i++) {
            actions.moveByOffset(xOffset, 0).click().build().perform();
            actions.moveByOffset(0, 0).sendKeys(params.get(i)).build().perform();
        }

        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back a column to save
    }
}
