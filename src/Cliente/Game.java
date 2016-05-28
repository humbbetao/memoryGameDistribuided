package Cliente;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author humbe Game é a tela do jogo do cliente
 */
class Game extends JFrame {

    static String files[] = {"Number6.png", "Number7.png", "Number9.png",
        "Number11.png", "Number17.png", "Number23.png", "Number30.png", "Number37.png",
        "Number39.png"};

    JButton buttons[];
    ImageIcon closedIcon;
    int numButtons;
    ImageIcon icons[];
    Timer myTimer;
    boolean telaTravada;

    int numClicks = 0;
    int oddClickIndex = 0;
    int currentIndex = 0;
    int numeroDeClicks = 0;
    boolean imagensDiferentes;
    boolean setEnviouMensagem = false;

    Game(int numeroDeButtons, ImageIcon[] temp, boolean telaTravada, int numero) {
        this.numButtons = numeroDeButtons;
        this.icons = temp;
        this.numeroDeClicks = numero;
        this.telaTravada = telaTravada;

        setTitle("Jogo da Memória");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, files.length));

        this.closedIcon = new ImageIcon("Back-Anime-ZX-2.png");
        this.buttons = new JButton[numButtons];
        this.icons = new ImageIcon[numButtons];
        for (int i = 0, j = 0; i < numButtons / 2; i++) {

            icons[j] = new ImageIcon(files[i]);
            this.buttons[j] = new JButton("");
//            this.buttons[j].addActionListener(new ImageButtonListener());
            this.buttons[j].setIcon(closedIcon);
            if (telaTravada == false) {
                this.buttons[j].setEnabled(true);
            } else {
                this.buttons[j].setEnabled(false);
            }
            add(this.buttons[j++]);

            icons[j] = icons[j - 1];
            buttons[j] = new JButton("");
//            buttons[j].addActionListener(new ImageButtonListener());
            buttons[j].setIcon(closedIcon);
            if (telaTravada == true) {
                buttons[j].setEnabled(false);
            } else {
                buttons[j].setEnabled(true);
            }
            add(buttons[j++]);
        }
        pack();
        setVisible(true);
//        myTimer = new Timer(1000, new TimerListener());
//        myTimer.start();
        System.out.println("iniciou a tela");
    }
}
//
//    public class TimerListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent ae) {
////         public void actionPerformed(ActionEvent e) {
//            System.out.println(currentIndex);
//            buttons[currentIndex].setIcon(closedIcon);
//            buttons[oddClickIndex].setIcon(closedIcon);
//            myTimer.stop();
//        }
//    }
//
//    public class ImageButtonListener implements ActionListener {
//
//        public void actionPerformed(ActionEvent e) {
//
//                            numeroDeClicks++;
//            System.out.println("Clicado" + currentIndex + "e clicado em " + oddClickIndex);
//
//            // we are waiting for timer to pop - no user clicks accepted
//            if (myTimer.isRunning()) {
//                return;
//            }
//
////            numClicks++;
//            // we are waiting for timer to pop - no user clicks accepted
//            if (myTimer.isRunning()) {
//                return;
//            }
//
//            // which button was clicked?
//            for (int i = 0; i < numButtons; i++) {
//                if (e.getSource() == buttons[i]) {
//                    buttons[i].setIcon(icons[i]);
//                    currentIndex = i;
//                }
//            }
//            // check for even click
//            if (numClicks % 2 == 0) {
//                // check whether same position is clicked twice!
//                if (currentIndex == oddClickIndex) {
//                    numClicks--;
//                    return;
//                }
//                // are two images matching?
//                if (icons[currentIndex] != icons[oddClickIndex]) {
//                    // show images for 1 sec, before flipping back
//                    imagensDiferentes = true;
//                    myTimer.start();
//                }
//            } else {
//                // we just record index for odd clicks
//                oddClickIndex = currentIndex;
//            }
////            numeroDeClicks++;
//        }

