package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.lang.module.ModuleReference;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

import static java.lang.Math.*;

public class MiniAI implements Ai {

	@Nonnull @Override public String name() { return "MiniAI"; }

	@Nonnull @Override public Move pickMove(
			@Nonnull Board board,
			Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
		var moves = board.getAvailableMoves().asList();
		return moves.get(new Random().nextInt(moves.size()));

	}

	public int findBestScore(Board board) {
		//Move bestMove =
		return 0;
	}


	public int score(Move move, Board board) {
		Optional<Integer> temp;
		int score = 0;
		GameSetup setup = board.getSetup();
		ImmutableSet<Piece> players = board.getPlayers();
		for (Piece player :players) {
			if (player.isDetective()) {
				temp  = board.getDetectiveLocation(Piece.Detective.valueOf(player.webColour()));
				score = score + abs(temp.get() - move.source());

				//if (board.getDetectiveLocation(Piece.Detective.valueOf(player.webColour())).hasValue(node))
				}
			}
		return score;
	}
}
