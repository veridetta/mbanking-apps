package com.example.hinadadebank.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyRupiah {
    public String rupiah(double nominal){
        DecimalFormat kurs = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols format = new DecimalFormatSymbols();

        format.setCurrencySymbol("Rp. ");
        format.setMonetaryDecimalSeparator(',');
        format.setGroupingSeparator('.');

        kurs.setDecimalFormatSymbols(format);
        String hasil = kurs.format(nominal);
        return hasil ;
    }
}
