package com.lambit.analizator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CellTable {
    private double value;
    private LocalDateTime dateTime;


    public CellTable(double value, LocalDateTime dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return dateTime +"; "+value+"";
    }

}
