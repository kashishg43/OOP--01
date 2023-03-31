package uk.ac.bris.cs.scotlandyard.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.*;
import javax.annotation.Nonnull;
import uk.ac.bris.cs.scotlandyard.model.Board.GameState;
import uk.ac.bris.cs.scotlandyard.model.Piece.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.*;

/**
 * cw-model
 * Stage 1: Complete this class
 */
public final class MyGameStateFactory implements Factory<GameState> {
	private final class MyGameState implements GameState {
		private GameSetup setup;
		private ImmutableSet<Piece> remaining; /*which pieces can still move in the current round*/
		private ImmutableList<LogEntry> log;
		private Player mrX; /*players that are in the game*/
		private List<Player> detectives;
		private List<Piece> pieces; /*which pieces are in the game*/
		private ImmutableSet<Move> moves;
		private ImmutableSet<Piece> winner;



		private boolean findPieceDuplicates(List<Player> detectives) {
			final List<Piece> duplicates = new ArrayList<>(); /*list of duplicates*/
			final Set<Piece> pieceSet = new HashSet<> (); /*allows the use of contains to check for duplicates*/

			for (Player detective: detectives) { /*loops through all detectives*/
				if (pieceSet.contains(detective.piece())) { /*contains returns true if item exists*/
					duplicates.add(detective.piece());
				}
				else {
					pieceSet.add(detective.piece()); /*adds new items to the set*/
				}

			}
			return duplicates.isEmpty();
		}

		private boolean findLocationDuplicates(List<Player> detectives) {
			final List<Integer> duplicates = new ArrayList<>(); /*list of duplicates*/
			final Set<Integer> locationSet = new HashSet<> (); /*allows the use of contains to check for duplicates*/

			for (Player detective: detectives) { /*loops through all detectives*/
				if (locationSet.contains(detective.location())) { /*contains returns true if item exists*/
					duplicates.add(detective.location());
				}
				else {
					locationSet.add(detective.location()); /*adds new items to the set*/
				}

			}
			return duplicates.isEmpty();
		}

		private static Set<Move.SingleMove> makeSingleMoves(GameSetup setup, List<Player> detectives, Player player, int source){

			final Set<Move.SingleMove> singleMoves = new HashSet<> ();
			boolean temp = true;/*used to check whether a detective is at the location yet*/

			for(int destination : setup.graph.adjacentNodes(source)) {
				for (Player detective: detectives) {
					if (detective.location() == destination) {
						temp = false;
						break;
					}
				}
				if (temp) {
					for (Transport t : setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of())) {
						if (player.has(t.requiredTicket())) {
							Move.SingleMove newMove = new Move.SingleMove(player.piece(), source, t.requiredTicket(), destination);
							singleMoves.add(newMove);
						}
					}
				}
						// TODO find out if the player has the required tickets
						//  if it does, construct a SingleMove and add it the collection of moves to return
				else
					temp = true;
				// TODO consider the rules of secret moves here
				//  add moves to the destination via a secret ticket if there are any left with the player
			}

			// TODO return the collection of moves
			return null;
		}
		private MyGameState(
				final GameSetup setup,
				final ImmutableSet<Piece> remaining,
				final ImmutableList<LogEntry> log,
				final Player mrX,
				final List<Player> detectives){

			this.setup = setup;
			this.remaining = remaining;
			this.log = log;
			this.mrX = mrX;
			this.detectives = detectives;
			/*this.winner = winner;how do we check if winner is empty, error produced apparently its always null*/

			if (setup == null || remaining == null || log == null || setup.moves.isEmpty()|| setup.graph.nodes().isEmpty()||!findPieceDuplicates(detectives) || !findLocationDuplicates(detectives)) {
				throw new IllegalArgumentException("Illegal input");
			}


			if (mrX == null || detectives == null) {
				throw new NullPointerException("Players cannot be null");
			}

			for (Player detective : detectives)
				if (detective.has (Ticket.DOUBLE) || detective.has (Ticket.SECRET) || detective.isMrX()) {
					throw new IllegalArgumentException("illegal detective tickets");
				}
		}

		@Nonnull
		@Override public GameSetup getSetup() {return setup;}
		@Nonnull
		@Override  public ImmutableSet<Piece> getPlayers() {
			Set<Piece> players = new HashSet<>();
			players.add(mrX.piece());
			for (Player detective : detectives)
				players.add(detective.piece());
			return ImmutableSet.copyOf(players);
		}

		@Nonnull
		@Override
		public Optional<Integer> getDetectiveLocation(Detective detective) {
			for (Player pDetective : detectives) /*compares each detective player to given detective value*/
				if (pDetective.piece() == detective) return Optional.of(pDetective.location());
			/*which then allows it to return the location using the player class*/
			return Optional.empty();
		}

		@Nonnull
		@Override /*how to return ticket board type?*/
		public Optional<TicketBoard> getPlayerTickets(Piece piece) {
			if (mrX.piece() == piece) {
				return null;
			} else {
				for (Player Detective : detectives)
					if (Detective.piece() == piece) return null;
			}
			return Optional.empty();
		}

		@Nonnull
		@Override
		public ImmutableList<LogEntry> getMrXTravelLog() {return log;}

		@Nonnull
		@Override
		public ImmutableSet<Piece> getWinner() {
			return winner;
		}
		@Nonnull
		@Override
		public ImmutableSet<Move> getAvailableMoves() {
			return null;
		}

		@Nonnull
		@Override
		public GameState advance(Move move) { return null; }
	}

	@Nonnull
	@Override
	public GameState build(GameSetup setup, Player mrX, ImmutableList<Player> detectives) {
		return new MyGameState(setup, ImmutableSet.of(MrX.MRX), ImmutableList.of(), mrX, detectives);
	}
}
