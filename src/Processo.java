import java.util.*;
	
class Processo implements Comparable<Processo> {
	private String nome;
	private int valorNome;
	private int contador_de_programa = 0;
	private String estado = "pronto";
	private int prioridade;
	private int x = 0;
	private int y = 0;


	private int tempoBloqueado = 0;
	private int numQuantum = 1;

	private String[] regiao_memoria;
	private List<String> comandos;
	private int creditos = -1;

	public Processo(String name,int priority, List<String> textSegment, int valor) {
		this.nome = name;
		this.prioridade = priority;
		this.comandos = textSegment;
		this.creditos = priority;
		this.valorNome = valor;
	}



	public void aumentaQuantum(){
		this.numQuantum++;
	}

	public int getVnome(){
		return this.valorNome;
	}

	public int getNQuantum(){
		return this.numQuantum;
	}


	public void setTempoBloq(int x1){
		this.tempoBloqueado = x1;
	}

	public void diminuiTempo(){
		this.tempoBloqueado--;
	}

	public int getTempoBloq(){
		return this.tempoBloqueado;
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

	public int getY() {
		return this.y;
	}

	public void setY(int y1) {
		 this.y = y1;
	}

	public void setX(int x1) {
		 this.x = x1;
	}

	public int getPc() {
		return this.contador_de_programa;
	}

	public int getCreditos() {
		 return this.creditos;
	}

	public void setCreditos(int x1) {
		if(x1 >= 0)
			this.creditos = x1;
		else
			this.creditos = 0;
	}

	public String getNextComando() {
		return this.comandos.get(++contador_de_programa);
	}


	@Override public int compareTo(Processo process) {

		if(this.creditos < process.getCreditos()) 
			return 1;
		if(this.creditos > process.getCreditos()) 
			return -1;
		return 0;
	}

}