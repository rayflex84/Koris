package com.torepofficiel.carem.koris.com.torepofficiel.carem.enums;

/**
 * Created by macbook on 25/08/17.
 */

public enum Type_Account  {

    //Resources.getSystem().getStringArray(R.array.list_account_type);

    info("0"),
    personnel("1"),
    commercial("2"),
    marchand("3");

    private String type = "";

    Type_Account(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}