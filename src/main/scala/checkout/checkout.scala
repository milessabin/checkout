package checkout

object Checkout {
  val applePrice = 60
  val orangePrice = 25

  sealed trait Item
  object Item {
    case object Apple extends Item
    case object Orange extends Item
  }

  case class Reduction(n: Int, d: Int) {
    def apply(items: Int) = ((items / n) * d) + (items % n)
  }

  object Reduction {
    val None = Reduction(1, 1)
  }

  def price(basket: List[Item], appleReduction: Reduction = Reduction.None, orangeReduction: Reduction = Reduction.None): Int = {
    val apples = basket.count(_ == Item.Apple)
    val oranges = basket.count(_ == Item.Orange)
    (appleReduction(apples)*applePrice)+(orangeReduction(oranges)*orangePrice)
  }
}
