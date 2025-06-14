package view;

import model.Aluno;
import model.Tutor;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import config.Conectante;
import main.CapacitaGUI;

import java.awt.*;
import utils.SecurityUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton goToRegisterButton;
    private CapacitaGUI parentFrame;

    public LoginPanel(CapacitaGUI parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new GridBagLayout()); 
        setBackground(new Color(240, 248, 255)); 


        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBackground(Color.WHITE);
        loginFormPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(150, 150, 150), 1),
                new EmptyBorder(30, 40, 30, 40) 
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

       
        JLabel titleLabel = new JLabel("Login no Capacita");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32)); 
        titleLabel.setForeground(new Color(65, 105, 225));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginFormPanel.add(titleLabel, gbc);


        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 15, 0); 
        loginFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);


        Dimension fieldSize = new Dimension(280, 35); 
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);


        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        loginFormPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setPreferredSize(fieldSize);
        emailField.setMaximumSize(fieldSize);
        emailField.setFont(fieldFont);
        loginFormPanel.add(emailField, gbc);


        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(labelFont);
        loginFormPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setFont(fieldFont);
        loginFormPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 10, 0); 
        loginFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);


        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(65, 105, 225)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(150, 45)); 
        loginButton.setMaximumSize(new Dimension(150, 45));
        loginButton.addActionListener(e -> performLogin());
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; 
        loginFormPanel.add(loginButton, gbc);


        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 0); 
        loginFormPanel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);

        goToRegisterButton = new JButton("Não tem conta? Cadastre-se");
        goToRegisterButton.setFont(new Font("Arial", Font.PLAIN, 13)); 
        goToRegisterButton.setForeground(new Color(65, 105, 225)); 
        goToRegisterButton.setBorderPainted(false); 
        goToRegisterButton.setFocusPainted(false); 
        goToRegisterButton.setContentAreaFilled(false); 
        goToRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        goToRegisterButton.addActionListener(e -> parentFrame.showCadastroPanel());
        gbc.gridy++;
        loginFormPanel.add(goToRegisterButton, gbc);


        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.weightx = 1.0;
        outerGbc.weighty = 1.0;
        outerGbc.fill = GridBagConstraints.NONE; 
        add(loginFormPanel, outerGbc);
    }

    private void performLogin() {
        String email = emailField.getText();
        String senha = new String(passwordField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email e senha não podem ser vazios.", "Erro de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario loggedInUser = null;
        try (Connection con = Conectante.createConnectionToMySQL()) {
            String sql = "SELECT id, nome, email, senha, tipo_usuario, area FROM Usuario WHERE email = ?";
            try (PreparedStatement pstm = con.prepareStatement(sql)) {
                pstm.setString(1, email);
                try (ResultSet rset = pstm.executeQuery()) {
                    if (rset.next()) {
                        String storedHashedPassword = rset.getString("senha");

                        if (SecurityUtils.verifyPassword(senha, storedHashedPassword)) {
                            String tipoUsuario = rset.getString("tipo_usuario");

                            if ("aluno".equalsIgnoreCase(tipoUsuario)) {
                                Aluno aluno = new Aluno();
                                aluno.setId(rset.getInt("id"));
                                aluno.setNome(rset.getString("nome"));
                                aluno.setEmail(rset.getString("email"));
                                aluno.setSenha(storedHashedPassword);
                                loggedInUser = aluno;
                            } else if ("tutor".equalsIgnoreCase(tipoUsuario)) {
                                Tutor tutor = new Tutor();
                                tutor.setId(rset.getInt("id"));
                                tutor.setNome(rset.getString("nome"));
                                tutor.setEmail(rset.getString("email"));
                                tutor.setSenha(storedHashedPassword);
                                tutor.setArea(rset.getString("area"));
                                loggedInUser = tutor;
                            } else {
                                JOptionPane.showMessageDialog(this, "Tipo de usuário desconhecido.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            parentFrame.setLoggedInUser(loggedInUser);
                            parentFrame.showMainAppPanel();
                        } else {
                            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}