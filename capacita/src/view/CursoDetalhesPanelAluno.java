package view;

import DAO.ModuloDAO;
import DAO.PostagemDAO;
import DAO.VideoAulaDAO;
import DAO.CursoDAO; 
import model.Curso;
import model.Modulo;
import model.Postagem;
import model.VideoAula;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder; 
import java.awt.*;
import java.util.List;

public class CursoDetalhesPanelAluno extends JPanel {    

    protected Curso curso;
    protected Usuario loggedInUser; 

    protected final CursoDAO cursoDAO = new CursoDAO(); 
    protected final ModuloDAO moduloDAO = new ModuloDAO();
    protected final PostagemDAO postagemDAO = new PostagemDAO();
    protected final VideoAulaDAO videoAulaDAO = new VideoAulaDAO();

    protected JLabel courseTitleLabel;
    protected JLabel courseTutorLabel;
    protected JTextArea courseDescriptionArea;
    protected JLabel courseAreaLabel;
    protected JPanel modulesContainerPanel;    

    protected JPanel mainContentPanel; 
    
    protected JPanel coursePostsContainerPanel; 

    public CursoDetalhesPanelAluno() { 
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        mainContentPanel = new JPanel(); 
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(240, 248, 255));
        mainContentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(65, 105, 225));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(25, 25, 112), 2),
                new EmptyBorder(10, 15, 10, 15)
        ));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));

        courseTitleLabel = new JLabel("Título do Curso");
        courseTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        courseTitleLabel.setForeground(Color.WHITE);
        courseTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        courseAreaLabel = new JLabel("Área: ");
        courseAreaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseAreaLabel.setForeground(Color.WHITE);
        courseAreaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        courseTutorLabel = new JLabel("Ministrado por: ");
        courseTutorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseTutorLabel.setForeground(Color.WHITE);
        courseTutorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(courseTitleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(courseAreaLabel);
        infoPanel.add(courseTutorLabel);
        mainContentPanel.add(infoPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel descriptionHeaderLabel = new JLabel("Descrição do Curso:");
        descriptionHeaderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descriptionHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(descriptionHeaderLabel);

        courseDescriptionArea = new JTextArea(5, 40);
        courseDescriptionArea.setEditable(false);
        courseDescriptionArea.setLineWrap(true);
        courseDescriptionArea.setWrapStyleWord(true);
        courseDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane descriptionScrollPane = new JScrollPane(courseDescriptionArea);
        descriptionScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionScrollPane.setPreferredSize(new Dimension(mainContentPanel.getWidth(), 100));
        descriptionScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        mainContentPanel.add(descriptionScrollPane);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        coursePostsContainerPanel = new JPanel();
        coursePostsContainerPanel.setLayout(new BoxLayout(coursePostsContainerPanel, BoxLayout.Y_AXIS));
        coursePostsContainerPanel.setBackground(new Color(245, 248, 255));
        coursePostsContainerPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(65, 105, 225), 2), 
                "Postagens do Curso", 
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), 
                new Color(30, 144, 255) 
            )
        ));
        coursePostsContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(coursePostsContainerPanel); 
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20))); 

        JLabel modulesHeaderLabel = new JLabel("Módulos:");
        modulesHeaderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        modulesHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(modulesHeaderLabel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        modulesContainerPanel = new JPanel();
        modulesContainerPanel.setLayout(new BoxLayout(modulesContainerPanel, BoxLayout.Y_AXIS));
        modulesContainerPanel.setBackground(mainContentPanel.getBackground());
        modulesContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(modulesContainerPanel);

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    public void setLoggedInUser(Usuario user) {
        this.loggedInUser = user;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCourse(Curso curso) {
        this.curso = curso;
        if (curso != null) {
            courseTitleLabel.setText(curso.getTitulo());
            courseDescriptionArea.setText(curso.getDescricao());
            courseAreaLabel.setText("Área: " + curso.getArea());
            if (curso.getTutor() != null && curso.getTutor().getNome() != null) {
                courseTutorLabel.setText("Ministrado por: " + curso.getTutor().getNome());
            } else {
                courseTutorLabel.setText("Ministrado por: (Tutor não especificado)");
            }
            loadCoursePosts(); 
            loadModules();     
        } else {
            courseTitleLabel.setText("Detalhes do Curso");
            courseDescriptionArea.setText("");
            courseAreaLabel.setText("");
            courseTutorLabel.setText("Ministrado por:");
            modulesContainerPanel.removeAll();
            coursePostsContainerPanel.removeAll(); 
            revalidate();
            repaint();
        }
    }

    protected void loadModules() {
        modulesContainerPanel.removeAll();
        if (this.curso != null) {
            List<Modulo> modulos = moduloDAO.listarPorCurso(this.curso.getId());
            if (modulos.isEmpty()) {
                JLabel noModulesLabel = new JLabel("Nenhum módulo disponível para este curso.");
                noModulesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                modulesContainerPanel.add(noModulesLabel);
            } else {
                for (Modulo mod : modulos) {
                    modulesContainerPanel.add(new ModulePanel(mod, this, loggedInUser));
                    modulesContainerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        revalidate();
        repaint();
    }

   
    protected void loadCoursePosts() {
        coursePostsContainerPanel.removeAll();

        if (this.curso != null) {
            List<Postagem> coursePosts = postagemDAO.listarPorCursoSemModulo(this.curso.getId());
            if (coursePosts.isEmpty()) {
                JLabel noCoursePostsLabel = new JLabel("Nenhuma postagem geral disponível para este curso.");
                noCoursePostsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                coursePostsContainerPanel.add(noCoursePostsLabel);
            } else {
                for (Postagem post : coursePosts) {
                    JLabel postTitleLabel = new JLabel("  - " + post.getTitulo());
                    postTitleLabel.setFont(new Font("Arial", Font.BOLD, 12));
                    postTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    coursePostsContainerPanel.add(postTitleLabel);

                    JTextArea postContent = new JTextArea("    " + post.getConteudo());
                    postContent.setFont(new Font("Arial", Font.PLAIN, 12));
                    postContent.setLineWrap(true);
                    postContent.setWrapStyleWord(true);
                    postContent.setEditable(false);
                    postContent.setBackground(coursePostsContainerPanel.getBackground());
                    postContent.setForeground(Color.BLACK);
                    postContent.setAlignmentX(Component.LEFT_ALIGNMENT);
                    coursePostsContainerPanel.add(postContent);
                    coursePostsContainerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
        }
        coursePostsContainerPanel.revalidate();
        coursePostsContainerPanel.repaint();
    }

    public void moduleActionCompleted() {
        loadModules(); 
        loadCoursePosts(); 
        revalidate();
        repaint();
    }
}