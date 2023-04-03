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
						temp = false;/*item is not added to the set*/
						break;
					}
				}
				if (temp) {
					for (Transport t : Objects.requireNonNull(setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of()))) {
						if (player.has(t.requiredTicket())) {
							Move.SingleMove newMove = new Move.SingleMove(player.piece(), source, t.requiredTicket(), destination);
							singleMoves.add(newMove);
						}

						if (player.has(Ticket.SECRET)) {
							Move.SingleMove newMove = new Move.SingleMove(player.piece(), source, Ticket.SECRET, destination);
							singleMoves.add(newMove);
						}
					}
				}
				temp = true;
			}
			return singleMoves;
		}

		private static Set<Move.DoubleMove> makeDoubleMoves(GameSetup setup, List<Player> detectives, Player player, int source){
			final Set<Move.DoubleMove> doubleMoves = new HashSet<> ();
			boolean temp = true;

			for(int destination : setup.graph.adjacentNodes(source)) {
				for (int destination2 : setup.graph.adjacentNodes(destination)) {
					for (Player detective : detectives) {
						if (detective.location() == destination || detective.location() == destination2) {
							temp = false;/*item is not added to the set*/
							break;
						}
					}
					if (temp) {
						for (Transport t : Objects.requireNonNull(setup.graph.edgeValueOrDefault(source, destination, ImmutableSet.of()))) {
							for (Transport t2 : Objects.requireNonNull(setup.graph.edgeValueOrDefault(destination, destination2, ImmutableSet.of()))) {
								if (player.has(t.requiredTicket()) && player.has(t2.requiredTicket())) {
									if (t != t2 || player.hasAtLeast(t.requiredTicket(), 2)) {
										Move.DoubleMove newMove = new Move.DoubleMove(player.piece(), source, t.requiredTicket(), destination, t2.requiredTicket(), destination2);
										doubleMoves.add(newMove);
									}
								}

								if (player.has(Ticket.SECRET)) {
									if (player.hasAtLeast(Ticket.SECRET, 2)) {
										Move.DoubleMove newMove = new Move.DoubleMove(player.piece(), source, Ticket.SECRET, destination, Ticket.SECRET, destination2);
										doubleMoves.add(newMove);
									}
									if (player.has(t2.requiredTicket())) {
										Move.DoubleMove newMove = new Move.DoubleMove(player.piece(), source, Ticket.SECRET, destination, t2.requiredTicket(), destination2);
										doubleMoves.add(newMove);
									}
									if (player.has(t.requiredTicket())) {
											Move.DoubleMove newMove = new Move.DoubleMove(player.piece(), source, t.requiredTicket(), destination, Ticket.SECRET, destination2);
											doubleMoves.add(newMove);
									}
								}
							}
						}
					}
					temp = true;
				}
			}
			return doubleMoves;
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
			this.winner = ImmutableSet.of();
			this.moves = ImmutableSet.of();

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
			Set<Move> allMoves = new HashSet<>(Set.of());
			if (remaining.contains(mrX.piece())) {
				allMoves.addAll(makeSingleMoves(setup, detectives, mrX, mrX.location()));
				if (mrX.has(Ticket.DOUBLE) && setup.moves.size() > 1)
					allMoves.addAll(makeDoubleMoves(setup, detectives, mrX, mrX.location()));

			}
			else {
				for (Piece item : remaining) {
					for (Player Detective : detectives) {
						if (item == Detective.piece()) {
							allMoves.addAll(makeSingleMoves(setup, detectives, Detective, Detective.location()));
						}
					}
				}
			}
			return ImmutableSet.copyOf(allMoves);
		}

		@Nonnull
		@Override
		public GameState advance(Move move) {
			moves = getAvailableMoves();
			if(!moves.contains(move)) throw new IllegalArgumentException("Illegal move: "+move);
			//using an anonymous inner class

			//TODO the immutable sets are causing problems - i.e. log and remaining
			move.accept(new Move.Visitor() {
				@Override
				public Object visit(Move.SingleMove move) {
					Set<Piece> newRemaining = new HashSet<>(Set.copyOf(remaining));
					List<LogEntry> newLog = new ArrayList<>(List.copyOf(log));
					if (move.commencedBy() == mrX.piece()) {
						mrX.use(move.ticket);
						mrX.at(move.destination);
						newRemaining.remove(mrX.piece());
						if (Set.of(3, 8, 13, 18, 24).contains(log.size()+1)) {
							newLog.add(LogEntry.reveal(move.ticket, move.destination));
						}
						else {
							newLog.add(LogEntry.hidden(move.ticket));
						}
					}
					else {
						for (Player Detective : detectives)
							if (Detective.piece() == move.commencedBy()) {
								Detective.use(move.ticket);
								Detective.at(move.destination);
								mrX.give(move.ticket);
								newRemaining.remove(Detective.piece());
								if (newRemaining.isEmpty()) {
									newRemaining = getPlayers();
								}
							}
					}
					//TODO return the new game state
					remaining = ImmutableSet.copyOf(newRemaining);
					log = ImmutableList.copyOf(newLog);
					//MyGameState newGameState = new MyGameState(setup, Remaining, Log, mrX, detectives);
					return null;
				}

				@Override
				public Object visit(Move.DoubleMove move) {
					Set<Piece> newRemaining = new HashSet<>(Set.copyOf(remaining));
					mrX.use(move.ticket1);
					mrX.use(move.ticket2);
					mrX.at(move.destination2);
					newRemaining.remove(mrX.piece());
					remaining = ImmutableSet.copyOf(newRemaining);
					//MyGameState newGameState =  new MyGameState(setup, Remaining, log, mrX, detectives);
					return null;
				}
			});

		return new MyGameState(setup, remaining, log, mrX, detectives);
		}
	}

	@Nonnull
	@Override
	public GameState build(GameSetup setup, Player mrX, ImmutableList<Player> detectives) {
		return new MyGameState(setup, ImmutableSet.of(MrX.MRX), ImmutableList.of(), mrX, detectives);
	}
}
