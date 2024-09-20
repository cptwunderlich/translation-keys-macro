import scala.quoted.*

def macroImpl[T, C](
    testsExpr: Expr[TranslationsTest[T]],
    container: Expr[C]
)(using quotes: Quotes, tTpe: Type[T], cTpe: Type[C]): Expr[Unit] =
  import quotes.reflect.*

  val tkTypes = List(
    TypeRepr.of[TranslationKey0[T]],
    TypeRepr.of[TranslationKey1[T, _]],
    TypeRepr.of[TranslationKey2[T, _, _]],
    TypeRepr.of[TranslationKey3[T, _, _, _]],
    TypeRepr.of[TranslationKey4[T, _, _, _, _]]
  )

  def isTranslationsKey(t: TypeRepr): Boolean =
    tkTypes.exists(t <:< _)

  val containerType: TypeRepr = TypeRepr.of[C]
  val containerTerm: Term = container.asTerm

  // Map over all member fields of container type,
  // searching for all translation keys.
  val calls = containerType.typeSymbol.fieldMembers.map { symbol =>
    symbol.tree match
      // Val definition is `val name: tpt = _`
      case ValDef(name, tpt, _) if isTranslationsKey(tpt.tpe) => {
        val field: Term = Select(containerTerm, symbol) // container.`name`
        val interpolationTArgs = tpt.tpe.typeArgs.tail // I1, I2, I3, ...

        // Resolve the needed `testTranslationKey` overload
        val callOverload = Select.overloaded(
          testsExpr.asTerm,
          "testTranslationKey",
          interpolationTArgs,
          List(field)
        )

        // Resolve givens/implicit for Arbitrary value generation
        val arbitraryTpe =
          TypeIdent(Symbol.requiredClass("org.scalacheck.Arbitrary")).tpe
        val implicits = interpolationTArgs.map { t =>
          Implicits.search(arbitraryTpe.appliedTo(t)) match
            case success: ImplicitSearchSuccess =>
              success.tree
            case failure =>
              report.errorAndAbort(s"Could not find implicit: $failure")
        }
        val res =
          if (implicits.nonEmpty) Apply(callOverload, implicits)
          else callOverload

        Some(res.asExpr)
      }
      case _ => None
  }.flatten

  // Block expression with all calls
  Expr.block(calls, '{ () })
