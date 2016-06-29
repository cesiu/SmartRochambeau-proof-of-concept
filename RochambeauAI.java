/**
 * Generates rock-paper-scissors throws based on a Markov chain.
 * @author Christopher Siu (cesiu)
 * @version 24 Jun 2016
 */

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Arrays;

public class RochambeauAI
{
   // the possible states
   public static enum RState {RockWin, RockLose, RockDraw, PaperWin, PaperLose,
                       PaperDraw, ScissorsWin, ScissorsLose, ScissorsDraw};
   // the possible intermediate states
   public static enum IState {Rock, Paper, Scissors};
   // map of states to state objects
   private LinkedHashMap<RState, ThrowState> states;
   // the current state 
   private ThrowState curState;
   // a random number generator to generate name lengths
   private Random rand;

   /**
    * Constructs an AI.
    */
   public RochambeauAI()
   {
      rand = new Random();
      initGen();
   }

   /**
    * Constructs a seeded AI.
    * @param seed the seed for the random number generator
    */
   public RochambeauAI(long seed)
   {
      rand = new Random(seed);
      initGen();
   }

   /**
    * Initializes an AI.
    */
   public void initGen()
   {
      // Populate the map
      states = new LinkedHashMap<RState, ThrowState>();
      for (RState s : RState.values()) {
         states.put(s, new ThrowState(s, rand));
      }
      
      LinkedHashMap<IState, Integer> initState; 
      // For every state do:
      for (ThrowState tempState : states.values()) {
         // Create initial states with an equal chance of picking each next 
         // option.
         initState = new LinkedHashMap<IState, Integer>();
        
         for (IState s : IState.values()) {
            initState.put(s, 1);
         }
         tempState.setTotal(IState.values().length);
         tempState.setMap(initState);
      }

      // The beginning state has an equal chance of picking any letter.
      curState = new ThrowState(null, rand);
      initState = new LinkedHashMap<IState, Integer>();
      for (IState s : IState.values()) {
         initState.put(s, 1);
      }
      curState.setTotal(IState.values().length);
      curState.setMap(initState);
   }

   /**
    * Adds a throw to the chain.
    * @param next the response to the last throw
    * @param newThrow the throw to be added
    */
   public void addThrow(IState next, RState newThrow)
   {
      // Tell the current throw what came after it.
      curState.addNext(next);
      // Set the current throw to the new throw.
      curState = states.get(newThrow);
   }

   /**
    * Generates a throw from the chain.
    * @return the generated throw
    */
   public String getThrow()
   {
      return curState.nextThrow();
   }

   /**
    * Returns a string representation of all the states.
    * @return the string
    */
   public String toString()
   {
      String retStr = "";

      for (ThrowState state : states.values()) {
         retStr += state.toString();
      }

      return retStr;
   }

   /**
    * Represents one throw state.
    */
   private class ThrowState
   {
      // the throw represented by this state
      RState curThrow;
      // the total number of next states seen
      int total;
      // a map of next states to their quantities
      LinkedHashMap<IState, Integer> nexts;
      // a random number generator for picking the next state
      Random rand;

      /**
       * Constructs a state representing a throw.
       * @param curThrow the throw 
       * @param rand a random number generator
       */
      private ThrowState(RState curThrow, Random rand)
      {  
         this.curThrow = curThrow;
         this.rand = rand;
      }

      /**
       * Gets the throw represented by this state.
       * @return the throw
       */
      private RState getThrow()
      {
         return curThrow;
      }

      /**
       * Generates the throw that beats the one represented by a state.
       * @param predict the state to beat
       * @return the throw
       */
      private String genThrow(IState predict)
      {
         String temp = predict.name();

         return temp.charAt(0) == 'R' ? "Paper" : temp.charAt(0) == 'P'
                ? "Scissors" : "Rock";
      }

      /**
       * Sets the total number of next states seen.
       * @param total the new total
       */
      private void setTotal(int total)
      {
         this.total = total;
      }

      /**
       * Sets the map of states to quantities.
       * @param nexts the new map
       */
      private void setMap(LinkedHashMap<IState, Integer> nexts)
      {
         this.nexts = nexts;
      }

      /**
       * Returns a Rochambeau throw.
       * @return the next throw.
       */
      private String nextThrow()
      {
         // Generate a random number within the total number of states seen.
         int choice = rand.nextInt(total);
         int chance = 0;
         IState predict = null;

         // For each state in the map do:
         for (IState rNext : nexts.keySet()) {
            // Add the chance of that state.
            chance += nexts.get(rNext);
            // If the choice is within the chance, then return the state.
            if (choice < chance) {
               predict = rNext;
               break;
            }
         }
         
         // If we haven't made a prediction, we generated a number too large.
         if (predict == null) {
            throw new RuntimeException("Error: " + curThrow + " generated "
               + choice + " out of " + total + ".");
         }

         // Return the throw that beats the prediction.
         return genThrow(predict);
      }

      /**
       * Indicates that a new next state has been seen.
       * @param newNext the state that was seen
       */
      private void addNext(IState newNext)
      {
         // Increment that state's quantity and the total.
         nexts.put(newNext, nexts.get(newNext) + 1);
         ++total;
      }

      /**
       * Returns a string representation of this state and its nexts.
       * @return the string
       */
      public String toString() {
         String retStr = "" + curThrow.name() + ":\n   ";

         for (IState state : nexts.keySet()) {
            retStr += "[" + state.name() + ":" + nexts.get(state) + "]";
         }

         return retStr + "\n";
      }
   }
}
