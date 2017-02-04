package de.reikodd.ddweki;

import java.util.ArrayList;

public class IntToArray {

    public static ArrayList<String> getArray(int number)    {

        ArrayList<String> numbers = new ArrayList<String>();
        for(int i=0; i<number; i++)
        {
            numbers.add("S"+i);
        }

        return(numbers);
    }
}