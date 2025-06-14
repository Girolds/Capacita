package model;

import static main.CapacitaTerminal.*;

import java.sql.SQLException;
import java.util.List;

public class Tutor extends Usuario {
	
	private String area;

    

	public String getArea() { return area; }
	public void setArea(String area) { this.area = area; }

   
    public static void exibirMenuTutor() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Menu do Tutor (" + loggedInUser.getNome() + ") ---");
            System.out.println("1. Criar Curso");
            System.out.println("2. Editar ou Excluir Curso");
            System.out.println("3. Criar Módulo");
            System.out.println("4. Editar ou Excluir Módulo");
            System.out.println("5. Adicionar Videoaula");
            System.out.println("6. Editar ou Excluir Videoaula");
            System.out.println("7. Postar Material (Postagem)");
            System.out.println("8. Editar ou Excluir Material (Postagem)");
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
                case 1: criarCurso(); break;
                case 2: editarOuExcluirCurso(); break;
                case 3: criarModulo(); break;
                case 4: try {
					editarOuExcluirModulo();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} break;
                case 5: adicionarVideoaula(); break;
                case 6: try {
					editarOuExcluirVideoaula();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} break;
                case 7: try {
					postarMaterial();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} break;
                case 8: try {
					editarOuExcluirPostagem();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} break;
                case 0:
                    loggedInUser = null; 
                    System.out.println("Logout realizado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void criarCurso() {
        System.out.println("\n--- Criar Novo Curso ---");
        System.out.print("Título do Curso: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição do Curso: ");
        String descricao = scanner.nextLine();
        System.out.print("Área do Curso: ");
        String area = scanner.nextLine();

        if (titulo.trim().isEmpty() || descricao.trim().isEmpty() || area.trim().isEmpty()) {
            System.out.println("Título, descrição e área não podem ser vazios.");
            return;
        }

        Curso novoCurso = new Curso();
        novoCurso.setTitulo(titulo);
        novoCurso.setDescricao(descricao);
        novoCurso.setArea(area);
        cursoDAO.criar(novoCurso);
    }

    public static void editarOuExcluirCurso() {
        System.out.println("\n--- Editar ou Excluir Curso ---");
        List<Curso> cursos = cursoDAO.listar();
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
            return;
        }
        cursos.forEach(c -> System.out.println("ID: " + c.getId() + ", Título: " + c.getTitulo()));

        System.out.print("Digite o ID do curso para editar/excluir (0 para voltar): ");
        int idCurso = scanner.nextInt();
        scanner.nextLine();
        if (idCurso == 0) return;

        Curso cursoParaAcao = cursoDAO.buscar(idCurso);
        if (cursoParaAcao == null) {
            System.out.println("Curso não encontrado.");
            return;
        }

        System.out.println("1. Editar | 2. Excluir");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 1) { 
            System.out.print("Novo título (atual: " + cursoParaAcao.getTitulo() + "): ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.trim().isEmpty()) cursoParaAcao.setTitulo(novoTitulo);
            
            System.out.print("Nova descrição: ");
            String novaDescricao = scanner.nextLine();
            if (!novaDescricao.trim().isEmpty()) cursoParaAcao.setDescricao(novaDescricao);

            System.out.print("Nova área (atual: " + cursoParaAcao.getArea() + "): ");
            String novaArea = scanner.nextLine();
            if (!novaArea.trim().isEmpty()) cursoParaAcao.setArea(novaArea);
            
            cursoDAO.atualizar(cursoParaAcao);
        } else if (opcao == 2) { 
            System.out.print("Tem certeza que deseja excluir o curso '" + cursoParaAcao.getTitulo() + "' e TODO o seu conteúdo? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                cursoDAO.deletar(idCurso);
            }
        }
    }

    public static void criarModulo() {
        System.out.println("\n--- Criar Novo Módulo ---");
        List<Curso> cursos = cursoDAO.listar();
        if (cursos.isEmpty()) {
            System.out.println("Crie um curso primeiro.");
            return;
        }
        cursos.forEach(c -> System.out.println("ID: " + c.getId() + " - " + c.getTitulo()));
        System.out.print("Digite o ID do curso ao qual este módulo pertence: ");
        int idCurso = scanner.nextInt();
        scanner.nextLine();

        if (cursoDAO.buscar(idCurso) == null) {
            System.out.println("Curso não encontrado.");
            return;
        }

        System.out.print("Título do Módulo: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição do Módulo: ");
        String descricao = scanner.nextLine();

        Modulo novoModulo = new Modulo();
        novoModulo.setTitulo(titulo);
        novoModulo.setDescricao(descricao);
        try {
			moduloDAO.criar(novoModulo, idCurso);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void editarOuExcluirModulo() throws SQLException {
        System.out.println("\n--- Editar ou Excluir Módulo ---");
        System.out.print("Digite o ID do curso para listar os módulos: ");
        int idCurso = scanner.nextInt();
        scanner.nextLine();

        List<Modulo> modulos = moduloDAO.listarPorCurso(idCurso);
        if (modulos.isEmpty()) {
            System.out.println("Nenhum módulo encontrado para este curso.");
            return;
        }
        modulos.forEach(m -> System.out.println("ID: " + m.getId() + ", Título: " + m.getTitulo()));

        System.out.print("Digite o ID do módulo para editar/excluir (0 para voltar): ");
        int idModulo = scanner.nextInt();
        scanner.nextLine();
        if (idModulo == 0) return;

        Modulo moduloParaAcao = moduloDAO.buscar(idModulo);
        if (moduloParaAcao == null) {
            System.out.println("Módulo não encontrado.");
            return;
        }

        System.out.println("1. Editar | 2. Excluir");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 1) { 
            System.out.print("Novo título (atual: " + moduloParaAcao.getTitulo() + "): ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.trim().isEmpty()) moduloParaAcao.setTitulo(novoTitulo);
            
            System.out.print("Nova descrição: ");
            String novaDescricao = scanner.nextLine();
            if (!novaDescricao.trim().isEmpty()) moduloParaAcao.setDescricao(novaDescricao);

            moduloDAO.atualizar(moduloParaAcao);
        } else if (opcao == 2) { 
             System.out.print("Tem certeza que deseja excluir o módulo '" + moduloParaAcao.getTitulo() + "'? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                moduloDAO.deletar(idModulo);
            }
        }
    }
    
    public static void adicionarVideoaula() {
        System.out.println("\n--- Adicionar Nova Videoaula ---");
        System.out.print("Digite o ID do módulo ao qual esta videoaula pertence: ");
        int idModulo = scanner.nextInt();
        scanner.nextLine();

        try {
			if (moduloDAO.buscar(idModulo) == null) {
			    System.out.println("Módulo não encontrado.");
			    return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.print("Título da Videoaula: ");
        String titulo = scanner.nextLine();
        System.out.print("URL da Videoaula: ");
        String url = scanner.nextLine();

        VideoAula novaVideoAula = new VideoAula();
        novaVideoAula.setTitulo(titulo);
        novaVideoAula.setUrl(url);
        try {
			videoAulaDAO.postar(novaVideoAula, idModulo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void editarOuExcluirVideoaula() throws SQLException {
        System.out.println("\n--- Editar ou Excluir Videoaula ---");
        System.out.print("Digite o ID do módulo para listar as videoaulas: ");
        int idModulo = scanner.nextInt();
        scanner.nextLine();

        List<VideoAula> videoAulas = videoAulaDAO.listarPorModulo(idModulo);
        if (videoAulas.isEmpty()) {
            System.out.println("Nenhuma videoaula encontrada.");
            return;
        }
        videoAulas.forEach(va -> System.out.println("ID: " + va.getId() + ", Título: " + va.getTitulo()));

        System.out.print("Digite o ID da videoaula para editar/excluir: ");
        int idVideoAula = scanner.nextInt();
        scanner.nextLine();

        VideoAula videoAulaParaAcao = videoAulaDAO.buscar(idVideoAula);
        if (videoAulaParaAcao == null) {
            System.out.println("Videoaula não encontrada.");
            return;
        }

        System.out.println("1. Editar | 2. Excluir");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        
        if (opcao == 1) { 
            System.out.print("Novo título: ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.trim().isEmpty()) videoAulaParaAcao.setTitulo(novoTitulo);
            
            System.out.print("Nova URL: ");
            String novaUrl = scanner.nextLine();
            if (!novaUrl.trim().isEmpty()) videoAulaParaAcao.setUrl(novaUrl);

            videoAulaDAO.atualizar(videoAulaParaAcao);
        } else if (opcao == 2) { 
            System.out.print("Tem certeza? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                videoAulaDAO.deletar(idVideoAula);
            }
        }
    }

    public static void postarMaterial() throws SQLException {
        System.out.println("\n--- Postar Novo Material (Postagem) ---");
        System.out.print("Título da Postagem: ");
        String titulo = scanner.nextLine();
        System.out.print("Conteúdo da Postagem: ");
        String conteudo = scanner.nextLine();

        System.out.print("A postagem pertence a um (1) Curso ou (2) Módulo? ");
        int tipoRelacao = scanner.nextInt();
        scanner.nextLine();

        Postagem novaPostagem = new Postagem();
        novaPostagem.setTitulo(titulo);
        novaPostagem.setConteudo(conteudo);
        
        if (tipoRelacao == 1) { 
            System.out.print("Digite o ID do curso: ");
            int idCurso = scanner.nextInt();
            scanner.nextLine();
            Curso c = cursoDAO.buscar(idCurso);
            if (c == null) {
                System.out.println("Curso não encontrado.");
                return;
            }
            novaPostagem.setCurso(c);
        } else if (tipoRelacao == 2) { 
            System.out.print("Digite o ID do módulo: ");
            int idModulo = scanner.nextInt();
            scanner.nextLine();
            Modulo m = moduloDAO.buscar(idModulo);
            if (m == null) {
                System.out.println("Módulo não encontrado.");
                return;
            }
            novaPostagem.setModulo(m);
        } else {
            System.out.println("Opção inválida.");
            return;
        }
        postagemDAO.postar(novaPostagem);
    }
    
    public static void editarOuExcluirPostagem() throws SQLException {
        System.out.println("\n--- Editar ou Excluir Postagem ---");
        System.out.print("Listar postagens por (1) Curso ou (2) Módulo? ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        List<Postagem> postagens = null;
        if (escolha == 1) {
            System.out.print("Digite o ID do curso para listar as postagens: ");
            int idCurso = scanner.nextInt();
            scanner.nextLine();
            postagens = postagemDAO.listarPorCursoSemModulo(idCurso);
        } else if (escolha == 2) {
            System.out.print("Digite o ID do módulo para listar as postagens: ");
            int idModulo = scanner.nextInt();
            scanner.nextLine();
            postagens = postagemDAO.listarPorModulo(idModulo);
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        if (postagens == null || postagens.isEmpty()) {
            System.out.println("Nenhuma postagem encontrada.");
            return;
        }

        System.out.println("Postagens encontradas:");
        postagens.forEach(p -> System.out.println("ID: " + p.getId() + ", Título: " + p.getTitulo()));

        System.out.print("Digite o ID da postagem para editar/excluir (0 para voltar): ");
        int idPostagem = scanner.nextInt();
        scanner.nextLine();

        if (idPostagem == 0) return;

        Postagem postagemParaAcao = postagemDAO.buscar(idPostagem);
        if (postagemParaAcao == null) {
            System.out.println("Postagem não encontrada.");
            return;
        }

        System.out.println("1. Editar | 2. Excluir");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 1) { 
            System.out.print("Novo título (atual: " + postagemParaAcao.getTitulo() + "): ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.trim().isEmpty()) {
                postagemParaAcao.setTitulo(novoTitulo);
            }
            
            System.out.print("Novo conteúdo: ");
            String novoConteudo = scanner.nextLine();
            if (!novoConteudo.trim().isEmpty()) {
                postagemParaAcao.setConteudo(novoConteudo);
            }
            
            postagemDAO.atualizar(postagemParaAcao);
            System.out.println("Postagem atualizada com sucesso!");
            
        } else if (opcao == 2) {
            System.out.print("Tem certeza que deseja excluir a postagem '" + postagemParaAcao.getTitulo() + "'? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                postagemDAO.deletar(idPostagem);
                System.out.println("Postagem excluída com sucesso!");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } else {
            System.out.println("Opção inválida.");
        }
    }
}