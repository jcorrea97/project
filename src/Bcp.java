import java.util.*;
	
class Bcp {

	// aqui deve ter os creditos e nao no processo
	private int contador_de_programa = -1;
	private String estado = "pronto";
	private int prioridade;
	private int x = 0;
	private int y = 0;
	private String[] regiao_memoria;
	private String nome;

	private int creditos = -1;

	public Bcp(int priority, String name) {
		this.nome = name;
		this.prioridade = priority;
		this.creditos = priority;
	}

	public String getNome() {
		return this.nome;
	}

	public int getPrioridade() {
		return this.prioridade;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String state) {
		this.estado = state;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x1) {
		 this.x = x1;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y1) {
		 this.y = y1;
	}

	public int getCreditos() {
		return this.creditos;
	}

	public void setCreditos(int x) {
		 this.creditos = x;
	}

	public int getPc() {
		return this.contador_de_programa;
	}
	public void setPc(int z){
		this.contador_de_programa = z;

	}
}