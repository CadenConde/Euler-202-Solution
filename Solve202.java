/*
 * EXPLAINATION: Instead of thinking of reflections as bouncing around a
 * single triangle, it is easier to think of it as a single line
 * shooting through a grid of triangles. (like shooting a laser at a
 * mirror, instead of trying to think of how the laser will reflect,
 * just shoot it at the object you want to hit's image in the mirror/
 * think of mirror as window into other room.)
 *
 * Once you visualize the problem like this, you are not longer solving
 * a reflection problem, you are solving a problem about a line
 * intersecting vertices.
 *
 * The number of reflections you want essentially tells you the "row" of
 * the graph, but to get that number, you need to take (bounces + 3) /
 * 2.
 *
 * After you get the row, you have a series of exit points which your
 * line could intersect, but some will hit another vertex before
 * escaping. The way you find how many vertices will be the first chance
 * a laser on a given line to escape has to do with prime number factors
 * or the row #. Euler's Totient Function turns out to be perfect for
 * this case, which is why it appears in the formula. The Totient
 * function gets our answer fairly close, but we need some slight
 * adjustments to avoid double/triple counting
 */
public class euler202 {
    public static void main(String[] args) {
        final long bounces = 12017639147L; // or 1000001 for provided test case
        long row = (bounces + 3) / 2; // convert to row of grid

        long phi = phi(row)[0];
        long numPrimeFactors = phi(row)[1];

        long ans = (long) (phi - Math.pow(2, numPrimeFactors)) / 3;
        System.out.println(ans);
    }

    //Euler's Totient function (phi function) (returns # of relative primes)
    //returns array [phi(n), uniquePrimeFactorsOfN]
    //solution based on "Sieve of Eratosthenes"
    static long[] phi(long n) {
        long phiOut = n;
        long primesDiv = 0;
        if (n % 2 == 0) {
            //bit shifts for faster /= 2
            phiOut = phiOut >> 1;
            primesDiv++;
            while (n % 2 == 0) {
                n = n >> 1;
            }
        }
        for (int i = 3; i <= n; i += 2) {
            if (n % i == 0 && isPrime(i)) {
                phiOut = (phiOut * (i - 1)) / i;
                primesDiv++;
                while (n % i == 0) {
                    n /= i;
                }
            }
        }
        return new long[] { phiOut, primesDiv };
    }

    //Fast Prime Checker
    static boolean isPrime(long primeCand) {
        if (primeCand == 2 || primeCand == 3) {
            return true;
        } else if (primeCand % 2 == 0 || primeCand % 3 == 0) {
            return false;
        } else {
            for (long i = 1; (i * 6) - 1 <= Math.ceil(Math.sqrt(primeCand)); i++) {
                if (primeCand % ((i * 6) - 1) == 0 || primeCand % ((i * 6) + 1) == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
