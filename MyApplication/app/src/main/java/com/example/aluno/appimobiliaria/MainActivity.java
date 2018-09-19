package com.example.aluno.appimobiliaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener{ // OnItemLongClickListener para clique longo para edição

    public static final int NOVO_IMOVEL = 333;
    public static final int EDITAR_IMOVEL = 444;
    List<Imovel> imoveis;
    ListView listaImoveis;
    ArrayAdapter<Imovel> adapter;
    int posicaoEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // criar lista
        imoveis = new ArrayList<Imovel>();
        adapter = new ArrayAdapter<Imovel>(this, android.R.layout.simple_list_item_single_choice, imoveis); // contexto, como será exibido, objeto
        listaImoveis = (ListView) findViewById(R.id.lista); //inserir no list view
        listaImoveis.setAdapter(adapter); // setar o adapter
        listaImoveis.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaImoveis.setOnItemLongClickListener(this); // referencia o proprio elemento da activity para implementar o método
                                                        // quando ocorrer um evento de clique longo
    }
    /// inflar o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu); // inflar o menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        // verifica a opção do menu pelo id do item clicado
        if(item.getItemId() == R.id.menu_novo){
            adicionar(null);
        }
        if(item.getItemId() == R.id.menu_sair){
            finish();
        }
        return true;
    }

    public void adicionar(View v){
        Intent it = new Intent(this, CadImovel.class); // cria um intensão para activity cadastro de imovel
        //startActivity(it); // inicia a intensão sem retorno
        startActivityForResult(it, NOVO_IMOVEL); // inicia a intensão com retorno
    }

    @Override
    public void onActivityResult(int codigo, int resultado,Intent dados){ //
        if(codigo == NOVO_IMOVEL && resultado == RESULT_OK){
            Imovel novo = (Imovel) dados.getSerializableExtra("imovel");
            imoveis.add(novo); // adiciona um novo imovel
            adapter.notifyDataSetChanged(); // notifica o adapter que foi alterado
        }
        if(codigo == EDITAR_IMOVEL && resultado == RESULT_OK){ // se o usuario clicar ok na edicao
            Imovel edit = (Imovel) dados.getSerializableExtra("imovel");
            imoveis.set(posicaoEdicao, edit); // salva o objeto de edicao
            adapter.notifyDataSetChanged(); // notifica o adapter que alterou
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Imovel im = imoveis.get(position); // posição do elemento selecionado
        Intent it = new Intent(this, CadImovel.class); // cria a intensão
        it.putExtra("imovelEditando", im); // passa o objeto selecionado
        startActivityForResult(it, EDITAR_IMOVEL);
        posicaoEdicao = position; // guarda a posicao de edicao
        return true;
    }
    public void remover(View v){
        if(listaImoveis.getCheckedItemCount() > 0) {
            final int pos = listaImoveis.getCheckedItemPosition();
            if (pos >= 0) {
                AlertDialog.Builder bld = new AlertDialog.Builder(this); // criando o alerta ou dialogo para mensagem
                bld.setTitle("Confirmação"); // titulo do alerta
                bld.setMessage("Deseja realmente excluir:\n" + imoveis.get(pos) + "?"); // mensagem do alerta
                bld.setCancelable(false); // obriga o usuário a clicar
                bld.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imoveis.remove(pos);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                bld.setNegativeButton("Não", null); // caso não passa null para fechar o dialogo
                bld.show();
            }
        }
    }
}
