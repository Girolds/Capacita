package model;

import java.util.List;

public class Modulo {
	
	private int id;
	private String titulo;
	private String descricao;
	private List<VideoAula> videos;
	private Curso curso;

	public List<VideoAula> getVideos() {
		return videos;
	}
	public void setVideos(List<VideoAula> videos) {
		this.videos = videos;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

	public Curso getCurso() { 
		return curso;
	}
	public void setCurso(Curso curso) { 
		this.curso = curso;
	}
}