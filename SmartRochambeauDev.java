import java.util.Scanner;

public class SmartRochambeauDev
{
   public static void main(String[] args)
   {
      RochambeauAI ai = new RochambeauAI();
      Scanner in = new Scanner(System.in);
      String pThrow = "";
      int pWins = 0, cWins = 0;

      do {
         System.out.println("r, p, s, q?");
         pThrow = expandString(in.nextLine());

         if (!pThrow.equals("q")) {
            String cThrow = ai.getThrow();

            String roundRes = result(pThrow, cThrow);

            if (roundRes.equals("Win")) {
               ++pWins;
            }
            else if (roundRes.equals("Lose")) {
               ++cWins;
            }

            ai.addThrow(RochambeauAI.IState.valueOf(pThrow),
                        RochambeauAI.RState.valueOf(pThrow + roundRes));

            System.out.println(ai + "\n\n" + cThrow + " => " + roundRes + "! Score - " + pWins + ":" + cWins);
         }
      } while (!pThrow.equals("q"));
   }

   // Strings will be the same as IState.
   public static String result(String pThrow, String cThrow)
   {
      if (pThrow.charAt(0) == cThrow.charAt(0)) {
         return "Draw";
      }
      if (pThrow.charAt(0) == 'R' && cThrow.charAt(0) == 'S'
          || pThrow.charAt(0) == 'P' && cThrow.charAt(0) == 'R'
          || pThrow.charAt(0) == 'S' && cThrow.charAt(0) == 'P') {
         return "Win";
      }
      return "Lose";
   }

   public static String expandString(String s)
   {
      if (s.equals("p"))
         s = "Paper";
      if (s.equals("s"))
         s = "Scissors";
      if (s.equals("r"))
         s = "Rock";

      return s;
   }
}
