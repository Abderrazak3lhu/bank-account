package com.talan.bankaccount.bankaccount.util;

public final class AppConstants {

    private AppConstants(){
    }

    public static final String ACCOUNT_NOT_FOUND = "Account doesn't exist";
    public static final String AMOUNT_NOT_VALID = "Amount must be greater than zero";
    public static final String NOT_SUFFICIENT_FUNDS = "The fund is not sufficient";

    public static final String DEPOSIT_URL= "/deposit";
    public static final String WITHDRAW_URL= "/withdraw";
    public static final String TRANSFERT_URL= "/transfert";
    public static final String TRANSACTIONS_HISTORY_URL = "/transactions";

}
