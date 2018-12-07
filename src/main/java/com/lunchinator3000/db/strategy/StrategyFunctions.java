package com.lunchinator3000.db.strategy;

import com.lunchinator3000.db.SheetsDb;
import org.openqa.selenium.interactions.Actions;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static int getNextRowNumber(String currentUrl) {
        int nextRow = 0;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(currentUrl, String.class);
        ArrayList<String> split = new ArrayList<String>(Arrays.asList(result.split("\n")));
        for (int i = 0; i < split.size(); i++ ) {
            if (split.get(i).contains("<title>"))
                break;
            if (split.get(i).equals("")) {
                do {
                    nextRow++;
                } while (!split.get(i++).contains("meta"));
                break;
            }
        }
        return nextRow + 1;
    }

    public static void setRow(ArrayList<String> paramNames, ArrayList<String> params, int xOffset, int xOffsetStart, int yOffsetStart, int yOffset, int nextRowInDatabase) {
        Actions actions = new Actions(SheetsDb.getWebDriver());

        //can do a find column by name (if there are names in the first cells in the column/know the names of the columns - could do a private variable of column names)
        actions.moveByOffset(xOffsetStart, yOffset*nextRowInDatabase + yOffsetStart).click().build().perform();
        actions.moveByOffset(0, 0).sendKeys(params.get(0)).build().perform();

        // todo: do a check to see if they are the params
        for (int i = 1; i < paramNames.size(); i++) {
            actions.moveByOffset(xOffset, 0).click().build().perform();
            actions.moveByOffset(0, 0).sendKeys(params.get(i)).build().perform();
        }

        actions.moveByOffset(-xOffset, 0).click().build().perform(); //back a column to save
    }

// deprecated with String.indexOf
    public static Integer getIndexAtChar(String text, String chars) {
        String[] ch = text.split("");
        String[] chComp = chars.split("");
        int index = 0;
        for (int i = 0; i < ch.length; i++) {
            if (ch[i].equals(chComp[0])) {
                index = i;
                for (int j = 0; j < chars.length(); j++) {
                    if (ch[i++].equals(chComp[j])) { }
                    else
                        return 0;
                }
                return index;
            }
        }
        return 0;
    }
}
