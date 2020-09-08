package test;

import dao.DAO;
import Model.Produto;
import Model.Estoque;

public class TesteDAO {

    public static void main(String[] args) throws Exception {

        // Instância do DAO
        DAO dao = new DAO();
        
        // popular o cadastro de produtos
        Produto p1 = new Produto("Pacote de bala", "PB001", 7.80);
        Produto p2 = new Produto("Celular Sansung", "CS001", 1099.90);
        Produto p3 = new Produto("Pacote de Café", "PC001", 8.69);
        dao.CreateProduto(p1);
        dao.CreateProduto(p2);
        dao.CreateProduto(p3);
        
        // popular o cadastro de Produtos (três pessoas)
        Estoque e = new Estoque(p1, 5);
        dao.CreateEsotuqe(e);
        
        e = new Estoque(p2, 7);
        dao.CreateEsotuqe(e);;
        
        // listar Produtos
        System.out.print("* Produtos no cadastro: ");
        System.out.println(dao.GetQuantityProduto());
        for (Produto pe : dao.GetAllProduto()){
            System.out.println(pe);
        }
        
        // alterar os dados de um Produto
        System.out.println("* alterando nome de p3: ");
        p3.setNome("Lata de Nescau");
        p3.setCod("LN001");
        dao.UpdateProduto(p3);
        System.out.println("novos dados: " + p3);
                
        // teste de busca
        Produto p = dao.GetByIdProduto(2);
        System.out.println("* buscando produto 2: " + p);
        
        // excluir
        dao.DeleteByIdProduto(2);;
        System.out.print("* Produtos no cadastro após exclusão de uma pessoa: ");
        System.out.println(dao.GetAllProduto());
        
        // quais são os estoques no cadastro?
        System.out.println("* Estoques no cadastro: ");
        for (Estoque estoque: dao.GetAllEstoques()) {
            System.out.println(estoque);
        }
    }
}