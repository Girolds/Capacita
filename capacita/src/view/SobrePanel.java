package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder; 

public class SobrePanel extends JPanel {

    public SobrePanel() {
       
        setLayout(new BorderLayout(20, 20)); 
        setBackground(new Color(245, 250, 255)); 
        setBorder(new EmptyBorder(30, 30, 30, 30)); 

        
        Color royalBlue = Color.decode("#4169E1"); 
        Color darkGrayText = new Color(50, 50, 50); 
        Color mediumGrayText = new Color(80, 80, 80);

        
        JLabel titleLabel = new JLabel("Sobre o Capacita+", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); 
        titleLabel.setForeground(royalBlue); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); 
        add(titleLabel, BorderLayout.NORTH);

        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE); 
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(royalBlue, 3), 
                new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel descriptionTitle = new JLabel("Visão Geral do Sistema:", SwingConstants.LEFT);
        descriptionTitle.setFont(new Font("Arial", Font.BOLD, 19));
        descriptionTitle.setForeground(royalBlue);
        descriptionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(descriptionTitle);
        contentPanel.add(Box.createVerticalStrut(10)); 

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText(
            "O Capacita+ é uma plataforma online dedicada a capacitar professores e profissionais da educação " +
            "com foco total na inclusão escolar. Ele permite que tutores criem e gerenciem cursos, módulos, " +
            "videoaulas e materiais de apoio (postagens), enquanto os alunos podem se inscrever em cursos, " +
            "acessar o conteúdo e acompanhar seu progresso. Nosso objetivo é fornecer as ferramentas e o " +
            "conhecimento necessários para criar ambientes de aprendizado mais acessíveis e equitativos."
        );
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(contentPanel.getBackground());
        descriptionArea.setForeground(darkGrayText);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(descriptionArea);
        contentPanel.add(Box.createVerticalStrut(30)); 

        
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(30));

        
        JLabel techLabel = new JLabel("Tecnologias Utilizadas:", SwingConstants.LEFT);
        techLabel.setFont(new Font("Arial", Font.BOLD, 19));
        techLabel.setForeground(royalBlue);
        techLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(techLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        JTextArea techArea = new JTextArea();
        techArea.setText(
            "- Linguagem de Programação: Java\n" +
            "- Interface Gráfica: Swing (API padrão do Java para GUIs)\n" +
            "- Banco de Dados: MySQL, MySQL Workbench\n" +
            "- IDEs: Eclipse e VS Code"
        );
        techArea.setFont(new Font("Arial", Font.PLAIN, 15)); 
        techArea.setLineWrap(true);
        techArea.setWrapStyleWord(true);
        techArea.setEditable(false);
        techArea.setBackground(contentPanel.getBackground());
        techArea.setForeground(mediumGrayText); 
        techArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(techArea);
        contentPanel.add(Box.createVerticalStrut(30));


        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(30));

        
        JLabel teamLabelTitle = new JLabel("Sobre Nós:", SwingConstants.LEFT);
        teamLabelTitle.setFont(new Font("Arial", Font.BOLD, 19));
        teamLabelTitle.setForeground(royalBlue);
        teamLabelTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(teamLabelTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        JTextArea teamArea = new JTextArea();
        teamArea.setText(
            "Desenvolvedores:\n" +
            "  - Geraldo Rafael Lopes Benevides\n" +
            "  - Júlia Évelyn Magalhães dos Santos\n\n" +
            "Orientador:\n" +
            "  - Woquiton Lima Fernandes"
        );
        teamArea.setFont(new Font("Arial", Font.PLAIN, 16)); 
        teamArea.setLineWrap(true);
        teamArea.setWrapStyleWord(true);
        teamArea.setEditable(false);
        teamArea.setBackground(contentPanel.getBackground());
        teamArea.setForeground(darkGrayText);
        teamArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(teamArea);

        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }
}