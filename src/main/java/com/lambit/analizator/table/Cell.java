package com.lambit.analizator.table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cell {
    private double value;
//    private String date;
//    private String time;
    private boolean diviation;
//    private Date dateD;
//    private int[] dateMass;
//    private int[] timeMass;
    private LocalDateTime dateTime;

//    public Cell(double value, int[] date, int[] time, Date dateD) {
//        this.value = value;
//        this.date = date[0]+"."+date[1]+"."+date[2];
//        this.time = String.format("%2d:%02d:%02d",time[0],time[1],time[2]);
//        this.dateD = dateD;
//        this.diviation = true;//Отклонение допустимо True
//        this.dateMass = date;
//        this.timeMass = time;
//    }

    public Cell(double value, LocalDateTime dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }
//
//    @Override
//    public String toString() {
//        return date+"; "+time+"; "+value+" --- "+diviation;
//    }

    @Override
    public String toString() {
        return dateTime +"; "+value+" --- "+diviation;
    }

}
