package com.wendy.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DecimalTo62UtilsTest {
    @Test
    void test_decimalTo62(){
        String res = DecimalTo62Utils.decimalTo62(63);
        Assertions.assertEquals("11", res);
    }


}