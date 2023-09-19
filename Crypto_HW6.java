import java.util.ArrayList;

public class Crypto_HW6 {

    // Code looks for duplicate values, which invalidate an alpha candidate
    public static boolean AlphaTester(int i, int input_prime) {
        boolean areDuplicates;
        int holder = 0;
        int flagValue = BigExponent(i, 1, input_prime);

        for (int j = 0; j < input_prime - 1; j++) {
            if (BigExponent(i, j, input_prime) == flagValue) {
                holder++;
            }
        }

        return (holder > 1);
    }

    // Finds all non repeating values for a given modulo, meaing all co primes
    public static void AlphaFinder(int input_prime, ArrayList<Integer> returnal) {
        boolean areDuplicates;

        for (int i = 2; i < input_prime; i++) {
            areDuplicates = AlphaTester(i, input_prime);

            // if no duplicate values are found by the AlphaTester function,
            // the alpha candidate is coprime, and added to the array
            if (areDuplicates == false) {
                returnal.add(i);
            }
        }
    }

    // It is necessary that modulo be taken after every pass or the numbers overflow
    // their memory
    public static int BigExponent(int operand, int exponent, int modulous) {
        int returnal = 1;

        for (int i = 0; i < exponent; i++) {
            returnal *= operand;
            returnal = returnal % modulous;
        }

        return returnal;
    }

    public static int KmFinder(int beta, int i, int ke, int d, int prime_p) {
        int returnal = 0;

        int km_alice = BigExponent(beta, i, prime_p);
        System.out.println("    The master key is " + km_alice);

        int km_bob = BigExponent(ke, d, prime_p);

        if (km_alice != km_bob) {
            System.out.println("Fatal Error: Master Keys not synchronized");
            returnal = 0;
        } else {
            returnal = km_bob;
            System.out.println("Bob also thinks that the master key is " + km_bob);
        }

        return returnal;
    }

    public static int InverseFinder(int reverse, int prime) {
        int inverse = 0;

        inverse = BigExponent(reverse, (prime - 2), prime);
        // System.out.println("we think the inverse equals " + inverse);

        return inverse;
    }

    public static void main(String[] args) throws Exception {
        int prime_p = 31;
        System.out.print("The public prime is " + prime_p);
        ArrayList<Integer> alphasList = new ArrayList<Integer>();

        AlphaFinder(prime_p, alphasList);
        // alphasList.forEach((n) -> System.out.print(n + " "));

        int alpha = (int) (Math.random() * alphasList.size());
        alpha = alphasList.get(alpha);
        System.out.println("    Alpha is " + alpha);

        int d = (int) (Math.random() * (prime_p - 2)) + 1;
        System.out.println("Bob's secret value d is " + d + "\n");

        int beta = BigExponent(alpha, d, prime_p);
        System.out.println("Hey Alice! p = " + prime_p + ", alpha = " + alpha + ", beta = " + beta + "\n");

        int i = (int) (Math.random() * (prime_p - 2)) + 1;
        System.out.println("Alice's secret value i is " + i);

        int ke = BigExponent(alpha, i, prime_p);
        System.out.print("The ephemeral key is " + ke);

        int km = KmFinder(beta, i, ke, d, prime_p);

        int plainText = 26;
        System.out.print("The plain text = " + plainText);

        int cypherText = (plainText * km) % prime_p;
        System.out.println("    The cypher text = " + cypherText);

        int inverse = InverseFinder(km, prime_p);
        int testHolder = (km * inverse) % prime_p;

        if (inverse == 0 || testHolder != 1) {
            System.out.println("Fatal Error: inverse is incorrect");
            return;
        } else {
            System.out.println("The inverse is " + inverse + "\n");
        }

        System.out.println("Hey Bob! The cypher text = " + cypherText + ", the ephemeral key = " + ke + "\n");

        int decodedText = (cypherText * inverse) % prime_p;

        System.out.println("Hey Alice, did you say " + decodedText + "?");
    }
}