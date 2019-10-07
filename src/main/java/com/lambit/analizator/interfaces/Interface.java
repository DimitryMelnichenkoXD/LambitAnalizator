package com.lambit.analizator.interfaces;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Data
@NoArgsConstructor
public class Interface{

    private File file;

    public void drowInterface() {
        JFrame frame = new JFrame("Lambit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(300,200);

        Box upperBox = Box.createHorizontalBox();
        JLabel maxDivLable = new JLabel("Мксимальное отклонение:");
        final JTextField maxDivField = new JTextField();
        upperBox.add(maxDivLable);
        upperBox.add(maxDivField);

        Box midleBox = Box.createHorizontalBox();
        JLabel minDivLable = new JLabel("Минимальное отклонение:");
        final JTextField minDivField = new JTextField();
        midleBox.add(minDivLable);
        midleBox.add(minDivField);

        Box bottomBox = Box.createHorizontalBox();
        JLabel numDivLable = new JLabel("Ожидаемое значение:");
        final JTextField numDivField = new JTextField();
        bottomBox.add(numDivLable);
        bottomBox.add(numDivField);

        Button startButton = new Button("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Controller controller = new Controller(maxDivField.getText(),minDivField.getText(),numDivField.getText(),getFile());
              //  controller.startModel();
            }
        });

        Button chooseFileButton = new Button("Select file");
        chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectFileForAnaliz();
            }
        });

        frame.add(getPrimaryBox(upperBox, midleBox, bottomBox, startButton, chooseFileButton));
        frame.setVisible(true);

    }

    private Box getPrimaryBox(Box upperBox, Box midleBox, Box bottomBox, Button startButton, Button chooseFileButton) {
        Box primaryBox = Box.createVerticalBox();
        primaryBox.add(upperBox);
        primaryBox.add(midleBox);
        primaryBox.add(bottomBox);
        primaryBox.add(chooseFileButton);
        primaryBox.add(startButton);
        return primaryBox;
    }//Создание глвного вертикального макета

    private void selectFileForAnaliz(){
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            this.file = fileopen.getSelectedFile();
        }
    }//Выбор файла
}

