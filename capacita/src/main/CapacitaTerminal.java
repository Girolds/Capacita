package main;

import DAO.AlunoDAO;
import DAO.CursoDAO;
import DAO.ModuloDAO;
import DAO.PostagemDAO;
import DAO.TutorDAO;
import DAO.VideoAulaDAO;
import java.util.Scanner;
import model.Aluno;
import model.Tutor;
import model.Usuario;

public class CapacitaTerminal {
	
	  public static Scanner scanner = new Scanner(System.in);
	    public static Usuario loggedInUser = null;
	    
	    public static final AlunoDAO alunoDAO = new AlunoDAO();
	    public static final TutorDAO tutorDAO = new TutorDAO();
	    public static final CursoDAO cursoDAO = new CursoDAO();
	    public static final ModuloDAO moduloDAO = new ModuloDAO();
	    public static final PostagemDAO postagemDAO = new PostagemDAO();
	    public static final VideoAulaDAO videoAulaDAO = new VideoAulaDAO();

	 public static void exibirMenuPrincipal() {
	        int opcao = -1;
	        
	        while (opcao != 0) {
	            System.out.println("\n--- Bem-vindo ao Sistema de Cursos ---");
	            System.out.println("1. Cadastrar Usuário");
	            System.out.println("2. Fazer Login");
	            System.out.println("0. Sair");
	            System.out.print("Escolha uma opção: ");
	            
	          
	            while (!scanner.hasNextInt()) {
	                System.out.println("Entrada inválida. Por favor, digite um número.");
	                scanner.next(); 
	                System.out.print("Escolha uma opção: ");
	            }
	            opcao = scanner.nextInt();
	            scanner.nextLine(); 

	            switch (opcao) {
	                case 1:
	                
	                    Usuario.cadastrarUsuario();
	                    break;
	                case 2:
	                
	                    Usuario.fazerLogin();
	                    
	                    if (loggedInUser != null) {
	                        if (loggedInUser instanceof Aluno) {
	                            Aluno.exibirMenuAluno();
	                        } else if (loggedInUser instanceof Tutor) {
	                            Tutor.exibirMenuTutor();
	                        }
	                    }
	                    break;
	                case 0:
	                    System.out.println("Saindo do sistema...");
	                    break;
	                default:
	                    System.out.println("Opção inválida. Tente novamente.");
	            }
	        }
	    }
   
	 public static void main(String[] args) {
        exibirMenuPrincipal();
       
        scanner.close(); 
    }
}