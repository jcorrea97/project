import java.io.*;
import java.util.*;
import java.nio.file.*;

class Escalonador {

	private static List<Processo> lista_teste = new ArrayList<Processo>();
	private static List<Deque<Processo>> lista_de_processos_prontos = new ArrayList<Deque<Processo>>();
	private static HashMap<String, Bcp> tabela_de_processos = new HashMap<String, Bcp>();
	private static List<Processo> lista_de_bloqueados = new LinkedList<>();
	private static String[] nome_processos = new String[10];
	private static int maior_prioridade = 0;
	private static int quantum = -1;

	public static void carregaArquivos() {
		

		File currentFolder = new File(Paths.get("processos").toString());
		File[] listOfFiles = currentFolder.listFiles();
		Arrays.sort(listOfFiles);
		for (int i = 0; i < 12; i++) {
	        try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]))) {
	        	if(i < 10){
					List<String> textSegment = new ArrayList<String>();
		            String line;
		            while ((line = br.readLine()) != null) {
		                textSegment.add(line);
		            }
		            String processName = textSegment.get(0);
		            char[] arranjoNome = processName.toCharArray();
		            nome_processos[i] = processName;
		            int priority = Integer.parseInt(Files.readAllLines(Paths.get("processos/prioridades.txt")).get(i));
		            if (priority > maior_prioridade)
		            	maior_prioridade = priority;
		            Processo process = new Processo(processName, priority, textSegment, Integer.parseInt(processName.substring(6,arranjoNome.length)));
		            lista_teste.add(process);
		            Bcp bloco = new Bcp(priority, processName);
		            tabela_de_processos.put(processName, bloco);
		        }

		    	if(i == 11)
	            	quantum = Integer.parseInt(br.readLine());
	        } 
	        catch (IOException e) {
	            System.err.format("IOException: %s%n", e);
	        }
		}
	}


	public static void criandoListaPrioridades() {
		for (int i = maior_prioridade; i > 0; i--) {

			Deque<Processo> process = new LinkedList<>();

			for (int j = 0; j < 10; j++) {
				try {
					int prioridade_atual = Integer.parseInt(Files.readAllLines(Paths.get("processos/prioridades.txt")).get(j));

					if(prioridade_atual == i)
						process.addLast(lista_teste.get(j));

		        } catch (IOException e) {
		            System.err.format("IOException: %s%n", e);
		        }
			}

			if (process.size() > 0)
				lista_de_processos_prontos.add(process);
		}
		
		Deque<Processo> prioridade0 = new LinkedList<>();
		lista_de_processos_prontos.add(prioridade0);
	}


	public static void printaOrdemProntos(){

		for(int i = 0; i <= maior_prioridade; i++){
			for(Iterator it = lista_de_processos_prontos.get(i).iterator(); it.hasNext(); ){
				Processo p = (Processo) it.next();
				System.out.println("carregando " + p.getNome());
			}
			System.out.println();
			System.out.println("fim da fila " + i);
			System.out.println();
		}
	}

	// public static void reorganiza(int fila){
	// 	//Collections.sort(lista_de_processos_prontos.get(fila));
	// }


	public static void checaBloquados(){
		Iterator it = lista_de_bloqueados.iterator();

		while(it.hasNext()){
			Processo p = (Processo) it.next();

			p.diminuiTempo();

			if(p.getTempoBloq() == 0){
				lista_de_bloqueados.remove(p);

				//aqui da cagada pq credito = 0 siginifica tabela 4
				lista_de_processos_prontos.get(   maior_prioridade - p.getCreditos()   ).addLast(p);
				it = lista_de_bloqueados.iterator();
			}

		}
			

	}




	public static void atualizaBcp(Bcp bloco, int pc1, String estado, int x, int y, int credit){
		bloco.setPc(pc1);
		bloco.setEstado(estado);
		bloco.setX(x);
		bloco.setY(y);
		bloco.setCreditos(credit);
	}


	public static void executa(int fila){

		while(lista_de_processos_prontos.get(fila).size() > 0){

			Iterator it = lista_de_processos_prontos.get(fila).iterator();

			Processo p = (Processo) it.next();


//			System.out.println();
			System.out.println("executando " + p.getNome());

			int i = 0;
			for(i = 0; i < (p.getNQuantum() * quantum); i++){

				String comando1 = p.getNextComando();
//				System.out.println("	-> " + comando1);
				char[] comando = comando1.toCharArray();
				int tamanho = comando.length;

				switch (comando[0]){

            		case 'X':
            			p.setX(Integer.parseInt(comando1.substring(2,tamanho)));
                	
                		break;

            		case 'Y':
            			p.setY(Integer.parseInt(comando1.substring(2,tamanho)));
               			break;

               		case 'E':
               			System.out.println("E/S iniciada em " + p.getNome());
               			System.out.println("Interrompendo " + p.getNome() + " apos " + (i+1) + " instrucoes");

						int aux = p.getCreditos();
						p.setCreditos(aux-2);
						lista_de_processos_prontos.get(fila).removeFirst();

						lista_de_bloqueados.add(p);
						p.aumentaQuantum();
						p.setTempoBloq(2);

						p.setEstado("bloqueado");

               			atualizaBcp(tabela_de_processos.get(p.getNome()), p.getPc(), p.getEstado(), p.getX(),p.getY(), p.getCreditos());

               			checaBloquados();
               			
               			break;
               			

               		case 'S':

               			lista_de_processos_prontos.get(fila).removeFirst();
						System.out.println(p.getNome() + " terminado " + "X=" + p.getX() + "Y=" + p.getY()  );

						checaBloquados();

						
						break;
               			

               		default:

               			break;
        		}	

        		if(comando[0] == 'E' || comando[0] == 'S')
        			break;		



			}
 
			if(i < p.getNQuantum() * (quantum-1) ){
				continue;
				
			}

			if(i+1 != p.getNQuantum()*quantum){
				System.out.println("Interrompendo " + p.getNome() + " apos " + i + " instrucoes" ); 

				int aux = p.getCreditos();
				p.setCreditos(aux-2);
				lista_de_processos_prontos.get(fila).removeFirst();

				if(fila+2 <= maior_prioridade)
					lista_de_processos_prontos.get(fila+2).addFirst(p);
				else
					lista_de_processos_prontos.get(maior_prioridade).addFirst(p);

				p.aumentaQuantum();
				atualizaBcp(tabela_de_processos.get(p.getNome()), p.getPc(), p.getEstado(), p.getX(),p.getY(), p.getCreditos());
				checaBloquados();
			}

		}

	}

	//falta terminar
	public static void redistribui(){

		lista_teste.clear();
		Iterator it = lista_de_processos_prontos.get(maior_prioridade).iterator();
			while(it.hasNext()){

				Processo p = (Processo) it.next();

				int fila = p.getPrioridade();

				p.setCreditos(p.getPrioridade());

				lista_teste.add(p);

				lista_de_processos_prontos.get(maior_prioridade).removeFirst();

				it = lista_de_processos_prontos.get(maior_prioridade).iterator();

			}

			Collections.sort(lista_teste, new Comparator<Processo>() {
			@Override public int compare(Processo o1, Processo o2) {
				if (o1.getVnome() > o2.getVnome() )
					return 1;
				if(o1.getVnome() < o2.getVnome() )
					return -1;
				return 0;
			}
		});

			Collections.sort(lista_teste);

	}




//pode ter um for, ou um while, enquanto a fila de prioridade 0 não tem 10 itens
	public static void main(String[] args) {
		carregaArquivos();
		criandoListaPrioridades();
		printaOrdemProntos();

		System.out.println("_____________________");
		System.out.println();

		for(int i = 0; i < 4 ; i++){
		//	printaOrdemProntos();
			executa(i);
		//	printaOrdemProntos();
		}

		System.out.println("_____________________");
		//executa(0);
		

		printaOrdemProntos();

		System.out.println("_____________________");
		redistribui();
		printaOrdemProntos();



			System.out.println(lista_teste.get(0).getNome());
			System.out.println(lista_teste.get(1).getNome());
			System.out.println(lista_teste.get(2).getNome());


		
			//printa lista de bloqueados
		// for(Iterator it = lista_de_bloqueados.iterator(); it.hasNext(); ){
		// 		Processo p = (Processo) it.next();
		// 		System.out.print( p.getNome() + "  ");
		// 		System.out.println( p.getCreditos());
		// }
		
	}
}