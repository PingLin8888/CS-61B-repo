package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // build your own world!
        //main menu with options. navigate via keyboard
        GameMenu gameMenu = new GameMenu();
        gameMenu.createGameMenu();
    }
}
