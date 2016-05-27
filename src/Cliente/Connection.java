/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Mensagens.Mensagem;
import Mensagens.MensagemDeEnvio;
import Mensagens.MensagemDeInicioDeJogo;
import Mensagens.MensagemDoJogo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author humberto COnnection: é a thread do lado do cliente
 */
class Connection extends Thread {

    ObjectInputStream inObject = null;
    ObjectOutputStream outObject = null;

    Socket s;
    int turno = 0;
    int eventosNaTela = 0;
    boolean telaTravada;
    Game g;
    int numClicks = 0;
    int oddClickIndex = 0;
    int currentIndex = 0;
    boolean f;
    int numeroDeClicks = 0;
    int numeroDoJogador;
    String inicioDeJogo = "InicioDeJogo";
    String fimDeJogo = "FimDeJogo";
    String meioDoJogo = "MeioDeJogo";
    String envio = "Envio";
    String recebimento = "Recebimento";
    String modoDeJogo;
    ImageIcon[] iconsDaTela;
    int numeroDeButtonNatela;
    boolean enviouMensagem;

    public Connection() {
        this.start();
    }

    Connection(Socket s, MensagemDeInicioDeJogo mensagemDeInicioDeJogo) {
        this.s = s;

        System.out.println("DENTRO DA THREAD");
        this.modoDeJogo = mensagemDeInicioDeJogo.getModoDeJogo();
        this.telaTravada = mensagemDeInicioDeJogo.isTelaTravada();
        this.turno = mensagemDeInicioDeJogo.getTurno();
        this.iconsDaTela = mensagemDeInicioDeJogo.getTemp();
        this.numeroDeButtonNatela = mensagemDeInicioDeJogo.getNumeroDeButtonNatela();
        numClicks = 0;

        enviouMensagem = true;
        System.out.println("ModoDoJogo: " + modoDeJogo);
        initGame();
        this.start();

    }

    public void run() {
        MensagemDeInicioDeJogo mensagem;
        int currentIndex = 0;
        int oddIndex = 0;
        while (true) {
            if (modoDeJogo.equals(recebimento)) {
                MensagemDeEnvio mensagemDeRecebimentoDeClickEmUmaCarta = new MensagemDeEnvio();
                if (inObject == null) {
                    try {
                        inObject = new ObjectInputStream(s.getInputStream());
                    } catch (IOException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    mensagemDeRecebimentoDeClickEmUmaCarta = (MensagemDeEnvio) inObject.readObject();
                    System.out.println("Leu a mensagem de Recebimento ");
                } catch (IOException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                int numeroDaCartaClickada = mensagemDeRecebimentoDeClickEmUmaCarta.getNumeroAberto();
                g.buttons[numeroDaCartaClickada].setEnabled(true);
//                g.icons[numeroDaCartaClickada] = (iconsDaTela[numeroDaCartaClickada]);
                
            } else if (modoDeJogo.equals(envio)) {
                System.out.println("numero de Clicks" + g.numeroDeClicks);
                System.out.println("numero de clikcs da conection " + numClicks);
                if (g.numeroDeClicks != numClicks) {

//                    if (g.currentIndex != currentIndex) {
                    numClicks = g.numeroDeClicks;
                    MensagemDeEnvio mensagemDeEnvioDeClickEmUmaCarta = new MensagemDeEnvio();
                    mensagemDeEnvioDeClickEmUmaCarta.setNumeroAberto(g.currentIndex);

                    currentIndex = g.currentIndex;
                    if (outObject == null) {
                        try {
                            outObject = new ObjectOutputStream(s.getOutputStream());
                        } catch (IOException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        outObject.writeObject(mensagemDeEnvioDeClickEmUmaCarta);
                        outObject.flush();
                        System.out.println("Envio a Mensagem  de Envio " + mensagemDeEnvioDeClickEmUmaCarta.toString());
                    } catch (IOException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    enviouMensagem = false;
                }
            } else {
                System.out.println("FOdasse");
            }
//
//                if (g.oddClickIndex != oddIndex) {
//                    MensagemDeEnvio mensagemDeEnvioDeClickEmUmaCarta = new MensagemDeEnvio();
//                    mensagemDeEnvioDeClickEmUmaCarta.setNumeroAberto(g.oddClickIndex);
//                    oddIndex = g.oddClickIndex;
//                    try {
//                        outObject = new ObjectOutputStream(s.getOutputStream());
//                    } catch (IOException ex) {
//                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    try {
//                        outObject.writeObject(mensagemDeEnvioDeClickEmUmaCarta);
//                        System.out.println("Envio a Mensagem  de Envio " + mensagemDeEnvioDeClickEmUmaCarta.toString());
//                    } catch (IOException ex) {
//                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    enviouMensagem = false;
//                }
//            }
        }
    }

    private void initGame() {
        numeroDeClicks = 0;
        g = new Game(numeroDeButtonNatela, iconsDaTela, telaTravada, numeroDeClicks);
    }

    private void openCard(int botaoClicado) {
        g.buttons[botaoClicado].setIcon(g.icons[botaoClicado]);
        if (currentIndex == oddClickIndex) {
            numClicks--;
            return;
        }
        if (g.icons[currentIndex] != g.icons[oddClickIndex]) {
            g.myTimer.start();
        }
    }

}
