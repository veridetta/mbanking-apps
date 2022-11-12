package com.example.hinadadebank.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String tanggal_trans(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return  dtf.format(now);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String tanggal_no(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        return  dtf.format(now);
    }
}
