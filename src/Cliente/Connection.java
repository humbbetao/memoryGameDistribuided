/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import static Cliente.Game.files;
import Mensagens.MensagemDeEnvio;
import Mensagens.MensagemDeFimDeJogo;
import Mensagens.MensagemDeInicioDeJogo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author humberto COnnection: é a thread do lado do cliente
 */
class Connection extends Thread {

    ObjectInputStream inObject = null;
    ObjectOutputStream outObject = null;
    ArrayList<Integer> cartasAbertas = new ArrayList<>();
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
    boolean leu = false;
    boolean escreveu = false;

    public Connection() {
        this.start();
    }

    Connection(Socket s, MensagemDeInicioDeJogo mensagemDeInicioDeJogo) {
        this.s = s;
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
        while (true) {
            if (modoDeJogo.equals(recebimento)) {
                recebimentoDeDados();
                System.out.println("RecebeuODado");
            } else if (modoDeJogo.equals(envio)) {
//                if()
                envioDeDados();
//                System.out.println("EnviouODado");
            } else {
                System.out.println("TerminouOJogo");
            }
        }
    }

    private void initGame() {
        numeroDeClicks = 0;
        g = new Game(numeroDeButtonNatela, iconsDaTela, telaTravada, numeroDeClicks);
    }

    private void recebimentoDeDados() {
        MensagemDeEnvio mensagemDeRecebimentoDeClickEmUmaCarta = new MensagemDeEnvio();
        if (inObject == null) {
            try {
                inObject = new ObjectInputStream(s.getInputStream());

            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MensagemDeFimDeJogo m1 = null;
        try {
            mensagemDeRecebimentoDeClickEmUmaCarta = (MensagemDeEnvio) inObject.readObject();
            System.out.println("Leu a mensagem de Recebimento ");
            System.out.println(mensagemDeRecebimentoDeClickEmUmaCarta.toString());
        } catch (ClassCastException ex) {

            try {
                m1 = (MensagemDeFimDeJogo) inObject.readObject();
                System.out.println(m1.toString());
            } catch (IOException ex1) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (ClassNotFoundException ex1) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex1);
            }

            if (m1.isVencedor() == true) {
                JOptionPane.showMessageDialog(g, "Você Venceu");
            } else {
                JOptionPane.showMessageDialog(g, "Você perdeu");
            }
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
//        if (mensagemDeRecebimentoDeClickEmUmaCarta != null) {
        int numeroDaCartaClickada = mensagemDeRecebimentoDeClickEmUmaCarta.getNumeroAberto();
        g.buttons[numeroDaCartaClickada].setIcon(g.icons[numeroDaCartaClickada]);
        g.buttons[numeroDaCartaClickada].setEnabled(true);
        cartasAbertas.add(numeroDaCartaClickada);
//            mensagemDeRecebimentoDeClickEmUmaCarta = null;

//        }
    }

    private void envioDeDados() {
        System.out.println("Numero de clicks da thread" + numClicks);
        System.out.println("Numerode clicks da tela" + g.numeroDeClicks);
        MensagemDeEnvio mensagemDeEnvioDeClickEmUmaCarta = null;
        if (g.numeroDeClicks != numClicks && escreveu == false) {
            System.out.println("eh diferente");
            numClicks = g.numeroDeClicks;
            mensagemDeEnvioDeClickEmUmaCarta = new MensagemDeEnvio();
            mensagemDeEnvioDeClickEmUmaCarta.setNumeroAberto(g.imageClicada);
            System.out.println("o numero de clciks eh " + g.imageClicada);
//            currentIndex = g.currentIndex;

            try {
                outObject = new ObjectOutputStream(s.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                System.out.println(mensagemDeEnvioDeClickEmUmaCarta.toString());
//               
                outObject.writeObject(mensagemDeEnvioDeClickEmUmaCarta);
                outObject.flush();
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            escreveu = true;
        } else {
            System.out.println("eh igual");
        }

    }
}
