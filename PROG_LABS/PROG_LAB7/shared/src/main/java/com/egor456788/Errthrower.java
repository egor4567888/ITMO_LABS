package com.egor456788;

import com.egor456788.exceptions.Ex;

public class Errthrower {
    public void errThrow() throws Ex {
        throw new Ex("ошибка");
    }
}
