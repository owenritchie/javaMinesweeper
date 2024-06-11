import javax.swing.*;
import java.util.Objects;
import java.awt.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;

public class Window extends JFrame implements MouseListener, ActionListener {
  private final Options options = new Options();
  final int dim = options.dimi;
  final int dif = options.difi;

  private final Tile[][] buttons = new Tile[dim][dim];

  JFrame frame = new JFrame();

  JPanel scorePanel = new JPanel();
  JLabel timerText = new JLabel();
  JButton playButton = new JButton();
  JLabel flagsLeft = new JLabel();

  JPanel hsPanel = new JPanel();
  JLabel flippedTiles = new JLabel();
  JButton highscores = new JButton();

  private int bombs = 0;
  private int flaggedBombs = 0;
  private int tilesOpen;
  private int timeGone = 0;
  private Timer gameTime;
  private Score tablePanel;
  private boolean firstClick = true;
  private boolean hasWon = false;
  private static final int MIN_BOMBS = 2;

  public Window() {
    frame.setSize(800, 790);
    frame.getContentPane().setBackground(new Color(63, 78, 83));
    frame.setLayout(new BorderLayout());
    frame.setVisible(true);

    scorePanel.setLayout(new GridLayout(1, 3));
    scorePanel.setBounds(0, 0, 800, 150);
    scorePanel.setBackground(Color.decode("#F0D100"));

    hsPanel.setLayout(new GridLayout(1, 2));
    hsPanel.setBounds(0, 0, 800, 150);
    hsPanel.setBackground(Color.decode("#F0D100"));

    flippedTiles.setForeground(Color.decode("#3E3E3E"));
    flippedTiles.setFont(new Font("Monospaced", Font.BOLD, 20));
    flippedTiles.setText("Last Score: ");
    flippedTiles.setHorizontalAlignment(JLabel.CENTER);

    timerText.setForeground(Color.decode("#3E3E3E"));
    timerText.setFont(new Font("Monospaced", Font.BOLD, 20));
    timerText.setText("0");
    timerText.setHorizontalAlignment(JLabel.CENTER);

    flagsLeft.setForeground(Color.decode("#3E3E3E"));
    flagsLeft.setFont(new Font("Monospaced", Font.BOLD, 20));
    flagsLeft.setText("FLAGS LEFT:" + (dim * dim / dif));
    flagsLeft.setHorizontalAlignment(JLabel.CENTER);

    playButton.setBackground(Color.decode("#F0D100"));
    playButton.setText("START GAME");
    playButton.setFont(new Font("Monospaced", Font.BOLD, 20));
    playButton.setForeground(Color.decode("#3E3E3E"));
    playButton.addActionListener(this);

    highscores.setBackground(Color.decode("#F0D100"));
    highscores.setText("HIGH SCORES");
    highscores.setFont(new Font("Monospaced", Font.BOLD, 20));
    highscores.setForeground(Color.decode("#3E3E3E"));
    highscores.addActionListener(this);

    frame.add(scorePanel, BorderLayout.NORTH);
    frame.add(hsPanel, BorderLayout.SOUTH);
    hsPanel.add(highscores);
    hsPanel.add(flippedTiles);
    scorePanel.add(flagsLeft);
    scorePanel.add(playButton);
    scorePanel.add(timerText);

    JPanel gamePanel = new JPanel();
    gamePanel.setLayout(new GridLayout(dim, dim));
    gamePanel.setBackground(Color.decode("#3E4142"));

    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        buttons[r][c] = new Tile(r, c);
        buttons[r][c].addMouseListener(this);
        buttons[r][c].setEnabled(false);
        buttons[r][c].setBackground(Color.decode("#3E4142"));
        buttons[r][c].setText("");
        gamePanel.add(buttons[r][c]);
      }
    }

    frame.add(gamePanel);
    gamePanel.revalidate();
    gamePanel.repaint();
    hasWon = false;
    frame.setResizable(false);
    this.setTitle("Minesweeper");
  }

  public static void main(String[] args) {
    new Window();
  }

  public void newGame() {
    gameTime = new Timer(1000, this);
    flagsLeft.setText("FLAGS LEFT:" + (dim * dim / dif));
    gameTime.start();

    timeGone = 0;
    tilesOpen = 0;
    bombs = 0;
    firstClick = true;

    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        buttons[r][c].setValue(0);
        buttons[r][c].setEnabled(true);
        buttons[r][c].setText("");
        buttons[r][c].setBackground(Color.decode("#3E4142"));
      }
    }

    flaggedBombs = 0;
  }

  public void placeBombs(int initialRow, int initialColumn) {
    Random randNum = new Random();
    bombs = 0;

    while (bombs < dim * dim / dif) {
      int r = randNum.nextInt(dim);
      int c = randNum.nextInt(dim);

      if (buttons[r][c].getValue() == 0 && buttons[r][c].isEnabled() && !isWithinRadius(initialRow, initialColumn, r, c, 1)) {
        buttons[r][c].setValue(1);
        buttons[r][c].setText("");
        bombs++;
      }
    }
  }

  private boolean isWithinRadius(int initialRow, int initialColumn, int r, int c, int radius) {
    return Math.abs(initialRow - r) <= radius && Math.abs(initialColumn - c) <= radius;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == gameTime) {
      timeGone++;
      timerText.setText(String.valueOf(timeGone));
    } else if (e.getSource() == playButton) {
      newGame();
    } else if (e.getSource() == highscores) {
      Score src = new Score();
      src.NAMELABEL.setText("");
      src.LASTSCORE.setText("");
    }
  }

  public int counter(int row, int column) {
    int touch = 0;
    for (int r = -1; r <= 1; r++) {
      for (int c = -1; c <= 1; c++) {
        try {
          if (buttons[row + r][column + c].getValue() == 1) {
            touch++;
          }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
      }
    }
    return touch;
  }

  public void show(int row, int column) {
    Tile tile = buttons[row][column];
    tile.setEnabled(false);
    tile.setFont(new Font("Monospaced", Font.BOLD, 20));
    tilesOpen++;

    if (counter(row, column) == 0) {
      for (int r = -1; r <= 1; r++) {
        for (int c = -1; c <= 1; c++) {
          try {
            if (buttons[row + r][column + c].getValue() != 1 && buttons[row + r][column + c] != buttons[row][column] && buttons[row + r][column + c].isEnabled()) {
              show(row + r, column + c);
            }
          } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
      }
    }

    if (tile.getValue() == 1) {
      tile.setText("B");
      tile.setBackground(Color.decode("#050505"));
      gameTime.stop();
      JOptionPane.showMessageDialog(null, "Tough Luck, Try Again.", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);

      int score = calculateScore();
      if (tablePanel == null) {
        tablePanel = new Score(score, "");
      } else {
        tablePanel.addScore(score, "");
      }

      flippedTiles.setText("Last Score: " + score);
      tile.setForeground(Color.decode("#3E4142"));
      newGame();
    } else {
      tile.setText(String.valueOf(counter(row, column)));
    }

    if (tilesOpen == dim * dim - bombs) {
      gameTime.stop();
      hasWon = true;
      JOptionPane.showMessageDialog(null, "Congrats, You've Swept The Mines!", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);

      int score = calculateScore();
      if (tablePanel == null) {
        tablePanel = new Score(score, "");
      } else {
        tablePanel.addScore(score, "");
      }

      flippedTiles.setText("Last Score: " + score);
    }
  }

  private int calculateScore() {
    int baseScore = 1000;
    int timePenalty = timeGone * 10;
    int tileBonus = tilesOpen * 200;
    int flagBonus = (bombs - flaggedBombs) * 200;
    double difficultyMultiplier = 1.0;

    switch (dif) {
    case 8:
      difficultyMultiplier = 1.0;
      break;
    case 6:
      difficultyMultiplier = 1.5;
      break;
    case 4:
      difficultyMultiplier = 2.0;
      break;
    }

    int score = (int)((baseScore - timePenalty + tileBonus + flagBonus) * difficultyMultiplier);
    if (hasWon) {
      score = score + 15000;
    }
    return Math.max(score, 0);
  }

  private int calculateTime() {
    return timeGone;
  }

  public void mousePressed(MouseEvent e) {
    Tile t = (Tile)(e.getSource());
    t.setFont(new Font("Monospaced", Font.BOLD, 20));

    if (firstClick) {
      placeBombs(t.getRow(), t.getColumn());
      firstClick = false;
    }

    if (t.isEnabled()) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        show(t.getRow(), t.getColumn());
      } else if (e.getButton() == MouseEvent.BUTTON3) {
        if (Objects.equals(t.getText(), "F")) {
          t.setText("");
          t.setBackground(Color.decode("#3E4142"));
          flaggedBombs--;
          flagsLeft.setText("FLAGS LEFT:" + (bombs - flaggedBombs));
        } else {
          t.setText("F");
          t.setBackground(Color.decode("#F0D100"));
          flaggedBombs++;
          flagsLeft.setText("FLAGS LEFT:" + (bombs - flaggedBombs));
        }
      }
    }
  }

  public void mouseClicked(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {}

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}
}

