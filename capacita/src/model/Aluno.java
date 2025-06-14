package model;

import static main.CapacitaTerminal.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.SecurityUtils;

public class Aluno extends Usuario {

    public static void exibirMenuAluno() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Menu do Aluno (" + loggedInUser.getNome() + ") ---");
            System.out.println("1. Visualizar Cursos Disponíveis");
            System.out.println("2. Se Inscrever em Curso");
            System.out.println("3. Acessar Meus Materiais");
            System.out.println("4. Editar Perfil");
            System.out.println("0. Logout");
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
                    visualizarCursosDisponiveis();
                    break;
                case 2:
                    inscreverEmCurso();
                    break;
                case 3:
                    acessarMaterial();
                    break;
                case 4:
                    editarPerfilAluno();
                    break;
                case 0:
                    loggedInUser = null; 
                    System.out.println("Logout realizado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

 
    public static void visualizarCursosDisponiveis() {
    	
    	
        System.out.println("\n--- Cursos Disponíveis ---");
        List<Curso> cursos = cursoDAO.listar();
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso disponível no momento.");
            return;
        }
        for (Curso curso : cursos) {
        	System.out.println("- - - - - - - - - - - - - ");
            System.out.println("ID: " + curso.getId() + ", Título: " + curso.getTitulo());
            System.out.println("Descrição: " + curso.getDescricao());
            System.out.println("Área: " + curso.getArea());
        }
        System.out.println("- - - - - - - - - - - - - ");
    }

	public static void inscreverEmCurso() {
        if (!(loggedInUser instanceof Aluno)) {
            System.out.println("Apenas alunos podem se inscrever em cursos.");
            return;
        }
        
        visualizarCursosDisponiveis();
        System.out.print("Digite o ID do curso para se inscrever: ");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next();
            System.out.print("Digite o ID do curso para se inscrever: ");
        }
        int idCurso = scanner.nextInt();
        scanner.nextLine();

        try (Connection con = config.Conectante.createConnectionToMySQL()) {
            String checkSql = "SELECT COUNT(*) FROM UsuarioCurso WHERE id_usuario = ? AND id_curso = ?";
            try (PreparedStatement checkPstm = con.prepareStatement(checkSql)) {
                checkPstm.setInt(1, loggedInUser.getId());
                checkPstm.setInt(2, idCurso);
                try (ResultSet rs = checkPstm.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Você já está inscrito neste curso.");
                        return;
                    }
                }
            }


            String sql = "INSERT INTO UsuarioCurso (data_inscricao, estado, id_usuario, id_curso) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstm = con.prepareStatement(sql)) {
                pstm.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                pstm.setString(2, "ativo");
                pstm.setInt(3, loggedInUser.getId());
                pstm.setInt(4, idCurso);
                pstm.execute();
                System.out.println("Inscrição no curso realizada com sucesso!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao se inscrever no curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

	public static void acessarMaterial() {
        if (!(loggedInUser instanceof Aluno)) {
            System.out.println("Apenas alunos podem acessar materiais.");
            return;
        }

        System.out.println("\n--- Meus Cursos Inscritos ---");
        List<Curso> cursosInscritos = new ArrayList<>();
        try (Connection con = config.Conectante.createConnectionToMySQL()) {
            String sql = "SELECT c.id, c.titulo, c.descricao, c.area FROM Curso c JOIN UsuarioCurso uc ON c.id = uc.id_curso WHERE uc.id_usuario = ?";
            try (PreparedStatement pstm = con.prepareStatement(sql)) {
                pstm.setInt(1, loggedInUser.getId());
                try (ResultSet rset = pstm.executeQuery()) {
                    while (rset.next()) {
                        Curso curso = new Curso();
                        curso.setId(rset.getInt("id"));
                        curso.setTitulo(rset.getString("titulo"));
                        cursosInscritos.add(curso);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos inscritos: " + e.getMessage());
            return;
        }

        if (cursosInscritos.isEmpty()) {
            System.out.println("Você não está inscrito em nenhum curso.");
            return;
        }
        
        cursosInscritos.forEach(c -> System.out.println("ID: " + c.getId() + " - " + c.getTitulo()));
        System.out.print("Digite o ID do curso para acessar o material: ");
        int idCurso = scanner.nextInt();
        scanner.nextLine();
        
        if (cursosInscritos.stream().noneMatch(c -> c.getId() == idCurso)) {
            System.out.println("Você não tem permissão para acessar este curso.");
            return;
        }

        System.out.println("\n--- Material do Curso ---");
        List<Modulo> modulos = moduloDAO.listarPorCurso(idCurso);
        if (modulos.isEmpty()) {
            System.out.println("Nenhum módulo neste curso.");
        } else {
            modulos.forEach(m -> System.out.println("Módulo ID " + m.getId() + ": " + m.getTitulo()));
            System.out.print("Digite o ID do módulo para ver o conteúdo: ");
            int idModulo = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("\n--- Conteúdo do Módulo ---");
            System.out.println("Videoaulas:");
            videoAulaDAO.listarPorModulo(idModulo).forEach(va -> System.out.println(" - " + va.getTitulo() + " (" + va.getUrl() + ")"));
            
            System.out.println("\nPostagens:");
            postagemDAO.listarPorModulo(idModulo).forEach(p -> System.out.println(" - " + p.getTitulo() + ": " + p.getConteudo()));
        }
    }

 
	public static void editarPerfilAluno() {
        if (!(loggedInUser instanceof Aluno)) {
            System.out.println("Erro: Não há um aluno logado.");
            return;
        }
        Aluno aluno = (Aluno) loggedInUser;
        System.out.println("\n--- Editar Perfil do Aluno ---");
        
        System.out.print("Novo nome (atual: " + aluno.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            aluno.setNome(novoNome);
        }

        System.out.print("Novo email (atual: " + aluno.getEmail() + "): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.trim().isEmpty()) {
            aluno.setEmail(novoEmail);
        }

        System.out.print("Nova senha (deixe em branco para manter): ");
        String novaSenha = scanner.nextLine();
        if (!novaSenha.trim().isEmpty()) {
            aluno.setSenha(SecurityUtils.hashPassword(novaSenha));
        }

        alunoDAO.atualizar(aluno);
        System.out.println("Perfil atualizado com sucesso!");
    }
}