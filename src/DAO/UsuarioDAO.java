package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.Conectante;
import model.Usuario;

public class UsuarioDAO {

	public void criar(Usuario usuario) {

		String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			
			con = Conectante.createConnectionToMySQL();
					
			pstm = con.prepareStatement(sql);
			
			pstm.setString(1, usuario.getNome());
			pstm.setString(2, usuario.getEmail());
			pstm.setString(3, usuario.getSenha());

			pstm.execute();
			
			System.out.println("Usuario criado");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<Usuario> listarUsuarios() {

		String sql = "SELECT * FROM usuario";

		List<Usuario> usuarios = new ArrayList<Usuario>();

		Connection con = null;
		PreparedStatement pstm = null;

		ResultSet rset = null;

		try {
			con = Conectante.createConnectionToMySQL();
			pstm = con.prepareStatement(sql);
			rset = pstm.executeQuery();

			while (rset.next()) {

				Usuario usuario = new Usuario();

				usuario.setId(rset.getInt("id"));
				usuario.setNome(rset.getString("nome"));
				usuario.setEmail(rset.getString("email"));
				usuario.setSenha(rset.getString("senha"));
				
				usuarios.add(usuario);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return usuarios;
	}

	public void atualizar(Usuario usuario) {
		
		String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";

		Connection con = null;
		PreparedStatement pstm = null;

		try {

			con = Conectante.createConnectionToMySQL();
			pstm = con.prepareStatement(sql);

			pstm.setString(1, usuario.getNome());
			pstm.setString(2, usuario.getEmail());
			pstm.setString(3, usuario.getSenha());
			pstm.setInt(4, usuario.getId());

			pstm.execute();

			System.out.println("Usuario atualizado");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {
					pstm.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void deletar(int id) {
		
		String sql = "DELETE FROM usuario WHERE id = ?";

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = Conectante.createConnectionToMySQL();
			pstm = con.prepareStatement(sql);

			pstm.setInt(1, id);

			pstm.execute();

			System.out.println("Usuário deletado");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}