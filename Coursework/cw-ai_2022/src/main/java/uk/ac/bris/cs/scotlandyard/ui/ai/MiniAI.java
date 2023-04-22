package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.lang.module.ModuleReference;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

public class MiniAI implements Ai {


	@Nonnull @Override public String name() { return "MiniAI"; }

	@Nonnull @Override public Move pickMove(
			@Nonnull Board board,
			Pair<Long, TimeUnit> timeoutPair) {
		// returns a random move, replace with your own implementation
		var moves = board.getAvailableMoves().asList();
		return moves.get(new Random().nextInt(moves.size()));

	}

	public int score(Board board) {
		GameSetup setup = board.getSetup();
		ImmutableSet<Move> moves = board.getAvailableMoves();
		ImmutableSet<Piece> players = board.getPlayers();
		for(Move move : moves) {
			for (Piece player :players) {
				if (player.isDetective()) {
					for (int node : setup.graph.adjacentNodes(move.source())) {
						if (board.getDetectiveLocation(Detective(player.webColour())).hasValue(node))
						{

						}
					}
				}
			}
		}
		return 0;
	}
}
