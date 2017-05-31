package research.mpl.backend.smart.util.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CustomFileUtils {

	private String nomeArquivo = null;

	public CustomFileUtils() {
		this.nomeArquivo = null;
		;
	}

	public CustomFileUtils(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String[] readFile(int numeroAmostras, String caminhoArquivo) {

		// public String[] lerArquivo(int numeroAmostras, File caminhoArquivo){
		String[] arquivos = new String[numeroAmostras];
		;

		File file = new File(caminhoArquivo);
		// File file = caminhoArquivo;
		FileInputStream fis = null;

		/* Obrigatoriamente temos que tratar a exceção de FileInputStream... */
		try {
			fis = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
			String linha = bReader.readLine();

			/* Armazena as linhas do arquivo num array de Strings, que representam cada imagem. */
			int i = 0;
			/*
			 * Pode ser que o número de amostras fornecido pelo usuário seja menor que a real quantidade de linhas do arquivo. Por isso que temos que testar se
			 * i < numeroAmostras, para não dar overflow no array. Porém, se o usuário entrar com o número de amostras que é maior que a real quantidade de
			 * linhas do arquivo, é pra dar certo, mas vai sobrar posições no array
			 */
			while (linha != null && i < numeroAmostras) {
				arquivos[i] = linha;
				i++;
				linha = bReader.readLine(); // Lê então a proxima linha.
			}

			fis.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		return arquivos;
	}

	// Esta função retorna se já existe o arquivo especificado ou não
	public boolean fileExists(String caminhoArquivo) {
		File arquivo = new File(caminhoArquivo);
		boolean exists = arquivo.exists();
		return exists;
	}

	// Esta função abre e lê um arquivo e retorna o que tem nele como uma string
	public String openFile(String caminhoArquivo) {

		String conteudo = "";
		File file = new File(caminhoArquivo);
		// File file = caminhoArquivo;
		FileInputStream fis = null;

		/* Obrigatoriamente temos que tratar a exceção de FileInputStream... */
		try {
			fis = new FileInputStream(file);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
			String linha = bReader.readLine();

			while (linha != null) {
				conteudo += linha + "\n";
				linha = bReader.readLine(); // Lê então a proxima linha.
			}

			fis.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		return conteudo;
	}

	// Esta função retorna o número de linhas que tem no arquivo txt
	public int lerQTDlinhas(BufferedReader a) {

		String linha = "";
		int numLinhas = 0; // Quantidade de linhas (ou amostras)
		try {
			while (linha != null) {
				if (numLinhas == 0)
					numLinhas++; // Conta com a que foi lida fora do while
				linha = a.readLine(); // Lê então a proxima linha (da segunda em diante, pois a 1ª já foi lida fora do for)
				numLinhas++;
			}
			System.out.println("\n\n" + numLinhas);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		return numLinhas;
	}

	// Esta função grava um arquivo txt com o texto recebido como parâmetro
	public void saveFile(String texto, String nomeArquivo) {
		System.out.println("Arquivo: " + nomeArquivo);
		File novoArquivo = new File(nomeArquivo); // nomeArquivo == algumacoisa.txt
		FileWriter writer;
		try {
			writer = new FileWriter(novoArquivo);
			BufferedWriter br = new BufferedWriter(writer);
			PrintWriter saida = new PrintWriter(br, true);
			saida.print(texto);
			saida.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Esta função nos permite abrir o arquivo adicionar conteudo
	public void gravarEmArquivoAberto(String texto) {

		File novoArquivo = new File(nomeArquivo); // nomeArquivo == algumacoisa.txt
		FileWriter writer;
		String conteudo = "";

		boolean exists = novoArquivo.exists();
		if (exists) {
			// File or directory exists
			try {
				FileInputStream fis = new FileInputStream(novoArquivo);
				BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
				String linha = bReader.readLine();

				while (linha != null) {
					conteudo += linha + "\n";
					linha = bReader.readLine(); // Lê então a proxima linha.
				}
				// Concatena o que já tinha antes com o novo texto
				conteudo += texto;

				// Grava o arquivo
				writer = new FileWriter(novoArquivo);
				BufferedWriter br = new BufferedWriter(writer);
				PrintWriter saida = new PrintWriter(br, true);
				saida.println(conteudo);

				bReader.close();
				saida.close();
				writer.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}

		} else {
			// File or directory does not exist
			try {
				writer = new FileWriter(novoArquivo);
				BufferedWriter br = new BufferedWriter(writer);
				PrintWriter saida = new PrintWriter(br, true);
				saida.println(texto);
				saida.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
