import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;

public class Score extends JDialog {
  private final int[] highScores = new int[10];
  private final String[] names = new String[10];
  private final String[][] tableData = {
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    },
    {
      "0",
      ""
    }
  };

  JLabel LASTSCORE = new JLabel();
  JLabel MINESWEEPER = new JLabel();
  JLabel NAMELABEL = new JLabel();
  JPanel tablePanel = new JPanel();
  public Score() {
    this.setBounds(800, 350, 500, 235);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JPanel titlePanel = new JPanel();

    tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
    titlePanel.setLayout(new GridLayout(1, 3));

    titlePanel.setBackground(Color.decode("#F0D100"));

    LASTSCORE.setForeground(Color.decode("#3E3E3E"));
    LASTSCORE.setFont(new Font("Monospaced", Font.BOLD, 15));
    LASTSCORE.setText("LAST SCORE: ");
    LASTSCORE.setHorizontalAlignment(JLabel.CENTER);

    MINESWEEPER.setForeground(Color.decode("#3E3E3E"));
    MINESWEEPER.setFont(new Font("Monospaced", Font.BOLD, 15));
    MINESWEEPER.setText("MINESWEEPER");
    MINESWEEPER.setHorizontalAlignment(JLabel.CENTER);

    NAMELABEL.setForeground(Color.decode("#3E3E3E"));
    NAMELABEL.setFont(new Font("Monospaced", Font.BOLD, 15));
    NAMELABEL.setText("USER: ");
    NAMELABEL.setHorizontalAlignment(JLabel.CENTER);

    String[] columnNames = {
      "Score",
      "Name"
    };
    JTable scoreTable = new JTable(tableData, columnNames);
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    scoreTable.setDefaultRenderer(Object.class, centerRenderer);
    scoreTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    scoreTable.setBackground(Color.decode("#3E3E3E"));
    scoreTable.setForeground(Color.decode("#c7c7c7"));

    titlePanel.add(LASTSCORE);
    titlePanel.add(MINESWEEPER);
    titlePanel.add(NAMELABEL);
    tablePanel.add(titlePanel);
    tablePanel.add(scoreTable);

    this.setContentPane(tablePanel);
    loadScores();
    this.setVisible(true);
    repaint();
  }

  public Score(int score, String name) {

    this();
    addScore(score, name);
    JOptionPane.showMessageDialog(null, "Press OK to restart");
    this.dispose();
  }

  public void addScore(int score, String name) {
    name = JOptionPane.showInputDialog("Enter your name:");

    NAMELABEL.setText("USER: " + name);
    LASTSCORE.setText("LAST SCORE: " + (score));
    for (int i = 0; i < 10; i++) {
      if (score > Integer.parseInt(tableData[i][0])) {
        for (int j = 9; j > i; j--) {
          tableData[j][0] = tableData[j - 1][0];
          tableData[j][1] = tableData[j - 1][1];
        }
        tableData[i][0] = (Integer.toString(score));
        tableData[i][1] = name;
        repaint();
        break;
      }
    }
    saveScores();
  }

  public void saveScores() {

    try {
      PrintWriter printScore = new PrintWriter("scores.txt");
      for (int i = 0; i < 10; i++) {
        printScore.println(tableData[i][0]);
        printScore.println(tableData[i][1]);
      }
      printScore.close();
    } catch (IOException e) {
      System.out.println("Cannot open file.");
    }

  }
  public void loadScores() {

    try {
      BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
      for (int i = 0; i < 10; i++) {
        tableData[i][0] = reader.readLine();

        tableData[i][1] = reader.readLine();

      }
      reader.close();
    } catch (IOException | NumberFormatException ignored) {

    }
    revalidate();
    repaint();
  }
}