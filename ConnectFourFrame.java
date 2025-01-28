import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConnectFourFrame extends JFrame{

    public ConnectFourFrame() throws IOException {
         //super("Wumpus Frame");
         setLayout(null);
         setSize(515,635);
         JPanel panel = new WumpusPanel();
         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         add(panel);
         setResizable(false);
         setVisible(true);

    }
}
