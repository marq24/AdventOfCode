package de.emac.aoc;

import java.math.BigInteger;

public class LongFrac extends Number implements Comparable<LongFrac> {

    /**
     * Required for serialization support. Lang version 2.0.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 65382027393091L;

    /**
     * {@code LongFrac} representation of 0.
     */
    public static final LongFrac ZERO = new LongFrac(0, 1);
    /**
     * {@code LongFrac} representation of 1.
     */
    public static final LongFrac ONE = new LongFrac(1, 1);
    /**
     * {@code LongFrac} representation of 1/2.
     */
    public static final LongFrac ONE_HALF = new LongFrac(1, 2);
    /**
     * {@code LongFrac} representation of 1/3.
     */
    public static final LongFrac ONE_THIRD = new LongFrac(1, 3);
    /**
     * {@code LongFrac} representation of 2/3.
     */
    public static final LongFrac TWO_THIRDS = new LongFrac(2, 3);
    /**
     * {@code LongFrac} representation of 1/4.
     */
    public static final LongFrac ONE_QUARTER = new LongFrac(1, 4);
    /**
     * {@code LongFrac} representation of 2/4.
     */
    public static final LongFrac TWO_QUARTERS = new LongFrac(2, 4);
    /**
     * {@code LongFrac} representation of 3/4.
     */
    public static final LongFrac THREE_QUARTERS = new LongFrac(3, 4);
    /**
     * {@code LongFrac} representation of 1/5.
     */
    public static final LongFrac ONE_FIFTH = new LongFrac(1, 5);
    /**
     * {@code LongFrac} representation of 2/5.
     */
    public static final LongFrac TWO_FIFTHS = new LongFrac(2, 5);
    /**
     * {@code LongFrac} representation of 3/5.
     */
    public static final LongFrac THREE_FIFTHS = new LongFrac(3, 5);
    /**
     * {@code LongFrac} representation of 4/5.
     */
    public static final LongFrac FOUR_FIFTHS = new LongFrac(4, 5);


    /**
     * The numerator number part of the LongFrac (the three in three sevenths).
     */
    private final long numerator;
    /**
     * The denominator number part of the LongFrac (the seven in three sevenths).
     */
    private final long denominator;

    /**
     * Cached output hashCode (class is immutable).
     */
    private transient int hashCode;
    /**
     * Cached output toString (class is immutable).
     */
    private transient String toString;
    /**
     * Cached output toProperString (class is immutable).
     */
    private transient String toProperString;

    /**
     * <p>Constructs a {@code LongFrac} instance with the 2 parts
     * of a LongFrac Y/Z.</p>
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     */
    private LongFrac(final long numerator, final long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * <p>Creates a {@code LongFrac} instance with the 2 parts
     * of a LongFrac Y/Z.</p>
     *
     * <p>Any negative signs are resolved to be on the numerator.</p>
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     * @return a new LongFrac instance
     * @throws ArithmeticException if the denominator is {@code zero}
     * or the denominator is {@code negative} and the numerator is {@code Integer#MIN_VALUE}
     */
    public static LongFrac getLongFrac(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            if (numerator == Long.MIN_VALUE || denominator == Long.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        return new LongFrac(numerator, denominator);
    }

    /**
     * <p>Creates a {@code LongFrac} instance with the 3 parts
     * of a LongFrac X Y/Z.</p>
     *
     * <p>The negative sign must be passed in on the whole number part.</p>
     *
     * @param whole  the whole number, for example the one in 'one and three sevenths'
     * @param numerator  the numerator, for example the three in 'one and three sevenths'
     * @param denominator  the denominator, for example the seven in 'one and three sevenths'
     * @return a new LongFrac instance
     * @throws ArithmeticException if the denominator is {@code zero}
     * @throws ArithmeticException if the denominator is negative
     * @throws ArithmeticException if the numerator is negative
     * @throws ArithmeticException if the resulting numerator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public static LongFrac getLongFrac(final int whole, final int numerator, final int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        }
        if (numerator < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        }
        final long numeratorValue;
        if (whole < 0) {
            numeratorValue = whole * (long) denominator - numerator;
        } else {
            numeratorValue = whole * (long) denominator + numerator;
        }
        if (numeratorValue < Integer.MIN_VALUE || numeratorValue > Integer.MAX_VALUE) {
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
        return new LongFrac((int) numeratorValue, denominator);
    }

    /**
     * <p>Creates a reduced {@code LongFrac} instance with the 2 parts
     * of a LongFrac Y/Z.</p>
     *
     * <p>For example, if the input parameters represent 2/4, then the created
     * LongFrac will be 1/2.</p>
     *
     * <p>Any negative signs are resolved to be on the numerator.</p>
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     * @return a new LongFrac instance, with the numerator and denominator reduced
     * @throws ArithmeticException if the denominator is {@code zero}
     */
    public static LongFrac getReducedLongFrac(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (numerator == 0) {
            return ZERO; // normalize zero.
        }
        // allow 2^k/-2^31 as a valid LongFrac (where k>0)
        if (denominator == Long.MIN_VALUE && (numerator & 1) == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0) {
            if (numerator == Long.MIN_VALUE || denominator == Long.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        // simplify LongFrac.
        final long gcd = greatestCommonDivisor(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return new LongFrac(numerator, denominator);
    }

    /**
     * <p>Creates a {@code LongFrac} instance from a {@code double} value.</p>
     *
     * <p>This method uses the <a href="http://archives.math.utk.edu/articles/atuyl/confrac/">
     *  continued LongFrac algorithm</a>, computing a maximum of
     *  25 convergents and bounding the denominator by 10,000.</p>
     *
     * @param value  the double value to convert
     * @return a new LongFrac instance that is close to the value
     * @throws ArithmeticException if {@code |value| &gt; Integer.MAX_VALUE}
     *  or {@code value = NaN}
     * @throws ArithmeticException if the calculated denominator is {@code zero}
     * @throws ArithmeticException if the algorithm does not converge
     */
    public static LongFrac getLongFrac(double value) {
        final int sign = value < 0 ? -1 : 1;
        value = Math.abs(value);
        if (value > Integer.MAX_VALUE || Double.isNaN(value)) {
            throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
        }
        final int wholeNumber = (int) value;
        value -= wholeNumber;

        int numer0 = 0; // the pre-previous
        int denom0 = 1; // the pre-previous
        int numer1 = 1; // the previous
        int denom1 = 0; // the previous
        int numer2 = 0; // the current, setup in calculation
        int denom2 = 0; // the current, setup in calculation
        int a1 = (int) value;
        int a2 = 0;
        double x1 = 1;
        double x2 = 0;
        double y1 = value - a1;
        double y2 = 0;
        double delta1, delta2 = Double.MAX_VALUE;
        double LongFrac;
        int i = 1;
        do {
            delta1 = delta2;
            a2 = (int) (x1 / y1);
            x2 = y1;
            y2 = x1 - a2 * y1;
            numer2 = a1 * numer1 + numer0;
            denom2 = a1 * denom1 + denom0;
            LongFrac = (double) numer2 / (double) denom2;
            delta2 = Math.abs(value - LongFrac);
            a1 = a2;
            x1 = x2;
            y1 = y2;
            numer0 = numer1;
            denom0 = denom1;
            numer1 = numer2;
            denom1 = denom2;
            i++;
        } while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
        if (i == 25) {
            throw new ArithmeticException("Unable to convert double to LongFrac");
        }
        return getReducedLongFrac((numer0 + wholeNumber * denom0) * sign, denom0);
    }

    /**
     * <p>Creates a LongFrac from a {@code String}.</p>
     *
     * <p>The formats accepted are:</p>
     *
     * <ol>
     *  <li>{@code double} String containing a dot</li>
     *  <li>'X Y/Z'</li>
     *  <li>'Y/Z'</li>
     *  <li>'X' (a simple whole number)</li>
     * </ol>
     * <p>and a .</p>

    // Accessors
    //-------------------------------------------------------------------

    /**
     * <p>Gets the numerator part of the LongFrac.</p>
     *
     * <p>This method may return a value greater than the denominator, an
     * improper LongFrac, such as the seven in 7/4.</p>
     *
     * @return the numerator LongFrac part
     */
    public long getNumerator() {
        return numerator;
    }

    /**
     * <p>Gets the denominator part of the LongFrac.</p>
     *
     * @return the denominator LongFrac part
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     * <p>Gets the proper numerator, always positive.</p>
     *
     * <p>An improper LongFrac 7/4 can be resolved into a proper one, 1 3/4.
     * This method returns the 3 from the proper LongFrac.</p>
     *
     * <p>If the LongFrac is negative such as -7/4, it can be resolved into
     * -1 3/4, so this method returns the positive proper numerator, 3.</p>
     *
     * @return the numerator LongFrac part of a proper LongFrac, always positive
     */
    public long getProperNumerator() {
        return Math.abs(numerator % denominator);
    }

    /**
     * <p>Gets the proper whole part of the LongFrac.</p>
     *
     * <p>An improper LongFrac 7/4 can be resolved into a proper one, 1 3/4.
     * This method returns the 1 from the proper LongFrac.</p>
     *
     * <p>If the LongFrac is negative such as -7/4, it can be resolved into
     * -1 3/4, so this method returns the positive whole part -1.</p>
     *
     * @return the whole LongFrac part of a proper LongFrac, that includes the sign
     */
    public long getProperWhole() {
        return numerator / denominator;
    }

    // Number methods
    //-------------------------------------------------------------------

    /**
     * <p>Gets the LongFrac as an {@code int}. This returns the whole number
     * part of the LongFrac.</p>
     *
     * @return the whole number LongFrac part
     */
    @Override
    public int intValue() {
        return (int) (numerator / denominator);
    }

    /**
     * <p>Gets the LongFrac as a {@code long}. This returns the whole number
     * part of the LongFrac.</p>
     *
     * @return the whole number LongFrac part
     */
    @Override
    public long longValue() {
        return numerator / denominator;
    }

    /**
     * <p>Gets the LongFrac as a {@code float}. This calculates the LongFrac
     * as the numerator divided by denominator.</p>
     *
     * @return the LongFrac as a {@code float}
     */
    @Override
    public float floatValue() {
        return (float) (numerator / denominator);
    }

    /**
     * <p>Gets the LongFrac as a {@code double}. This calculates the LongFrac
     * as the numerator divided by denominator.</p>
     *
     * @return the LongFrac as a {@code double}
     */
    @Override
    public double doubleValue() {
        return (double) numerator / (double) denominator;
    }

    // Calculations
    //-------------------------------------------------------------------

    /**
     * <p>Reduce the LongFrac to the smallest values for the numerator and
     * denominator, returning the result.</p>
     *
     * <p>For example, if this LongFrac represents 2/4, then the result
     * will be 1/2.</p>
     *
     * @return a new reduced LongFrac instance, or this if no simplification possible
     */
    public LongFrac reduce() {
        if (numerator == 0) {
            return equals(ZERO) ? this : ZERO;
        }
        final long gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
        if (gcd == 1) {
            return this;
        }
        return getLongFrac(numerator / gcd, denominator / gcd);
    }

    /**
     * <p>Gets a LongFrac that is the inverse (1/LongFrac) of this one.</p>
     *
     * <p>The returned LongFrac is not reduced.</p>
     *
     * @return a new LongFrac instance with the numerator and denominator
     *         inverted.
     * @throws ArithmeticException if the LongFrac represents zero.
     */
    public LongFrac invert() {
        if (numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        }
        if (numerator==Long.MIN_VALUE) {
            throw new ArithmeticException("overflow: can't negate numerator");
        }
        if (numerator<0) {
            return new LongFrac(-denominator, -numerator);
        }
        return new LongFrac(denominator, numerator);
    }

    /**
     * <p>Gets a LongFrac that is the negative (-LongFrac) of this one.</p>
     *
     * <p>The returned LongFrac is not reduced.</p>
     *
     * @return a new LongFrac instance with the opposite signed numerator
     */
    public LongFrac negate() {
        // the positive range is one smaller than the negative range of an int.
        if (numerator==Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: too large to negate");
        }
        return new LongFrac(-numerator, denominator);
    }

    /**
     * <p>Gets a LongFrac that is the positive equivalent of this one.</p>
     * <p>More precisely: {@code (LongFrac &gt;= 0 ? this : -LongFrac)}</p>
     *
     * <p>The returned LongFrac is not reduced.</p>
     *
     * @return {@code this} if it is positive, or a new positive LongFrac
     *  instance with the opposite signed numerator
     */
    public LongFrac abs() {
        if (numerator >= 0) {
            return this;
        }
        return negate();
    }

    /**
     * <p>Gets a LongFrac that is raised to the passed in power.</p>
     *
     * <p>The returned LongFrac is in reduced form.</p>
     *
     * @param power  the power to raise the LongFrac to
     * @return {@code this} if the power is one, {@code ONE} if the power
     * is zero (even if the LongFrac equals ZERO) or a new LongFrac instance
     * raised to the appropriate power
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac pow(final int power) {
        if (power == 1) {
            return this;
        } else if (power == 0) {
            return ONE;
        } else if (power < 0) {
            if (power == Integer.MIN_VALUE) { // MIN_VALUE can't be negated.
                return this.invert().pow(2).pow(-(power / 2));
            }
            return this.invert().pow(-power);
        } else {
            final LongFrac f = this.multiplyBy(this);
            if (power % 2 == 0) { // if even...
                return f.pow(power / 2);
            }
            return f.pow(power / 2).multiplyBy(this);
        }
    }


    private static long greatestCommonDivisor(long a, long b) {
        return b == 0 ? a : greatestCommonDivisor(b, a % b);
    }

    /**
     * <p>Gets the greatest common divisor of the absolute value of
     * two numbers, using the "binary gcd" method which avoids
     * division and modulo operations.  See Knuth 4.5.2 algorithm B.
     * This algorithm is due to Josef Stein (1961).</p>
     *
     * @param u  a non-zero number
     * @param v  a non-zero number
     * @return the greatest common divisor, never zero
     */
    private static long greatestCommonDivisorX(int u, int v) {
        // From Commons Math:
        if (u == 0 || v == 0) {
            if (u == Long.MIN_VALUE || v == Long.MIN_VALUE) {
                throw new ArithmeticException("overflow: gcd is 2^31");
            }
            return Math.abs(u) + Math.abs(v);
        }
        // if either operand is abs 1, return 1:
        if (Math.abs(u) == 1 || Math.abs(v) == 1) {
            return 1;
        }
        // keep u and v negative, as negative integers range down to
        // -2^31, while positive numbers can only be as large as 2^31-1
        // (i.e. we can't necessarily negate a negative number without
        // overflow)
        if (u > 0) {
            u = -u;
        } // make u negative
        if (v > 0) {
            v = -v;
        } // make v negative
        // B1. [Find power of 2]
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
            u /= 2;
            v /= 2;
            k++; // cast out twos.
        }
        if (k == 31) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        // B2. Initialize: u and v have been divided by 2^k and at least
        // one is odd.
        int t = (u & 1) == 1 ? v : -(u / 2)/* B3 */;
        // t negative: u was odd, v may be even (t replaces v)
        // t positive: u was even, v is odd (t replaces u)
        do {
            /* assert u<0 && v<0; */
            // B4/B3: cast out twos from t.
            while ((t & 1) == 0) { // while t is even..
                t /= 2; // cast out twos
            }
            // B5 [reset max(u,v)]
            if (t > 0) {
                u = -t;
            } else {
                v = t;
            }
            // B6/B3. at this point both u and v should be odd.
            t = (v - u) / 2;
            // |u| larger: t positive (replace u)
            // |v| larger: t negative (replace v)
        } while (t != 0);
        return -u * (1 << k); // gcd is u*2^k
    }

    // Arithmetic
    //-------------------------------------------------------------------

    /**
     * Multiply two integers, checking for overflow.
     *
     * @param x a factor
     * @param y a factor
     * @return the product {@code x*y}
     * @throws ArithmeticException if the result can not be represented as
     *                             an int
     */
    private static long mulAndCheck(final long x, final long y) {
        final long m =  x * y;
        if (m < Long.MIN_VALUE || m > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: mul");
        }
        return (long) m;
    }

    /**
     *  Multiply two non-negative integers, checking for overflow.
     *
     * @param x a non-negative factor
     * @param y a non-negative factor
     * @return the product {@code x*y}
     * @throws ArithmeticException if the result can not be represented as
     * an int
     */
    private static long mulPosAndCheck(final long x, final long y) {
        /* assert x>=0 && y>=0; */
        final long m = (long) x * (long) y;
        if (m > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: mulPos");
        }
        return (long) m;
    }

    /**
     * Add two integers, checking for overflow.
     *
     * @param x an addend
     * @param y an addend
     * @return the sum {@code x+y}
     * @throws ArithmeticException if the result can not be represented as
     * an int
     */
    private static long addAndCheck(final long x, final long y) {
        final long s = (long) x + (long) y;
        if (s < Long.MIN_VALUE || s > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return s;
    }

    /**
     * Subtract two integers, checking for overflow.
     *
     * @param x the minuend
     * @param y the subtrahend
     * @return the difference {@code x-y}
     * @throws ArithmeticException if the result can not be represented as
     * an int
     */
    private static long subAndCheck(final long x, final long y) {
        final long s = (long) x - (long) y;
        if (s < Long.MIN_VALUE || s > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return (long) s;
    }

    /**
     * <p>Adds the value of this LongFrac to another, returning the result in reduced form.
     * The algorithm follows Knuth, 4.5.1.</p>
     *
     * @param LongFrac  the LongFrac to add, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws IllegalArgumentException if the LongFrac is {@code null}
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac add(final LongFrac LongFrac) {
        return addSub(LongFrac, true /* add */);
    }

    /**
     * <p>Subtracts the value of another LongFrac from the value of this one,
     * returning the result in reduced form.</p>
     *
     * @param LongFrac  the LongFrac to subtract, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws IllegalArgumentException if the LongFrac is {@code null}
     * @throws ArithmeticException if the resulting numerator or denominator
     *   cannot be represented in an {@code int}.
     */
    public LongFrac subtract(final LongFrac LongFrac) {
        return addSub(LongFrac, false /* subtract */);
    }

    /**
     * Implement add and subtract using algorithm described in Knuth 4.5.1.
     *
     * @param LongFrac the LongFrac to subtract, must not be {@code null}
     * @param isAdd true to add, false to subtract
     * @return a {@code LongFrac} instance with the resulting values
     * @throws IllegalArgumentException if the LongFrac is {@code null}
     * @throws ArithmeticException if the resulting numerator or denominator
     *   cannot be represented in an {@code int}.
     */
    private LongFrac addSub(final LongFrac LongFrac, final boolean isAdd) {
        // zero is identity for addition.
        if (numerator == 0) {
            return isAdd ? LongFrac : LongFrac.negate();
        }
        if (LongFrac.numerator == 0) {
            return this;
        }
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        final long d1 = greatestCommonDivisor(denominator, LongFrac.denominator);
        if (d1 == 1) {
            // result is ( (u*v' +/- u'v) / u'v')
            final long uvp = mulAndCheck(numerator, LongFrac.denominator);
            final long upv = mulAndCheck(LongFrac.numerator, denominator);
            return new LongFrac(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(denominator,
                    LongFrac.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7. we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        final BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(LongFrac.denominator / d1));
        final BigInteger upv = BigInteger.valueOf(LongFrac.numerator).multiply(BigInteger.valueOf(denominator / d1));
        final BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        final long tmodd1 = t.mod(BigInteger.valueOf(d1)).longValue();
        final long d2 = tmodd1 == 0 ? d1 : greatestCommonDivisor(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        final BigInteger w = t.divide(BigInteger.valueOf(d2));
        return new LongFrac(w.longValue(), mulPosAndCheck(denominator / d1, LongFrac.denominator / d2));
    }

    /**
     * <p>Multiplies the value of this LongFrac by another, returning the
     * result in reduced form.</p>
     *
     * @param LongFrac  the LongFrac to multiply by, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws NullPointerException if the LongFrac is {@code null}
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac multiplyBy(final LongFrac LongFrac) {
        if (numerator == 0 || LongFrac.numerator == 0) {
            return ZERO;
        }
        // knuth 4.5.1
        // make sure we don't overflow unless the result *must* overflow.
        final long d1 = greatestCommonDivisor(numerator, LongFrac.denominator);
        final long d2 = greatestCommonDivisor(LongFrac.numerator, denominator);
        return getReducedLongFrac(mulAndCheck(numerator / d1, LongFrac.numerator / d2), mulPosAndCheck(denominator / d2, LongFrac.denominator / d1));
    }

    /**
     * <p>Divide the value of this LongFrac by another.</p>
     *
     * @param LongFrac  the LongFrac to divide by, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws NullPointerException if the LongFrac is {@code null}
     * @throws ArithmeticException if the LongFrac to divide by is zero
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac divideBy(final LongFrac LongFrac) {
        if (LongFrac.numerator == 0) {
            throw new ArithmeticException("The LongFrac to divide by must not be zero");
        }
        return multiplyBy(LongFrac.invert());
    }

    // Basics
    //-------------------------------------------------------------------

    /**
     * <p>Compares this LongFrac to another object to test if they are equal.</p>.
     *
     * <p>To be equal, both values must be equal. Thus 2/4 is not equal to 1/2.</p>
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LongFrac)) {
            return false;
        }
        final LongFrac other = (LongFrac) obj;
        return getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator();
    }

    /**
     * <p>Gets a hashCode for the LongFrac.</p>
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {

        if (hashCode == 0) {
            // hash code update should be atomic.
            hashCode = 37 * (37 * 17 + ((int) numerator) + ((int) denominator));
        }
        return hashCode;
    }

    /**
     * <p>Compares this object to another based on size.</p>
     *
     * <p>Note: this class has a natural ordering that is inconsistent
     * with equals, because, for example, equals treats 1/2 and 2/4 as
     * different, whereas compareTo treats them as equal.
     *
     * @param other  the object to compare to
     * @return -1 if this is less, 0 if equal, +1 if greater
     * @throws ClassCastException if the object is not a {@code LongFrac}
     * @throws NullPointerException if the object is {@code null}
     */
    @Override
    public int compareTo(final LongFrac other) {
        if (this == other) {
            return 0;
        }
        if (numerator == other.numerator && denominator == other.denominator) {
            return 0;
        }

        // otherwise see which is less
        final long first = (long) numerator * (long) other.denominator;
        final long second = (long) other.numerator * (long) denominator;
        return Long.compare(first, second);
    }

    /**
     * <p>Gets the LongFrac as a {@code String}.</p>
     *
     * <p>The format used is '<i>numerator</i>/<i>denominator</i>' always.
     *
     * @return a {@code String} form of the LongFrac
     */
    @Override
    public String toString() {
        if (toString == null) {
            toString = getNumerator() + "/" + getDenominator();
        }
        return toString;
    }

    /**
     * <p>Gets the LongFrac as a proper {@code String} in the format X Y/Z.</p>
     *
     * <p>The format used in '<i>wholeNumber</i> <i>numerator</i>/<i>denominator</i>'.
     * If the whole number is zero it will be omitted. If the numerator is zero,
     * only the whole number is returned.</p>
     *
     * @return a {@code String} form of the LongFrac
     */
    public String toProperString() {
        if (toProperString == null) {
            if (numerator == 0) {
                toProperString = "0";
            } else if (numerator == denominator) {
                toProperString = "1";
            } else if (numerator == -1 * denominator) {
                toProperString = "-1";
            } else if ((numerator > 0 ? -numerator : numerator) < -denominator) {
                // note that we do the magnitude comparison test above with
                // NEGATIVE (not positive) numbers, since negative numbers
                // have a larger range. otherwise numerator==Integer.MIN_VALUE
                // is handled incorrectly.
                final long properNumerator = getProperNumerator();
                if (properNumerator == 0) {
                    toProperString = Long.toString(getProperWhole());
                } else {
                    toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
                }
            } else {
                toProperString = getNumerator() + "/" + getDenominator();
            }
        }
        return toProperString;
    }
}

