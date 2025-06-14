package view;

import DAO.PostagemDAO;
import DAO.VideoAulaDAO;
import DAO.ModuloDAO;
import model.Modulo;
import model.Postagem;
import model.VideoAula;
import model.Usuario;
import model.Tutor;
import model.Curso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.sql.SQLException;

public class ModulePanel extends JPanel {

    private Modulo modulo;
    private VideoAulaDAO videoAulaDAO = new VideoAulaDAO();
    private PostagemDAO postagemDAO = new PostagemDAO();
    private ModuloDAO moduloDAO = new ModuloDAO();

    private CursoDetalhesPanelAluno parentDetailsPanel; 
    private Usuario loggedInUser; 

    private JLabel titleLabel;
    private JTextArea descriptionArea;
    private JPanel contentPanel;
    private boolean isExpanded = false;

    private JPanel headerPanel;
    private JButton editModuleButton;
    private JButton deleteModuleButton;

    public ModulePanel(Modulo modulo, CursoDetalhesPanelAluno parentDetailsPanel, Usuario loggedInUser) {
        this.modulo = modulo;
        this.parentDetailsPanel = parentDetailsPanel;
        this.loggedInUser = loggedInUser;

        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(65, 105, 225));
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(25, 25, 112), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackground());
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = new JLabel(modulo.getTitulo() + " ▶");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel titleAndButtonsPanel = new JPanel(new BorderLayout());
        titleAndButtonsPanel.setBackground(getBackground());
        titleAndButtonsPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel moduleActionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        moduleActionButtonsPanel.setBackground(getBackground());

        editModuleButton = new JButton("Editar");
        editModuleButton.setFont(new Font("Arial", Font.BOLD, 11));
        editModuleButton.setBackground(new Color(255, 165, 0)); 
        editModuleButton.setForeground(Color.WHITE);
        editModuleButton.setFocusPainted(false);
        editModuleButton.setPreferredSize(new Dimension(70, 25));
        editModuleButton.addActionListener(e -> navigateToEditModule());
        moduleActionButtonsPanel.add(editModuleButton);

        deleteModuleButton = new JButton("Excluir");
        deleteModuleButton.setFont(new Font("Arial", Font.BOLD, 11));
        deleteModuleButton.setBackground(new Color(220, 20, 60)); 
        deleteModuleButton.setForeground(Color.WHITE);
        deleteModuleButton.setFocusPainted(false);
        deleteModuleButton.setPreferredSize(new Dimension(70, 25));
        deleteModuleButton.addActionListener(e -> deleteCurrentModule());
        moduleActionButtonsPanel.add(deleteModuleButton);

        titleAndButtonsPanel.add(moduleActionButtonsPanel, BorderLayout.EAST);

        headerPanel.add(titleAndButtonsPanel, BorderLayout.NORTH);

        descriptionArea = new JTextArea(modulo.getDescricao());
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            
        headerPanel.add(descriptionArea, BorderLayout.CENTER);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 248, 255));
        contentPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
        contentPanel.setVisible(false); 
            
        add(contentPanel, BorderLayout.CENTER);

        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (!(e.getSource() instanceof JButton) && !(e.getComponent() instanceof JButton)) { 
                    toggleExpansion();
                }
            }
        });
            
        loadModuleContent();
        updateModuleSize();
        
        updateModuleButtonVisibility();
    }

    private void updateModuleButtonVisibility() {
        boolean isTutorOwnerOfCourse = false;
       
        if (loggedInUser instanceof Tutor && parentDetailsPanel instanceof CursoDetalhesPanelTutor && modulo != null && modulo.getCurso() != null && modulo.getCurso().getTutor() != null) {
            isTutorOwnerOfCourse = (modulo.getCurso().getTutor().getId() == ((Tutor) loggedInUser).getId());
        }
        editModuleButton.setVisible(isTutorOwnerOfCourse);
        deleteModuleButton.setVisible(isTutorOwnerOfCourse);
    }

    private void toggleExpansion() {
        isExpanded = !isExpanded;
        contentPanel.setVisible(isExpanded);
        titleLabel.setText(modulo.getTitulo() + (isExpanded ? " ▼" : " ▶"));
        updateModuleSize();
    }

    private void updateModuleSize() {
        headerPanel.revalidate();
        headerPanel.repaint();
        contentPanel.revalidate();
        contentPanel.repaint();

        int preferredHeight = 0;
        preferredHeight += headerPanel.getPreferredSize().height; 
        
        if (isExpanded) {
            preferredHeight += contentPanel.getPreferredSize().height;
            preferredHeight += 5; 
            preferredHeight += getBorder().getBorderInsets(this).top + getBorder().getBorderInsets(this).bottom;
        } else {
            preferredHeight += getBorder().getBorderInsets(this).top + getBorder().getBorderInsets(this).bottom;
        }
        
        setPreferredSize(new Dimension(getPreferredSize().width, preferredHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));

        Window ancestorWindow = SwingUtilities.getWindowAncestor(this);
        if (ancestorWindow != null) {
            ancestorWindow.revalidate();
            ancestorWindow.repaint();
        }
    }

    private void loadModuleContent() {
        contentPanel.removeAll();    

        contentPanel.add(new JLabel(" "));    
        JLabel videoHeader = new JLabel("Videoaulas:");
        videoHeader.setFont(new Font("Arial", Font.BOLD, 14));
        videoHeader.setForeground(new Color(0, 100, 0));    
        videoHeader.setAlignmentX(Component.LEFT_ALIGNMENT);    
        contentPanel.add(videoHeader);

        List<VideoAula> videoAulas = videoAulaDAO.listarPorModulo(modulo.getId());
        if (videoAulas.isEmpty()) {
            JLabel noVideosLabel = new JLabel("  - Nenhuma videoaula disponível.");
            noVideosLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(noVideosLabel);
        } else {
            for (VideoAula va : videoAulas) {
                JLabel videoLabel = new JLabel("  - " + va.getTitulo() + " (URL: " + va.getUrl() + ")");
                videoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(videoLabel);
            }
        }

        contentPanel.add(new JLabel(" "));    
        JLabel postHeader = new JLabel("Postagens:");
        postHeader.setFont(new Font("Arial", Font.BOLD, 14));
        postHeader.setForeground(new Color(0, 100, 0));    
        postHeader.setAlignmentX(Component.LEFT_ALIGNMENT);    
        contentPanel.add(postHeader);

        List<Postagem> postagens = postagemDAO.listarPorModulo(modulo.getId());
        if (postagens.isEmpty()) {
            JLabel noPostsLabel = new JLabel("  - Nenhuma postagem disponível.");
            noPostsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(noPostsLabel);
        } else {
            for (Postagem p : postagens) {
                JLabel postTitleLabel = new JLabel("  - " + p.getTitulo());
                postTitleLabel.setFont(new Font("Arial", Font.BOLD, 12));
                postTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(postTitleLabel);

                JTextArea postContent = new JTextArea("    " + p.getConteudo());    
                postContent.setFont(new Font("Arial", Font.PLAIN, 12));
                postContent.setLineWrap(true);
                postContent.setWrapStyleWord(true);
                postContent.setEditable(false);
                postContent.setBackground(contentPanel.getBackground());
                postContent.setForeground(Color.BLACK);
                postContent.setAlignmentX(Component.LEFT_ALIGNMENT);    
                    
                contentPanel.add(postContent);    
            }
        }
        contentPanel.add(new JLabel(" "));    
    }

    private void navigateToEditModule() {
        if (parentDetailsPanel instanceof CursoDetalhesPanelTutor) {
            CursoDetalhesPanelTutor tutorDetailsPanel = (CursoDetalhesPanelTutor) parentDetailsPanel;
            if (tutorDetailsPanel.tutorPanelCallback != null && modulo != null) {
                tutorDetailsPanel.tutorPanelCallback.navigateToManageModulesAndSelect(modulo);
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Não é possível editar o módulo (referência ao TutorPanel ausente).", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro: Funcionalidade de edição de módulo indisponível neste contexto (não é painel de tutor).", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCurrentModule() {
        if (parentDetailsPanel instanceof CursoDetalhesPanelTutor) {
            CursoDetalhesPanelTutor tutorDetailsPanel = (CursoDetalhesPanelTutor) parentDetailsPanel;
            if (tutorDetailsPanel.tutorPanelCallback != null && modulo != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir o módulo '" + modulo.getTitulo() + "' e todo o seu conteúdo (videoaulas e postagens)?",
                        "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        moduloDAO.deletar(modulo.getId());
                        JOptionPane.showMessageDialog(this, "Módulo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        parentDetailsPanel.moduleActionCompleted(); 
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir módulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Não é possível excluir o módulo (referência ao TutorPanel ausente).", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro: Funcionalidade de exclusão de módulo indisponível neste contexto (não é painel de tutor).", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}