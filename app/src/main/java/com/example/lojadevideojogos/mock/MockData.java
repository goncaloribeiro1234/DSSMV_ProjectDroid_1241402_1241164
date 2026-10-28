package com.example.lojadevideojogos.mock;

import com.example.lojadevideojogos.model.Game;
import com.example.lojadevideojogos.model.GameRequest;
import com.example.lojadevideojogos.model.RequestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 - Dados falsos para desenvolver e testar a interface sem rede.
 - Equivale ao pacote "mock" do projeto de exemplo do professor.
 - Os rawg_id são valores de exemplo: não têm de corresponder a jogos reais.
 */
public class MockData {

    public static List<Game> getMockGames() {
        List<Game> games = new ArrayList<>();
        // Substituímos os nulls originais por imagens reais da RAWG para testar o Glide visualmente
        games.add(new Game("mock_g1", 1001, "Grand Theft Auto V", "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg", 59.99, 10, false, null));
        games.add(new Game("mock_g2", 1002, "The Witcher 3", "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg", 39.99, 3, true, 29.99));
        games.add(new Game("mock_g3", 1003, "Cyberpunk 2077", "https://media.rawg.io/media/games/26d/26d440e0abf48c159f43150023f1a3d3.jpg", 19.99, 0, false, null)); // sem stock
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
        // Injetada imagem placeholder real para a pesquisa rawg mock
        games.add(new Game(null, 3498, "Zelda: Mock Edition", "https://media.rawg.io/media/games/879/879c930f9c6787c920153fa2df452eb3.jpg", 0.0, 0, false, 0.0));
        return games;
    }
}