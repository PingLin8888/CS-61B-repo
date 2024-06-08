package core;

import tileengine.TERenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JFrame {
    public GameMenu() {
        setTitle("CS61B: THE GAME");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton newGameButton = new JButton("NEW GAME (N)");
        JButton loadButton = new JButton("LOAD GAME (L)");
        JButton quitButton = new JButton("QUIT (Q)");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "New game...");
                World world = new World();
                TERenderer teRenderer = new TERenderer();
                int width = world.getMap().length;
                int height = world.getMap()[0].length;
                teRenderer.initialize(width, height);

                world.buildWorld(60);

                teRenderer.renderFrame(world.getMap());

            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Loading game...");


            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);

                }
                JOptionPane.showMessageDialog(null, "Quiting game...");


            }
        });

        panel.add(newGameButton);
        panel.add(loadButton);
        panel.add(quitButton);

        add(panel);
        setVisible(true);

    }
}


