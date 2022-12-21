package de.emac.aoc;

import java.math.BigInteger;

public class LongFrac implements Comparable<LongFrac> {
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
    protected final long numerator;
    /**
     * The denominator number part of the LongFrac (the seven in three sevenths).
     */
    protected final long denominator;

    /**
     * Cached output hashCode (class is immutable).
     */
    private transient int hashCode;
    /**
     * Cached output toString (class is immutable).
     */
    private transient String toString;

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
    public long getDenominator() {
        return denominator;
    }

    public long longValue() {
        return numerator / denominator;
    }

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

    public LongFrac negate() {
        // the positive range is one smaller than the negative range of an int.
        if (numerator==Long.MIN_VALUE) {
            throw new ArithmeticException("overflow: too large to negate");
        }
        return new LongFrac(-numerator, denominator);
    }

    public LongFrac abs() {
        if (numerator >= 0) {
            return this;
        }
        return negate();
    }

    public LongFrac pow(final int power) {
        if (power == 1) {
            return this;
        } else if (power == 0) {
            return ONE;
        } else if (power < 0) {
            if (power == Long.MIN_VALUE) { // MIN_VALUE can't be negated.
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

    private static long mulAndCheck(final long x, final long y) {
        final long m =  x * y;
        if (m < Long.MIN_VALUE || m > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: mul");
        }
        return m;
    }

    private static long mulPosAndCheck(final long x, final long y) {
        /* assert x>=0 && y>=0; */
        final long m = (long) x * (long) y;
        if (m > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: mulPos");
        }
        return m;
    }

    private static long addAndCheck(final long x, final long y) {
        final long s =  x + y;
        if (s < Long.MIN_VALUE || s > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return s;
    }

    private static long subAndCheck(final long x, final long y) {
        final long s =  x - y;
        if (s < Long.MIN_VALUE || s > Long.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return s;
    }

    public LongFrac add(final LongFrac other) {
        return addSub(other, true /* add */);
    }

    public LongFrac subtract(final LongFrac other) {
        return addSub(other, false /* subtract */);
    }

    private LongFrac addSub(final LongFrac other, final boolean isAdd) {
        // zero is identity for addition.
        if (numerator == 0) {
            return isAdd ? other : other.negate();
        }
        if (other.numerator == 0) {
            return this;
        }
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        final long d1 = greatestCommonDivisor(denominator, other.denominator);
        if (d1 == 1) {
            // result is ( (u*v' +/- u'v) / u'v')
            final long uvp = mulAndCheck(numerator, other.denominator);
            final long upv = mulAndCheck(other.numerator, denominator);
            return new LongFrac(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(denominator, other.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7. we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        final BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(other.denominator / d1));
        final BigInteger upv = BigInteger.valueOf(other.numerator).multiply(BigInteger.valueOf(denominator / d1));
        final BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        final long tmodd1 = t.mod(BigInteger.valueOf(d1)).longValue();
        final long d2 = tmodd1 == 0 ? d1 : greatestCommonDivisor(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        final BigInteger w = t.divide(BigInteger.valueOf(d2));
        return new LongFrac(w.longValue(), mulPosAndCheck(denominator / d1, other.denominator / d2));
    }

    /**
     * <p>Multiplies the value of this LongFrac by another, returning the
     * result in reduced form.</p>
     *
     * @param other  the LongFrac to multiply by, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws NullPointerException if the LongFrac is {@code null}
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac multiplyBy(final LongFrac other) {
        if (numerator == 0 || other.numerator == 0) {
            return ZERO;
        }
        // knuth 4.5.1
        // make sure we don't overflow unless the result *must* overflow.
        final long d1 = greatestCommonDivisor(numerator, other.denominator);
        final long d2 = greatestCommonDivisor(other.numerator, denominator);
        return getReducedLongFrac(mulAndCheck(numerator / d1, other.numerator / d2), mulPosAndCheck(denominator / d2, other.denominator / d1));
    }

    /**
     * <p>Divide the value of this LongFrac by another.</p>
     *
     * @param other  the LongFrac to divide by, must not be {@code null}
     * @return a {@code LongFrac} instance with the resulting values
     * @throws NullPointerException if the LongFrac is {@code null}
     * @throws ArithmeticException if the LongFrac to divide by is zero
     * @throws ArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public LongFrac divideBy(final LongFrac other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("The LongFrac to divide by must not be zero");
        }
        return multiplyBy(other.invert());
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

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            // hash code update should be atomic.
            hashCode = 37 * (37 * 17 + ((int) numerator) + ((int) denominator));
        }
        return hashCode;
    }

    @Override
    public int compareTo(final LongFrac other) {
        if (this == other) {
            return 0;
        }
        if (numerator == other.numerator && denominator == other.denominator) {
            return 0;
        }

        // otherwise see which is less
        final long first = numerator * other.denominator;
        final long second = other.numerator * denominator;
        return Long.compare(first, second);
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = getNumerator() + "/" + getDenominator();
        }
        return toString;
    }
}

