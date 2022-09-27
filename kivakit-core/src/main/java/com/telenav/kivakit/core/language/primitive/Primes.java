////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Prime number allocation sizes and "powers of two" (the next prime above a power of two).
 *
 * <ul>
 *     <li>{@link #isPrime(long)} - Returns true if the given value is prime</li>
 *     <li>{@link #primeAllocationSize(long)} - Returns the next largest prime allocation size</li>
 *     <li>{@link #primePowerOfTwo(int)} - Returns the next prime number after the given power of two</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramPrimitive.class)
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Primes
{
    /**
     * As fate would have it, the largest prime integer value in Java is a Mersenne prime: 2^31 - 1 or 0x80000000 - 1 or
     * 0x7fffffff. This also happens to be the largest integer value.
     */
    public static int LARGEST_INT = 0x7fffffff;

    /**
     * Prime numbers larger than the nearest power of two.
     */
    private static final int[] primePowersOfTwo = { 5, 11, 17, 37, 67, 131, 257, 521, 1031, 2053, 4099, 8209, 16411,
            32771, 65537, 131071, 262147, 524287, 1048573, 2097143, 4194301, 8388617, 16777213, 33554467, 67108859,
            134217757, 268435459, 536870909, 1073741651, 2147483477 };

    private static final long[] SIZES = {
            11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113,
            127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233,
            239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359,
            367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487,
            491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619,
            631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761,
            769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911,
            919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1_999, 3_001, 4_003, 5_009, 6_011, 7_013, 8_017,
            9_029, 10_037, 11_047, 12_049, 13_063, 14_071, 15_073, 16_087, 17_093, 18_097, 19_121, 20_123, 21_139,
            22_147, 23_159, 24_169, 25_171, 26_177, 27_179, 28_181, 29_191, 30_197, 31_219, 32_233, 33_247, 34_253,
            35_257, 36_263, 37_273, 38_281, 39_293, 40_343, 41_351, 42_359, 43_391, 44_417, 45_427, 46_439, 47_441,
            48_449, 49_451, 54_469, 59_471, 64_483, 69_491, 74_507, 79_531, 84_533, 89_561, 94_573, 99_577, 149_579,
            199_583, 249_589, 299_603, 349_637, 399_643, 449_653, 499_661, 549_667, 599_681, 649_697, 699_709, 749_711,
            799_723, 849_727, 899_749, 949_759, 999_763, 1_499_767, 1_999_771, 2_499_779, 2_999_783, 3_499_787,
            3_999_791, 4_499_801, 4_999_823, 5_499_839, 5_999_863, 6_499_879, 6_999_899, 7_499_939, 7_999_963,
            8_499_971, 8_999_981, 9_500_021, 10_000_079, 10_500_089, 11_000_111, 11_500_127, 12_000_133, 12_500_177,
            13_000_187, 13_500_209, 14_000_221, 14_500_223, 15_000_233, 15_500_237, 16_000_253, 16_500_257, 17_000_287,
            17_500_289, 18_000_319, 18_500_327, 19_000_357, 19_500_367, 20_000_377, 20_500_387, 21_000_391, 21_500_429,
            22_000_441, 22_500_449, 23_000_459, 23_500_471, 24_000_517, 24_500_527, 25_000_541, 25_500_557, 26_000_563,
            26_500_571, 27_000_587, 27_500_621, 28_000_691, 28_500_709, 29_000_729, 29_500_799, 30_500_831, 31_500_853,
            32_500_873, 33_500_879, 34_500_889, 35_500_897, 36_500_903, 37_500_923, 38_500_963, 39_500_971, 40_500_983,
            41_501_023, 42_501_047, 43_501_049, 44_501_063, 45_501_089, 46_501_127, 47_501_131, 48_501_149, 49_501_181,
            50_501_219, 51_501_221, 52_501_243, 53_501_251, 54_501_257, 55_501_267, 56_501_293, 57_501_317, 58_501_319,
            59_501_339, 60_501_347, 61_501_351, 62_501_353, 63_501_407, 64_501_427, 65_501_437, 66_501_451, 67_501_453,
            68_501_471, 69_501_479, 70_501_481, 71_501_537, 72_501_553, 73_501_567, 74_501_597, 75_501_619, 76_501_643,
            77_501_653, 78_501_659, 79_501_661, 80_501_683, 81_501_691, 82_501_711, 83_501_729, 84_501_733, 85_501_763,
            86_501_783, 87_501_793, 88_501_811, 89_501_843, 90_501_871, 91_501_877, 92_501_881, 93_501_883, 94_501_889,
            95_501_899, 96_501_901, 97_501_907, 98_501_929, 108_501_961, 118_501_963, 128_501_983, 138_501_991,
            148502047, 158502059, 168502063, 178502087, 188502091, 198502093, 208502111, 218502121, 228502177,
            238502179, 248502181, 258502193, 268502209, 278502221, 288502241, 298502251, 308502277, 318502279,
            328502287, 338502323, 348502327, 358502381, 368502437, 378502441, 388502449, 398502473, 408502481,
            418502521, 428502533, 438502541, 448502557, 458502599, 468502691, 478502711, 573_292_817, 1_164_186_217,
            2_364_114_217L, 4_294_967_291L, 8_589_934_583L,
    };

    /**
     * Returns true if the given number is prime. This is inefficient, but convenient for some uses.
     */
    public static boolean isPrime(long n)
    {
        if (n % 2 == 1)
        {
            for (var a = 3L; a * a <= n; a += 2)
            {
                if (n % a == 0)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args)
    {
        // Compute primes from 11 to 100,000,00. Assumption is that no single hashmap will be larger
        // than this size. This isn't an issue because larger maps are split into multiple maps.

        var primes = new LinkedList<Integer>();
        for (var i = 11; i < 2_000_000_000; i++)
        {
            if (i % 2 != 0 && i % 3 != 0 && i % 5 != 0 && isPrime(i))
            {
                var spacing = !primes.isEmpty() ? i - primes.getLast() : 0;
                if ((i < 1_000)
                        || (i < 50_000 && spacing > 1000)
                        || (i < 100_000 && spacing > 5_000)
                        || (i < 1_000_000 && spacing > 50_000)
                        || (i < 30_000_000 && spacing > 500_000)
                        || (i < 99_000_000 && spacing > 1_000_000)
                        || (spacing > 10_000_000))
                {
                    primes.add(i);
                    System.out.print(i + " ");
                    System.out.flush();
                    if (primes.size() % 10 == 0)
                    {
                        System.out.println();
                    }
                }
            }
        }
        System.out.println(primes.size() + " primes:");
        for (var prime : primes)
        {
            System.out.print(Longs.commaSeparated(prime).replaceAll(",", "_") + ", ");
        }
    }

    /**
     * @return A prime allocation size greater than the given size
     */
    public static long primeAllocationSize(long size)
    {
        for (var allocationSize : SIZES)
        {
            if (size <= allocationSize)
            {
                return allocationSize;
            }
        }
        fail("No prime allocation size greater than " + size);
        return -1;
    }

    /**
     * @return The smallest "prime power of two" that is larger than the given value. A "prime power of two" is
     * basically the next prime after a power of two.
     */
    public static int primePowerOfTwo(int value)
    {
        if (value < primePowersOfTwo[0])
        {
            return primePowersOfTwo[0];
        }
        for (var i = 0; i < primePowersOfTwo.length - 1; i++)
        {
            if (value >= primePowersOfTwo[i] && value < primePowersOfTwo[i + 1])
            {
                return primePowersOfTwo[i + 1];
            }
        }
        return primePowersOfTwo[primePowersOfTwo.length - 1] * 2 - 1;
    }
}
