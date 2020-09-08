package view;

import Model.Produto;
import dao.DAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.ListSelectionModel;

public class TelaDeProduto extends JFrame {

	private JTable tblProdutos;
	private JButton btnExcluir;
	DefaultTableModel dadosTabela; // acesso aos dados da tabela
	DAO dao = new DAO(); // acesso à camada de dados
		
	public TelaDeProduto() {
		// configurações gerais da janela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 557, 307);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // layout absoluto
		// centralizar a janela
		this.setLocationRelativeTo(null);
		
		// título da janela
		JLabel lblCadastroDeProdutos = new JLabel("Cadastro de Produtos");
		lblCadastroDeProdutos.setFont(new Font("Dialog", Font.BOLD, 19));
		lblCadastroDeProdutos.setBounds(119, 12, 239, 15);
		contentPane.add(lblCadastroDeProdutos);
				
		// rótulos
		JLabel lblNome = new JLabel("Nome: ");
		lblNome.setBounds(26, 184, 70, 15);
		getContentPane().add(lblNome);
		
		JLabel lblCodigo = new JLabel("Códogo:");
		lblCodigo.setBounds(26, 211, 70, 15);
		getContentPane().add(lblCodigo);
		
		JLabel lblValor = new JLabel("Valor(R$):");
		lblValor.setBounds(26, 238, 70, 15);
		getContentPane().add(lblValor);
		
		// caixas de texto
		JTextField txtNome = new JTextField();
		txtNome.setText("");
		txtNome.setBounds(99, 182, 279, 19);
		getContentPane().add(txtNome);
		txtNome.setColumns(10);
		
		JTextField txtCodigo = new JTextField();
		txtCodigo.setText("");
		txtCodigo.setColumns(10);
		txtCodigo.setBounds(99, 209, 209, 19);
		getContentPane().add(txtCodigo);
		
		JTextField txtValor = new JTextField();
		txtValor.setText("");
		txtValor.setColumns(10);
		txtValor.setBounds(99, 236, 135, 19);
		getContentPane().add(txtValor);
		
		//botão adicionar
		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(389, 206, 100, 25);
		// definir ação do botão adicionar
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// obter informações da tela
				String nome = txtNome.getText();
				String cod = txtCodigo.getText();
				double valor = 0;
				try {
					valor = Double.parseDouble(txtValor.getText());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "O valor deve ser numérico"); 
					return;
				}
				// criar Produto
				Produto novo = new Produto(nome, cod, valor);
				// adicionar na camada de dados
				dao.CreateProduto(novo);
				// adicionar na tela
				dadosTabela.addRow(new String[] {String.valueOf(novo.getId()), novo.getNome(),
						novo.getCodigo(), String.valueOf(novo.getValor())});
				// limpar os campos
				txtNome.setText("");
				txtCodigo.setText("");
				txtValor.setText("");
			}
		});
		getContentPane().add(btnAdicionar);
		
		// configurações da tabela/grid
		dadosTabela = new DefaultTableModel(
			new String[][] { }, // dados iniciais
			new String[] {"Id", "Nome", "Código", "Valor(R$)" } // cabeçalho
		);		
		// usar construtor sem parâmetros, senão não consegue alterar defaultTableModel
		tblProdutos = new JTable(dadosTabela);
		// permitir a seleção de apenas uma linha
		tblProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// associar modelo definido acima (definição das colunas)
		tblProdutos.setModel(dadosTabela);
		// aprimoramento visual da borda
		tblProdutos.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		// tamanho da grid
		tblProdutos.setBounds(30, 38, 387, 116);
		// inserir jscrollpane para exibir cabeçalho da tabela
		JScrollPane scrollPane = new JScrollPane(tblProdutos);
		scrollPane.setLocation(28, 39);
		scrollPane.setSize(390,125);
		contentPane.add(scrollPane);
		// carregar os dados na grid
		for (Produto p : dao.GetAllProduto()) {
			dadosTabela.addRow(new String[] {String.valueOf(p.getId()), p.getNome(), p.getCodigo(), String.valueOf(p.getValor())});
		}				
		
		// evento que altera estado do botão excluir
		tblProdutos.getSelectionModel().addListSelectionListener(
			 new ListSelectionListener(){
				 public void valueChanged(ListSelectionEvent event) {
					 Boolean alguemSelecionado = tblProdutos.getSelectedRow() != -1;
					 btnExcluir.setEnabled(alguemSelecionado); 
				 }
	         }
		);
		
		// botão de exclusão
		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.setBounds(427, 35, 100, 25);		
		// outra forma de codificar o botão: criar o evento separado		
		ActionListener aclExcluir = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// obter a linha selecionada
				int i = tblProdutos.getSelectedRow();
				// existe alguma linha selecionada?
				if (i != -1) {
					// obter id a ser excluído
					String idParaExcluir = (String) dadosTabela.getValueAt(i,  0);
					// remover a linha da grid
					dadosTabela.removeRow(i);
					// remover o produto da fonte de dados
					try {
						dao.DeleteByIdProduto(Integer.parseInt(idParaExcluir));
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Erro ao excluir");
					}
				}
			}
		};
		// associar o evento ao botão
		btnExcluir.addActionListener(aclExcluir);
		// adicionar o botão ao painel
		contentPane.add(btnExcluir);	
		
		// popular o cadastro: criar Produtos e inserir no DAO
		Produto p1 = new Produto("Café Novo", "CN001", 10.00);
		Produto p2 = new Produto("Café Velho", "CV001", 8.00);
		Produto p3 = new Produto("Café Usado", "CU001", 1.00);
		dao.CreateProduto(p1);
		dao.CreateProduto(p2);
		dao.CreateProduto(p3);
		// exibir dados na grid
		dadosTabela.addRow(new String[] {String.valueOf(p1.getId()), p1.getNome(), p1.getCodigo(), String.valueOf(p1.getValor())});
		dadosTabela.addRow(new String[] {String.valueOf(p2.getId()), p2.getNome(), p2.getCodigo(), String.valueOf(p2.getValor())});
		dadosTabela.addRow(new String[] {String.valueOf(p3.getId()), p3.getNome(), p3.getCodigo(), String.valueOf(p3.getValor())});
	}
}