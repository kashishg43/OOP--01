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
			if (duplicates.isEmpty()) return true;
			else return false;
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
			if (duplicates.isEmpty()) return true;
			else return false;
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

			if (setup == null || remaining == null || log == null || setup.moves.isEmpty() || !findPieceDuplicates(detectives) || !findLocationDuplicates(detectives)) {
				throw new IllegalArgumentException("Illegal input");
			}


			if (mrX == null || detectives == null) {
				throw new NullPointerException("Players cannot be null");
			}

			for (Player detective : detectives)
				if (detective.has (Ticket.DOUBLE) || detective.has (Ticket.SECRET)) {
					throw new IllegalArgumentException("illegal detective tickets");
				}

		}


		@Override public GameSetup getSetup() {
			this.setup = setup;
			/*this.mrX = mrX;
			this.detectives = detectives;*/
			return setup;
		}
		@Override  public ImmutableSet<Piece> getPlayers() {
			List<Piece> players = new ArrayList<>();
			players.add(mrX.piece());
			for (Player detective : detectives)
				players.add(detective.piece());
			return (ImmutableSet<Piece>) Collections.unmodifiableList(players);
		}

		@Nonnull
		@Override
		public Optional<Integer> getDetectiveLocation(Detective detective) {
			/*for (Piece.Detective detective : detectives)*/
			return null;
		}

		@Nonnull
		@Override
		public Optional<TicketBoard> getPlayerTickets(Piece piece) {
			return null;
		}

		@Nonnull
		@Override
		public ImmutableList<LogEntry> getMrXTravelLog() {
			return null;
		}


		@Override
		public ImmutableSet<Piece> getWinner() {
			return winner;
		}

		@Nonnull
		@Override
		public ImmutableSet<Move> getAvailableMoves() {
			return null;
		}

		@Override public GameState advance(Move move) {  return null;  }
	}

	@Nonnull
	@Override
	public GameState build(GameSetup setup, Player mrX, ImmutableList<Player> detectives) {
		return new MyGameState(setup, ImmutableSet.of(MrX.MRX), ImmutableList.of(), mrX, detectives);
	}
}
