package core;

import tileengine.TERenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by gpt
 */
public class GameMenu extends JFrame {
    private StringBuilder seedBuilder = new StringBuilder();
    private boolean enteringSeed = false;
    private MenuPanel menuPanel;
    private JLabel promptLabel;

    public GameMenu() {
        setTitle("CS61B: THE GAME");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        menuPanel = new MenuPanel();
        add(menuPanel);


        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (enteringSeed) {
                    if (Character.isDigit(e.getKeyChar())) {
                        seedBuilder.append(e.getKeyChar());
                        updateSeedLabel();
                    } else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
                        finalizeSeed();
                    }
                } else {
                    switch (e.getKeyChar()) {
                        case 'n':
                        case 'N':
                            startSeedEntry();
                            break;
                        case 'l':
                        case 'L':
                            loadGame();
                            break;
                        case 'q':
                        case 'Q':
                            quitGame();
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setVisible(true);
    }

    private void finalizeSeed() {
        if (seedBuilder.length() > 0) {
            Long seed = Long.parseLong(seedBuilder.toString());
            startNewGameWithSeed(seed);
            enteringSeed = false;
            menuPanel.remove(promptLabel);
            menuPanel.repaint();
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

        teRenderer.renderFrame(world.getMap());
    }


    private void updateSeedLabel() {
        promptLabel.setText("Enter seed: " + seedBuilder.toString());
        menuPanel.repaint();
    }

    private void startSeedEntry() {
        enteringSeed = true;
        seedBuilder.setLength(0);
        promptLabel = new JLabel("Enter seed: ");
        promptLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        promptLabel.setForeground(Color.WHITE);
        menuPanel.removeAll();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.add(promptLabel);
        menuPanel.revalidate();
        menuPanel.repaint();
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

    private class MenuPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the title
            g2d.setFont(new Font("Serif", Font.BOLD, 48));
            g2d.setColor(Color.WHITE);
            String title = "CS61B: THE GAME";
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = (getWidth() - fm.stringWidth(title)) / 2;
            int titleY = getHeight() / 4;
            g2d.drawString(title, titleX, titleY);

            if (!enteringSeed) {
                // Draw the options
                g2d.setFont(new Font("Serif", Font.PLAIN, 24));
                String[] options = {"New Game (N)", "Load Game (L)", "Quit (Q)"};
                for (int i = 0; i < options.length; i++) {
                    String option = options[i];
                    int optionX = (getWidth() - fm.stringWidth(option)) / 2;
                    int optionY = getHeight() / 2 + (i * 50);
                    g2d.drawString(option, optionX, optionY);
                }
            }
        }
    }

}


