package com.lambit.analizator.interfaces;

import javax.swing.*;

public class RezultFrame extends JFrame {

    public RezultFrame(String title, int width, int height) {
        super(title);
        super.setSize(width, height);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }

}
