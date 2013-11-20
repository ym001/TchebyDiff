import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
public class graphique extends JFrame {
graphique() {
super("Un cadre sensible");
setDefaultCloseOperation(EXIT_ON_CLOSE);
JPanel panneau = new JPanel();
panneau.setBackground(Color.WHITE);
GuetteurSouris felix = new GuetteurSouris();
panneau.addMouseListener(felix);
getContentPane().add(panneau);
setSize(250, 150);
setVisible(true);
}
public static void main(String[] args) {
new graphique();
}
}
class GuetteurSouris implements MouseListener {
public void mousePressed(MouseEvent e) {
System.out.println("Clic en (" + e.getX() + ", " + e.getY() + ")");
}
public void mouseReleased(MouseEvent e) {
}
public void mouseClicked(MouseEvent e) {
}
public void mouseEntered(MouseEvent e) {
}
public void mouseExited(MouseEvent e) {
}
}

