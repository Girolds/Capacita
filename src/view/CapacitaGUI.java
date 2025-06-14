package view;

import model.Aluno;
import model.Tutor;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class CapacitaGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private CadastroPanel cadastroPanel;
    private AlunoPanel alunoPanel;
    private TutorPanel tutorPanel;

    private Usuario loggedInUser;

    public CapacitaGUI() {
        setTitle("Capacita+");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        loginPanel = new LoginPanel(this);
        cadastroPanel = new CadastroPanel(this);
        alunoPanel = new AlunoPanel(this); 
        try {
			tutorPanel = new TutorPanel(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(cadastroPanel, "Cadastro");
        mainPanel.add(alunoPanel, "AlunoMain");
        mainPanel.add(tutorPanel, "TutorMain");

        add(mainPanel);

        showLoginPanel(); 
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "Login");
    }

    public void showCadastroPanel() {
        cardLayout.show(mainPanel, "Cadastro");
    }

    public void setLoggedInUser(Usuario user) {
        this.loggedInUser = user;
    }

    public void showMainAppPanel() throws Exception {
        if (loggedInUser instanceof Aluno) {
            alunoPanel.setLoggedInAluno((Aluno) loggedInUser); 
            cardLayout.show(mainPanel, "AlunoMain");
        } else if (loggedInUser instanceof Tutor) {
            tutorPanel.setLoggedInTutor((Tutor) loggedInUser); 
            cardLayout.show(mainPanel, "TutorMain");
        } else { 
            JOptionPane.showMessageDialog(this, "Erro: Usuário não reconhecido.", "Erro", JOptionPane.ERROR_MESSAGE);
            showLoginPanel(); 
        }
    }

    public void logout() {
        setLoggedInUser(null);
        JOptionPane.showMessageDialog(this, "Logout realizado.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        showLoginPanel();
    }

    public static void main(String[] args) {
     
        SwingUtilities.invokeLater(() -> {
            new CapacitaGUI().setVisible(true);
        });
    }
}
