package checkout

object Checkout {
  val applePrice = 60
  val orangePrice = 25

  sealed trait Item
  object Item {
    case object Apple extends Item
    case object Orange extends Item
  }

  def price(basket: List[Item]): Int = {
    val apples = basket.count(_ == Item.Apple)
    val oranges = basket.count(_ == Item.Orange)
    (apples*applePrice)+(oranges*orangePrice)
  }
}
