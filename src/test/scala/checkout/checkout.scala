package checkout

import scala.util.Random

import org.scalacheck.Gen
import org.scalatest._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import Checkout._
import Item._

class CheckoutSpec extends FlatSpec with ScalaCheckDrivenPropertyChecks with MustMatchers {
  def itemsGen: Gen[Int] = Gen.chooseNum(0, 20)

  "Checkout" should "price the example basket correctly" in {
    assert(Checkout.price(List(Apple, Apple, Orange, Apple)) == 205)
  }

  it should "price a basket of n apples as n*applePrice" in {
    forAll (itemsGen) { n =>
      Checkout.price(List.fill(n)(Apple)) must be (n*applePrice)
    }
  }

  it should "price a basket of m oranges as m*orangePrice" in {
    forAll (itemsGen) { m =>
      Checkout.price(List.fill(m)(Orange)) must be (m*orangePrice)
    }
  }

  it should "price a basket of n apples and m oranges as the sum the of prices of separate baskets" in {
    forAll (itemsGen, itemsGen) { (n, m) =>
      val apples = List.fill(n)(Apple)
      val oranges = List.fill(m)(Orange)
      val applesPrice = Checkout.price(apples)
      val orangesPrice = Checkout.price(oranges)
      val mixedBasket = Random.shuffle(apples ++ oranges)
      Checkout.price(mixedBasket) must be (applesPrice+orangesPrice)
    }
  }
}
