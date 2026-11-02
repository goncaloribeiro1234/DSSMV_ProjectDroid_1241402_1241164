package com.example.lojadevideojogos.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.controllers.StoreController;
import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.util.DataState;

public class EditGameActivity extends AppCompatActivity {

    private EditText editTitulo, editPreco, editStock, editImageUrl;
    private Button btnSalvar;
    private StoreController storeController;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        storeController = new StoreController();

        editTitulo = findViewById(R.id.editTitulo);
        editPreco = findViewById(R.id.editPreco);
        editStock = findViewById(R.id.editStock);
        editImageUrl = findViewById(R.id.editImageUrl);
        btnSalvar = findViewById(R.id.btnSalvarEdicao);

        // Receber o jogo enviado pelo GameDetailActivity
        game = (Game) getIntent().getSerializableExtra("EXTRA_GAME");

        if (game != null) {
            editTitulo.setText(game.getTitle());
            editPreco.setText(String.valueOf(game.getPrice()));
            editStock.setText(String.valueOf(game.getStock()));
            editImageUrl.setText(game.getImageUrl());
        }

        // Observar o resultado da atualização na BD
        storeController.getActionState().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS) {
                Toast.makeText(this, "Jogo atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha o ecrã de edição e volta aos detalhes
            } else if (state.status == DataState.Status.ERROR) {
                Toast.makeText(this, "Erro ao atualizar: " + state.errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Botão para guardar as alterações
        btnSalvar.setOnClickListener(v -> {
            if (game != null) {
                try {
                    game.setTitle(editTitulo.getText().toString().trim());
                    game.setPrice(Double.parseDouble(editPreco.getText().toString().trim()));
                    game.setStock(Integer.parseInt(editStock.getText().toString().trim()));
                    game.setImageUrl(editImageUrl.getText().toString().trim());

                    // Chamar o StoreController para atualizar no RestDB
                    storeController.updateGame(game);

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Verifica se os valores numéricos estão corretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}