object TranslationKey:

  def apply[B](key: String): TranslationKey0[B] =
    TranslationKey0(key)

  def apply[B, I1](key: String)(
      f: I1 => Map[String, Any]
  ): TranslationKey1[B, I1] =
    TranslationKey1(key)(f)

  def apply[B, I1, I2](key: String)(
      f: (I1, I2) => Map[String, Any]
  ): TranslationKey2[B, I1, I2] =
    TranslationKey2(key)(f)

  def apply[B, I1, I2, I3](key: String)(
      f: (I1, I2, I3) => Map[String, Any]
  ): TranslationKey3[B, I1, I2, I3] =
    TranslationKey3(key)(f)

  def apply[B, I1, I2, I3, I4](key: String)(
      f: (I1, I2, I3, I4) => Map[String, Any]
  ): TranslationKey4[B, I1, I2, I3, I4] =
    TranslationKey4(key)(f)

object TranslationKey0:
  def apply[B](key: String): TranslationKey0[B] =
    new TranslationKey0[B]:
      override val value: String = key

object TranslationKey1:
  def apply[B, I1](key: String)(
      f: I1 => Map[String, Any]
  ): TranslationKey1[B, I1] =
    new TranslationKey1[B, I1]:
      override val value: String = key
      override def parameters(i1: I1): Map[String, Any] = f(i1)

object TranslationKey2:
  def apply[B, I1, I2](key: String)(
      f: (I1, I2) => Map[String, Any]
  ): TranslationKey2[B, I1, I2] =
    new TranslationKey2[B, I1, I2]:
      override val value: String = key
      override def parameters(i1: I1, i2: I2): Map[String, Any] = f(i1, i2)

object TranslationKey3:
  def apply[B, I1, I2, I3](key: String)(
      f: (I1, I2, I3) => Map[String, Any]
  ): TranslationKey3[B, I1, I2, I3] =
    new TranslationKey3[B, I1, I2, I3]:
      override val value: String = key
      override def parameters(i1: I1, i2: I2, i3: I3): Map[String, Any] =
        f(i1, i2, i3)

object TranslationKey4:
  def apply[B, I1, I2, I3, I4](key: String)(
      f: (I1, I2, I3, I4) => Map[String, Any]
  ): TranslationKey4[B, I1, I2, I3, I4] =
    new TranslationKey4[B, I1, I2, I3, I4]:
      override def value: String = key
      override def parameters(
          i1: I1,
          i2: I2,
          i3: I3,
          i4: I4
      ): Map[String, Any] = f(i1, i2, i3, i4)

trait TranslationKey:
  def value: String

trait TranslationKey0[B] extends TranslationKey:
  def apply()(implicit t: TranslationBundle[B]): String = t(this)

trait TranslationKey1[B, I1] extends TranslationKey:
  def parameters(i1: I1): Map[String, Any]
  def apply(i1: I1)(implicit t: TranslationBundle[B]): String = t(this, i1)

trait TranslationKey2[B, I1, I2] extends TranslationKey:
  def parameters(i1: I1, i2: I2): Map[String, Any]
  def apply(i1: I1, i2: I2)(implicit t: TranslationBundle[B]): String =
    t(this, i1, i2)

trait TranslationKey3[B, I1, I2, I3] extends TranslationKey:
  def parameters(i1: I1, i2: I2, i3: I3): Map[String, Any]
  def apply(i1: I1, i2: I2, i3: I3)(implicit t: TranslationBundle[B]): String =
    t(this, i1, i2, i3)

trait TranslationKey4[B, I1, I2, I3, I4] extends TranslationKey:
  def parameters(i1: I1, i2: I2, i3: I3, i4: I4): Map[String, Any]
  def apply(i1: I1, i2: I2, i3: I3, i4: I4)(implicit
      t: TranslationBundle[B]
  ): String = t(this, i1, i2, i3, i4)

class TranslationBundle[T]:
  def apply(key: TranslationKey0[T]): String = key.value
  def apply[I1](key: TranslationKey1[T, I1], i1: I1): String =
    s"${key.value}(${key.parameters(i1)})"
  def apply[I1, I2](key: TranslationKey2[T, I1, I2], i1: I1, i2: I2): String =
    s"${key.value}(${key.parameters(i1, i2)})"
  def apply[I1, I2, I3](
      key: TranslationKey3[T, I1, I2, I3],
      i1: I1,
      i2: I2,
      i3: I3
  ): String =
    s"${key.value}(${key.parameters(i1, i2, i3)})"
  def apply[I1, I2, I3, I4](
      key: TranslationKey4[T, I1, I2, I3, I4],
      i1: I1,
      i2: I2,
      i3: I3,
      i4: I4
  ): String =
    s"${key.value}(${key.parameters(i1, i2, i3, i4)})"
