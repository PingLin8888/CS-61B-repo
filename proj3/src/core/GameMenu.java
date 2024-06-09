package core;

import org.checkerframework.checker.units.qual.K;
import tileengine.TERenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameMenu extends JFrame {

    private StringBuilder seedBuilder = new StringBuilder();
    private JLabel seedLabel = new JLabel("Enter seed: ");
    private boolean enteringSeed = false;

    public GameMenu() {
        setTitle("CS61B: THE GAME");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton newGameButton = new JButton("NEW GAME (N)");
        JButton loadButton = new JButton("LOAD GAME (L)");
        JButton quitButton = new JButton("QUIT (Q)");

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSeedEntry();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitGame();


            }
        });

        panel.add(newGameButton);
        panel.add(loadButton);
        panel.add(quitButton);
        panel.add(seedLabel);

        add(panel);
        setVisible(true);

        // Add Key Bindings for "N", "L", and "Q" keys
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "startNewGame");
        actionMap.put("startNewGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSeedEntry();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "loadGame");
        actionMap.put("loadGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quitGame");
        actionMap.put("quitGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitGame();
            }
        });

        for (int i = 0; i <= 9; i++) {
            int num = i;
            inputMap.put(KeyStroke.getKeyStroke((char) ('0' + i)), "enterDigit" + i);
            actionMap.put("enterDigit" + i, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (enteringSeed) {
                        seedBuilder.append(num);
                        updateSeedLabel();
                    }
                }
            });
        }

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "finaliseSeed");
        actionMap.put("finaliseSeed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enteringSeed) {
                    finalizeSeed();
                }
            }
        });
        panel.requestFocusInWindow();
    }

    private void finalizeSeed() {
        if (seedBuilder.length() > 0) {
            Long seed = Long.parseLong(seedBuilder.toString());
            startNewGameWithSeed(seed);
            enteringSeed = false;
            seedLabel.setText("Enter Seed: ");
        } else {
            JOptionPane.showMessageDialog(null, "Seed cannot be empty. Please enter a valid seed.");
        }
    }

    private void startNewGameWithSeed(Long seed) {
        JOptionPane.showMessageDialog(null, "Start new game with seed: " + seed);
        World world = new World(seed);
        TERenderer teRenderer = new TERenderer();
        int width = world.getMap().length;
        int height = world.getMap()[0].length;
        teRenderer.initialize(width, height);

        world.buildWorld(40);

        teRenderer.renderFrame(world.getMap());
    }


    private void updateSeedLabel() {
        seedLabel.setText("Enter seed: " + seedBuilder.toString());
    }

    private void startSeedEntry() {
        JOptionPane.showMessageDialog(null, "Creating a new game. Please enter a new seed.");
        enteringSeed = true;
        seedBuilder.setLength(0);
        updateSeedLabel();
    }

    private void loadGame() {
        JOptionPane.showMessageDialog(null, "Loading game...");
    }

    private void quitGame() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        JOptionPane.showMessageDialog(null, "Quiting game...");
    }

}


