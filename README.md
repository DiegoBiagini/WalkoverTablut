# WalkoverTablut
This project was developed as an optional challenge for the course **Fundamentals of artificial intelligence and knowledge representation**, by Diego Biagini and Ildebrando Simeoni.

The name of our team was Walkover.

## The challenge
This challenge consisted in developing a program (no programming language restriction was enforced) able to play a variant of an old northern European board game, called **Ashton Tablut**.

The program would have to communicate with a server (developed by the organizers) both to receive the current state of the game and send the individual moves that it wants to perform.

The administrators of the challenge would then go on to pit each of the challenger's programs against each other, like in some kind of tournament.

## Our solution
The program was developed in Java.


We opted for a simple solution to tackle this problem, which consisted in using the classic adversarial search algorithm Minimax with alpha-beta pruning.
In addition to this we decided to use iterative deepening as to explore as much of the tree as possible before the time allocated for the player runs out.


Another refinement that we implemented is the use of **Transposition tables**, a way to keep in memory the game states that we already searched during the exploration of the game tree.

Using transposition tables and iterative deepening allowed us to decide on a preferential move ordering, since now we can track the states we have explored in the previous shallow searches.


As for the heuristic functions we decided to use almost the same sets of heuristics for our black and white player which are mainly:
- The piece advantage
- How many enemies and allies are surrounding the king
- The king's position
- How free is the main center cross of the board
However we decided on different weights of each heuristic for the 2 different colors.


For more information, especially in regards to the heuristics, read the presentation.
## Executing the player
