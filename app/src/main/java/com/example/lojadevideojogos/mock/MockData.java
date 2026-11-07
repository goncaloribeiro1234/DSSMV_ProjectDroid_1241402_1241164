package com.example.lojadevideojogos.mock;

import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.model.GameRequest;
import com.example.lojadevideojogos.model.RequestStatus;

import java.util.ArrayList;
import java.util.List;

public class MockData {

    public static List<Game> getMockGames() {
        List<Game> games = new ArrayList<>();
        // Links Wikimedia garantidos para o Glide não falhar nenhum
        games.add(new Game("mock_g1", 1001, "Grand Theft Auto V", "https://upload.wikimedia.org/wikipedia/en/a/a5/Grand_Theft_Auto_V.png", 59.99, 10, false, null));
        games.add(new Game("mock_g2", 1002, "The Witcher 3", "https://upload.wikimedia.org/wikipedia/en/0/0f/The_Witcher_3_Wild_Hunt_Cover.jpg", 39.99, 3, true, 29.99));
        games.add(new Game("mock_g3", 1003, "Cyberpunk 2077", "https://upload.wikimedia.org/wikipedia/en/9/9f/Cyberpunk_2077_box_art.jpg", 19.99, 5, false, null));
        games.add(new Game("mock_g4", 1004, "EA Sports FC 24", "https://upload.wikimedia.org/wikipedia/en/b/b2/EA_Sports_FC_24_Standard_Edition_Cover.jpg", 49.99, 15, false, null));
        games.add(new Game("mock_g5", 1005, "Red Dead Redemption 2", "https://upload.wikimedia.org/wikipedia/en/4/44/Red_Dead_Redemption_II.jpg", 49.99, 8, false, null));
        games.add(new Game("mock_g6", 1006, "God of War", "https://upload.wikimedia.org/wikipedia/en/8/81/God_of_War_4_cover.jpg", 39.99, 12, true, 24.99));

        return games;
    }

    public static List<GameRequest> getMockRequests() {
        List<GameRequest> requests = new ArrayList<>();
        requests.add(new GameRequest("mock_r1", 2001, "Jogo Pedido X", null,
                RequestStatus.PENDING, "2026-10-14"));
        requests.add(new GameRequest("mock_r2", 2002, "Jogo Pedido Y", null,
                RequestStatus.APPROVED, "2026-10-12"));
        requests.add(new GameRequest("mock_r3", 2003, "Jogo Pedido Z", null,
                RequestStatus.REJECTED, "2026-10-10"));
        return requests;
    }

    public static List<Game> getMockRawgResults() {
        List<Game> games = new ArrayList<>();
        games.add(new Game("mock_zelda", 3498, "Zelda: Mock Edition", "https://upload.wikimedia.org/wikipedia/en/c/c6/The_Legend_of_Zelda_Breath_of_the_Wild.jpg", 0.0, 0, false, 0.0));
        return games;
    }
}