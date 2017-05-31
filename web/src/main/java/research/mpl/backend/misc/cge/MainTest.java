package research.mpl.backend.misc.cge;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public class MainTest {

    public static void main(String[] args){
        Random random = new Random();


        DevolucaoRecursosController controller = new DevolucaoRecursosController();

        List<DevolucaoRecursosVO> listaDevolucoesIntrumento =
                controller.buscarDevolucoesPorInstrumentoOrdenadasPorData();

        controller.carregarRecursosDoInstrumento();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Gostaria de adicionar novo recurso? s/n ");
        String simOuNao = scanner.nextLine();
        if (simOuNao.equalsIgnoreCase("s")){
            System.out.println("Entre o nome da fonte: ");
            String nomeRecurso = scanner.nextLine();
            System.out.println("A fonte é " + nomeRecurso);
            System.out.println("Entre o valor da fonte: ");
            BigDecimal valorRecurso = scanner.nextBigDecimal();
            System.out.println("O valor do recurso é " + valorRecurso);
        }


        DevolucaoRecursosVO devolucaoRecursosVO = null;
        for (int i = 0; i < 3; i++) {

            mostrarDevolucoes(listaDevolucoesIntrumento);

            devolucaoRecursosVO = new DevolucaoRecursosVO();
            controller.setDevolucaoRecursosVO(devolucaoRecursosVO);
            controller.gerarDevolucaoParaInstrumento();


            System.out.print("\nLista de recursos: ");
            BigDecimal totalRecursos = BigDecimal.ZERO;
            for (DevolucaoParaFonteVO devolucaoParaFonteVO : devolucaoRecursosVO.getListaDevolucaoParaFonteVO()) {
                System.out.print("  Recurso: " + devolucaoParaFonteVO.getFonte()
                        + " Valor: " + devolucaoParaFonteVO.getRecursoVO().getValorRecurso());
                totalRecursos = totalRecursos.add(devolucaoParaFonteVO.getRecursoVO().getValorRecurso());
            }
            System.out.print("  Total: "+ totalRecursos);

            BigDecimal totalRecursosDevolvidos = BigDecimal.ZERO;
            for (DevolucaoRecursosVO devolucao : listaDevolucoesIntrumento) {
                for (DevolucaoParaFonteVO recursoDevolucao : devolucao.getListaDevolucaoParaFonteVO()) {
                    totalRecursosDevolvidos = totalRecursosDevolvidos
                            .add(recursoDevolucao.getOrdemBancariaVO().getValorObt());
                }
            }

            BigDecimal sobra = totalRecursos.subtract(totalRecursosDevolvidos);
            BigDecimal valorParaDevolver = new BigDecimal(
                    (random.nextInt(sobra.intValue())));
            devolucaoRecursosVO.setValorASerDevolvido(valorParaDevolver);
            System.out.println("\nValor a devolver: " + devolucaoRecursosVO.getValorASerDevolvido());

            controller.calcularValorDevolucaoPorFonteDeRecurso(listaDevolucoesIntrumento);
            System.out.print("Nova devolução: ");

            for (DevolucaoParaFonteVO devolucaoParaFonteVO : devolucaoRecursosVO.getListaDevolucaoParaFonteVO()) {
                System.out.print("  Recurso: " + devolucaoParaFonteVO.getFonte()
                        + " Valor  a devolver: " + devolucaoParaFonteVO.getOrdemBancariaVO().getValorObt());
            }
            System.out.print("\n\n");

            devolucaoRecursosVO.setId(listaDevolucoesIntrumento.size() + 1L);
            for (DevolucaoParaFonteVO devolucaoParaFonteVO : devolucaoRecursosVO.getListaDevolucaoParaFonteVO()) {
                devolucaoParaFonteVO.getOrdemBancariaVO().getSituacaoOrdemBancariaVO()
                        .setCodigo(SituacaoOrdemBancariaEnum.NAO_EFETIVADA.getCodigo());
            }
            listaDevolucoesIntrumento.add(devolucaoRecursosVO);
        }
    }

    public static void mostrarDevolucoes(List<DevolucaoRecursosVO> listaDevolucoesIntrumento){
        for (DevolucaoRecursosVO devolucao : listaDevolucoesIntrumento) {
            System.out.print("\nDevolução: " + devolucao.getId() + " "
                    + "Valor: " + devolucao.getValorASerDevolvido());
            for(DevolucaoParaFonteVO recursoDevolucao : devolucao.getListaDevolucaoParaFonteVO()) {
                System.out.print("	Recurso: " + recursoDevolucao.getFonte() + " "
                        + "Valor: " + recursoDevolucao.getOrdemBancariaVO().getValorObt());
                if(recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO()
                        .getCodigo().equals(SituacaoOrdemBancariaEnum.EFETIVADA.getCodigo())){
                    System.out.print("(E)");
                } else if (recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO()
                        .getCodigo().equals(SituacaoOrdemBancariaEnum.NAO_EFETIVADA.getCodigo())){
                    System.out.print("(NE)");
                } else {
                    System.out.print("(I)");
                }
            }
        }
//        System.out.print("\n");
    }
}
