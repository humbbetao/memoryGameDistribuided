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
    
    ObjectInputStream inObject;
    ObjectOutputStream outObject;
    
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
        enviouMensagem = true;
        initGame();
        this.start();
        
    }
    
    public void run() {
        MensagemDeInicioDeJogo mensagem;
        int currentIndex = -1;
        int oddIndex = -1;
        while (true) {
            if (modoDeJogo.equals("recebimento")) {
                MensagemDeEnvio mensagemDeRecebimentoDeClickEmUmaCarta = null;
                try {
                    mensagemDeRecebimentoDeClickEmUmaCarta = (MensagemDeEnvio) inObject.readObject();
                } catch (IOException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                int numeroDaCartaClickada = mensagemDeRecebimentoDeClickEmUmaCarta.getNumeroAberto();
                g.icons[numeroDaCartaClickada].setImage(iconsDaTela[numeroDaCartaClickada]);
            } else if (modoDeJogo.equals("envio")) {
                while (enviouMensagem != true) {
                    if (g.currentIndex != currentIndex) {
                        MensagemDeEnvio mensagemDeEnvioDeClickEmUmaCarta = new MensagemDeEnvio();
                        mensagemDeEnvioDeClickEmUmaCarta.setNumeroAberto(g.currentIndex);
                        currentIndex = g.currentIndex;
                        try {
                            outObject = new ObjectOutputStream(s.getOutputStream());
                        } catch (IOException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            outObject.writeObject(mensagemDeEnvioDeClickEmUmaCarta);
                        } catch (IOException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        enviouMensagem = false;
                    }
                    
                    if (g.oddClickIndex != oddIndex) {
                        MensagemDeEnvio mensagemDeEnvioDeClickEmUmaCarta = new MensagemDeEnvio();
                        mensagemDeEnvioDeClickEmUmaCarta.setNumeroAberto(g.oddClickIndex);
                        oddIndex = g.oddClickIndex;
                        try {
                            outObject = new ObjectOutputStream(s.getOutputStream());
                        } catch (IOException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            outObject.writeObject(mensagemDeEnvioDeClickEmUmaCarta);
                        } catch (IOException ex) {
                            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        enviouMensagem = false;
                    }
                }
            }
        }
    }
//
//            if (turno % numeroDoJogador == 0 && telaTravada == false) {
//                //se o numero de clicks esta desatualizado na thread;
//                //se o turno do jogador eh o dele
//                //se a tela esta destravada pra ele
//                //na vdd eh ambiguo mas vai
//                //mandar mensagem pros clientes
//                g.numeroDeClicks = numeroDeClicks;
//                if (currentIndex != g.currentIndex) {
//                    System.out.println("entrou na diferenca de clicks direito");
//                    currentIndex = g.currentIndex;
//                    try {
//                        MensagemDoJogo mNova = new MensagemDoJogo();
//                        mNova.setBotaoClicado1(currentIndex);
//                        mNova.setBotao(true);
//                        outObject.writeObject(mNova);
//                        outObject.flush();
//                        System.out.println("Enviou a mensagem de clicks");
//                    } catch (IOException ex) {
//                        System.out.println(ex.getMessage());
//                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                if (oddClickIndex != g.oddClickIndex) {
//                    System.out.println("entrou na diferenca de clicks esquerdo");
//                    oddClickIndex = g.oddClickIndex;
//                    mensagem.setBotaoClicado1(oddClickIndex);
//                    mensagem.setBotao(false);
//                    try {
//                        outObject.writeObject(mensagem);
//                        outObject.flush();
//                        System.out.println("Enviou a mensagem de clicks");
//                    } catch (IOException ex) {
//                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//            } else if (turno % numeroDoJogador != 0 && telaTravada == true) {
//                try {
//                    inObject = new ObjectInputStream(s.getInputStream());
//
//                } catch (IOException ex) {
//                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try {
//                    mensagem = (MensagemDoJogo) inObject.readObject();
//                } catch (IOException ex) {
//                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                boolean botao = mensagem.isBotao();
//                if (botao == true) {
//                    currentIndex = mensagem.getBotaoClicado1();
//                    openCard(currentIndex);
//                } else {
//                    oddClickIndex = mensagem.getBotaoClicado2();
//                    openCard(oddClickIndex);
//                }
//
//                System.out.println("recebeu a mensagem dos clicks");
////            }
//        }
//    }

    private void initGame() {
        g = new Game(numeroDeButtonNatela, iconsDaTela, telaTravada);
    }

//        
//        
//        MensagemDoJogo mensagemDoJogo = null;
//        System.out.println("initGame iniciado");
//        try {
//            inObject = new ObjectInputStream(s.getInputStream());
//            System.out.println("Aceitou a conexao");
//
//        } catch (IOException ex) {
//            Logger.getLogger(Connection.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//        turno++;
//        try {
//            mensagemDoJogo = (MensagemDoJogo) inObject.readObject();
//            System.out.println("leu a o objeto");
//            //mudar o codigo para um so object input e output; na classe toda;
//
//        } catch (IOException ex) {
//            Logger.getLogger(Connection.class
//                    .getName()).log(Level.SEVERE, null, ex);
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Connection.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.out.println("preparando");
//        int numeroDeButoes = mensagemDoJogo.getNumeroDeButtons();
//        ImageIcon[] temp = mensagemDoJogo.getTemp();
//        boolean t = mensagemDoJogo.isTelaTravada();
//        numeroDoJogador = mensagemDoJogo.getNumeroDoJogador();
//        System.out.println("Recebeu nas variavies");
//        g = new Game(numeroDeButoes, temp, t);
//        g.setVisible(true);
//        System.out.println("O jogo comecou");
//        inicia jogo;
//    }
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
