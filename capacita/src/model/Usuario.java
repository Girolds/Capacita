package model;

import static main.CapacitaTerminal.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.SecurityUtils;

public class Usuario {
	
	private int id;
	private String nome;
	private String email;
	private String senha;
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	public static void fazerLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

      
        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Email e senha não podem ser vazios.");
            return;
        }

        loggedInUser = null;
        try (Connection con = config.Conectante.createConnectionToMySQL()) {
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
                                System.out.println("Login de Aluno bem-sucedido!");
                            } else if ("tutor".equalsIgnoreCase(tipoUsuario)) {
                                Tutor tutor = new Tutor();
                                tutor.setId(rset.getInt("id"));
                                tutor.setNome(rset.getString("nome"));
                                tutor.setEmail(rset.getString("email"));
                                tutor.setSenha(storedHashedPassword);
                                tutor.setArea(rset.getString("area"));
                                loggedInUser = tutor;
                                System.out.println("Login de Tutor bem-sucedido!");
                            } else {
                                System.out.println("Tipo de usuário desconhecido no banco de dados.");
                            }
                        } else {
                            System.out.println("Email ou senha inválidos.");
                        }
                    } else {
                        System.out.println("Email ou senha inválidos.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar fazer login: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	public static void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            System.out.println("Nome, email e senha não podem ser vazios.");
            return;
        }

        System.out.print("Tipo de Usuário (aluno/tutor): ");
        String tipo = scanner.nextLine();

        if (tipo.equalsIgnoreCase("aluno")) {
            Aluno aluno = new Aluno();
            aluno.setNome(nome);
            aluno.setEmail(email);
            aluno.setSenha(SecurityUtils.hashPassword(senha)); 
            alunoDAO.criar(aluno);
        } else if (tipo.equalsIgnoreCase("tutor")) {
            System.out.print("Área de especialização do Tutor: ");
            String area = scanner.nextLine();
            if (area.trim().isEmpty()) {
                System.out.println("Área de especialização não pode ser vazia para tutores.");
                return;
            }
            Tutor tutor = new Tutor();
            tutor.setNome(nome);
            tutor.setEmail(email);
            tutor.setSenha(SecurityUtils.hashPassword(senha));
            tutor.setArea(area);
            tutorDAO.criar(tutor);
        } else {
            System.out.println("Tipo de usuário inválido. O cadastro falhou.");
        }
    }
}