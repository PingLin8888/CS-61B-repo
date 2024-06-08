package core;

import tileengine.TERenderer;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        //main menu with options. navigate via keyboard

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameMenu();
            }
        });
    }
}
