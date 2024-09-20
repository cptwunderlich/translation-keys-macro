//> using scala 3.3.3
//> using file Macro.scala
//> using file TranslationKey.scala
//> using dep org.scalacheck::scalacheck:1.18.1

import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen}

trait Marker
object Container:
  val key0: TranslationKey0[Marker] = TranslationKey0("key0")
  val key1: TranslationKey1[Marker, Int] = TranslationKey1("key1"): i1 =>
    Map("i1" -> i1)
  val key2: TranslationKey2[Marker, Int, String] = TranslationKey2("key2"):
    (i1, i2) => Map("i1" -> i1, "i2" -> i2)

class TranslationsTest[A]:
  def testTranslationKey(key: TranslationKey0[A]): Unit =
    testKeyInternal(key.value)(key()(_))

  def testTranslationKey[I1: Arbitrary](key: TranslationKey1[A, I1]): Unit =
    testKeyInternal(key.value)(key(random[I1])(_))

  def testTranslationKey[I1: Arbitrary, I2: Arbitrary](
      key: TranslationKey2[A, I1, I2]
  ): Unit =
    testKeyInternal(key.value)(key(random[I1], random[I2])(_))

  def testTranslationKey[I1: Arbitrary, I2: Arbitrary, I3: Arbitrary](
      key: TranslationKey3[A, I1, I2, I3]
  ): Unit =
    testKeyInternal(key.value)(key(random[I1], random[I2], random[I3])(_))

  def testTranslationKey[
      I1: Arbitrary,
      I2: Arbitrary,
      I3: Arbitrary,
      I4: Arbitrary
  ](
      key: TranslationKey4[A, I1, I2, I3, I4]
  ): Unit =
    testKeyInternal(key.value)(
      key(random[I1], random[I2], random[I3], random[I4])(_)
    )

  inline def testAllTranslationKeys[C](container: C): Unit =
    ${ macroImpl[A, C]('this, 'container) }

  private def testKeyInternal(keyName: String)(
      f: TranslationBundle[A] => String
  ): Unit =
    println(s"Test $keyName: ${f(new TranslationBundle[A])}")

  private lazy val seed: Seed = Seed((new scala.util.Random).nextLong)

  def random[T](implicit a: Arbitrary[T]) =
    Arbitrary.arbitrary[T].pureApply(Gen.Parameters.default, seed)

@main def main(): Unit =
  val tests = new TranslationsTest[Marker]
  tests.testAllTranslationKeys(Container)
