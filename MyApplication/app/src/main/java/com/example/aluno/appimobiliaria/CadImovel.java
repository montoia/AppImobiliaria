package com.example.aluno.appimobiliaria;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadImovel extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String[] tipos = {"Casa", "Apto", "Terreno", "Sobrado"};
    static final int TIRAR_FOTO = 1;
    Date data;
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_imovel);
        Spinner ps = (Spinner) findViewById(R.id.spTipo);
        ArrayAdapter sa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tipos);
        ps.setAdapter(sa);
        // testar se é edicao
        Intent it = getIntent();
        Imovel im = (Imovel)it.getSerializableExtra("imovelEditando"); // se ele vir imovelEditando
        if( im != null){ // preencher os campos se  o im não vier vazio
            EditText ed = (EditText) findViewById(R.id.edDesc);
            ed.setText(im.getDescricao());
            Spinner sp = (Spinner) findViewById(R.id.spTipo);
            for (int i = 0; i < tipos.length; i++) {
                if(tipos[i].equals(im.getTipo()))
                    sp.setSelection(i);
            }
            ed = (EditText) findViewById(R.id.edEnd);
            ed.setText(im.getEndereco());
            ed = (EditText) findViewById(R.id.edBairro);
            ed.setText(im.getBairro());
            ed = (EditText) findViewById(R.id.edPreco);
            ed.setText(Double.toString(im.getPreco()));
            data = im.getDataValidade();
            ed = (EditText) findViewById(R.id.edData);
            ed.setText(fmt.format(data));
        }


    }

    /// inflar o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cad, menu); // inflar o menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        // verifica a opção do menu pelo id do item clicado
        if(item.getItemId() == R.id.menu_confirmar){
            confirmar(null);
        }
        if(item.getItemId() == R.id.menu_foto){
            tirarFoto(null);
        }

        if(item.getItemId() == R.id.menu_cancelar){
            cancelar(null);
        }
        return true;
    }

    public void tirarFoto(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TIRAR_FOTO);
        }
    }

    //@Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == TIRAR_FOTO && request == RESULT_OK) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            ImageView iv = (ImageView) findViewById(R.id.imagem);
            iv.setImageBitmap(bm);
        }
    }

    public void ajudarData(View v){
        Date hoje = data  == null ? new Date() : data; // data de hoje e operador ternario para verificar se a data for null recebe a data atual

        DatePickerDialog dpl = new DatePickerDialog(this, this,hoje.getYear()+1900, hoje.getMonth(), hoje.getDay()); // dialogo de seleção de data -- mostrar data apartir de 2018
        dpl.show(); // mostrar
    }

    public void confirmar(View v){
        Spinner sp = (Spinner) findViewById(R.id.spTipo);
        EditText edDesc = (EditText) findViewById(R.id.edDesc);
        EditText edEnd = (EditText) findViewById(R.id.edEnd);
        EditText edBairro = (EditText) findViewById(R.id.edBairro);
        EditText edPreco = (EditText) findViewById(R.id.edPreco);
        Imovel im = new Imovel();
        im.setTipo(sp.getSelectedItem().toString());
        im.setDescricao(edDesc.getText().toString());
        im.setEndereco(edEnd.getText().toString());
        im.setBairro(edBairro.getText().toString());
        im.setPreco(Double.parseDouble(edPreco.getText().toString()));
        im.setDataValidade(data);
        Intent it = new Intent();
        it.putExtra("imovel", im);
        setResult(RESULT_OK, it);
        finish();
    }
    public void cancelar(View v){
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        data = new Date(year-1900,monthOfYear,dayOfMonth);
        TextView edData = (TextView) findViewById(R.id.edData);
        edData.setText(fmt.format(data));
    }
}
