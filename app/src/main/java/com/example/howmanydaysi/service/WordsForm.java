package com.example.howmanydaysi.service;

public final class WordsForm {
    public static String getDayWordForm(int quantityDays)//чисто для согласованности числа и слова день))
    {
        if(quantityDays%10==1&&quantityDays!=11)
            return "день";
        else if(quantityDays%10>1&&quantityDays%10<5&&(quantityDays<10||quantityDays>20))
            return "дня";
        else
            return "дней";
    }

}
