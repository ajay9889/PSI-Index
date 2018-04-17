package com.psi.index.singapore.sp;

import com.psi.index.singapore.sp.Utils.MainController;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        MainController.getHrMinute("2018-04-14T20:00:00+08:00");
        String currentDateTimeStamp=  MainController.getDateTimeInSGT();
        System.out.print(currentDateTimeStamp);
        for (int i = 1 ; i<=5; i++) {
            String previous_days=  MainController.getAddedDate(i);
            System.out.print(previous_days);
        }
    }
}