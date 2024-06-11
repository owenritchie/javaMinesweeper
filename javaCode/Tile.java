import javax.swing.JButton;



public class Tile extends JButton {
     
     private int r, c, value;
     public Tile (int r, int c) {
         
         value = 0;
         
         this.r = r;
         this.c = c;
     }
     public int getRow () {
         return r;
     }
     public int getColumn () {
         return c;
     }
      public int getValue() {
        return value;
     }
     public void setValue(int value) {
        this.value = value;
     }
}