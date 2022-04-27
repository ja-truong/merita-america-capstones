package com.techelevator.tenmo.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicLogger {

    public static void log(String message){
        String logpath = "tenmo-client\\log.txt";
        File logFile = new File(logpath);
        try(PrintWriter log = new PrintWriter(new FileOutputStream(logFile,true))){
            try {
                log.print(LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")));
                log.println(": " + message);


            } catch (DateTimeException e) {
                System.err.println("There is a problem with your temporal location, please see a local time traveler for help");
            }

        }catch (FileNotFoundException e){
            //throw(FileNotFoundException e);
            System.out.println("File not found.:(");

        }

    }

}
