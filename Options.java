import javax.swing.JOptionPane;
import java.util.*;
public class Options {
    int dimi =7;
    int difi =6;
    
    public Options () {
        String[] dimChoice = new String [3];
        String[] difChoice = new String [3];
        dimChoice[0] = "7 x 7";
        dimChoice[1] = "11 x 11";
        dimChoice[2] = "15 x 15";
        difChoice[0] = "Easy";
        difChoice[1] = "Medium";
        difChoice[2] = "Hard";
        
        String whichSize = (String) JOptionPane.showInputDialog(null, "Choose a Size","Minesweeper",JOptionPane.QUESTION_MESSAGE,null,dimChoice,dimChoice[0]);
        int indexDim = Arrays.asList(dimChoice).indexOf(whichSize);
    
        String whichDif = (String) JOptionPane.showInputDialog(null, "Choose a Difficulty","Minesweeper",JOptionPane.QUESTION_MESSAGE,null,difChoice,difChoice[0]);
        int indexDif = Arrays.asList(difChoice).indexOf(whichDif);
        
        if (whichSize == dimChoice [0]) {
            dimi = 7;
        }
        if (whichSize == dimChoice [1]) {
            dimi = 11;
        }
        if (whichSize == dimChoice [2]) {
            dimi = 15;
        }
        if (whichDif == difChoice [0]) {
            difi = 8;
        }
        if (whichDif == difChoice [2]) {
            difi = 6;
        }
        if (whichDif == difChoice [2]) {
            difi = 4;
        }
    }
}
