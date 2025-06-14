package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.Conectante;
import model.Aluno; 
import model.Curso; 

public class AlunoDAO {

  
    public void criar(Aluno aluno) {
        String sql = "INSERT INTO Usuario (nome, email, senha, tipo_usuario) VALUES (?, ?, ?, 'aluno')";
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getEmail());
            pstm.setString(3, aluno.getSenha()); 

            pstm.execute();
            
            try (ResultSet rset = pstm.getGeneratedKeys()) {
                if (rset.next()) {
                    aluno.setId(rset.getInt(1)); 
                }
            }
            
            String alunoSql = "INSERT INTO Aluno (id_usuario) VALUES (?)";
            try (PreparedStatement alunoPstm = con.prepareStatement(alunoSql)) {
                alunoPstm.setInt(1, aluno.getId());
                alunoPstm.execute();
            }

            System.out.println("Aluno criado com sucesso!");
        
        } catch (SQLException e) {
            System.err.println("Erro SQL ao criar aluno: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao criar aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }

  
    public List<Aluno> listar() {
        String sql = "SELECT u.id, u.nome, u.email, u.senha FROM Usuario u JOIN Aluno a ON u.id = a.id_usuario";

        List<Aluno> alunos = new ArrayList<>();

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Aluno aluno = new Aluno();
                
                aluno.setId(rset.getInt("id"));
                aluno.setNome(rset.getString("nome"));
                aluno.setEmail(rset.getString("email"));
                aluno.setSenha(rset.getString("senha"));

                alunos.add(aluno);
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar alunos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar alunos: " + e.getMessage());
            e.printStackTrace();
        }
        return alunos;
    }

  
    public void atualizar(Aluno aluno) {
        String sql = "UPDATE Usuario SET nome = ?, email = ?, senha = ? WHERE id = ? AND tipo_usuario = 'aluno'";

        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getEmail());
            pstm.setString(3, aluno.getSenha()); 
            pstm.setInt(4, aluno.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno atualizado com sucesso!");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID especificado ou não é um aluno.");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar aluno: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar aluno: " + e.getMessage());
            e.printStackTrace();
        }
    }

  
    public void deletar(int id) {
        String deleteAlunoSql = "DELETE FROM Aluno WHERE id_usuario = ?";
        String deleteUsuarioSql = "DELETE FROM Usuario WHERE id = ? AND tipo_usuario = 'aluno'";

        Connection con = null; 
        try {
            con = Conectante.createConnectionToMySQL();
            con.setAutoCommit(false); 

            try (PreparedStatement pstmAluno = con.prepareStatement(deleteAlunoSql)) {
                pstmAluno.setInt(1, id);
                pstmAluno.execute();
            }

            try (PreparedStatement pstmUsuario = con.prepareStatement(deleteUsuarioSql)) {
                pstmUsuario.setInt(1, id);
                pstmUsuario.execute();
            }

            con.commit(); 
            System.out.println("Aluno deletado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro SQL ao deletar aluno: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar aluno.");
                    con.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar aluno: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Rollback da transação ao deletar aluno.");
                    con.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro ao realizar rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); 
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão no finally: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

  
    public void desinscreverCurso(int idAluno, int idCurso) {
        String sql = "DELETE FROM UsuarioCurso WHERE id_usuario = ? AND id_curso = ?";
        
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            
            pstm.setInt(1, idAluno);
            pstm.setInt(2, idCurso);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno desinscrito do curso com sucesso!");
            } else {
                System.out.println("Inscrição não encontrada para o aluno e curso especificados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao desinscrever aluno do curso: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao desinscrever aluno do curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

  
    public boolean isAlunoInscrito(int idAluno, int idCurso) {
        String sql = "SELECT COUNT(*) FROM UsuarioCurso WHERE id_usuario = ? AND id_curso = ?";
        try (Connection con = Conectante.createConnectionToMySQL();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, idAluno);
            pstm.setInt(2, idCurso);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao verificar inscrição do aluno: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao verificar inscrição do aluno: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}