package by.maxi.blockchain.ark;

/**
 * Created by Max on 02.11.2017.
 */
public class Utils {

    public static final long SATOSHI_PER_ARK = 100000000L;

    public static long toArk(long satoshi) {
        return satoshi / SATOSHI_PER_ARK;
    }

    public static long toKiloArk(long satoshi) {
        return satoshi / (SATOSHI_PER_ARK * 1000);
    }

    public static long toSatoshi(long ark) {
        return ark * SATOSHI_PER_ARK;
    }
}
