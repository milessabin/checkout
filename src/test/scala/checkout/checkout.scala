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

  def reduceGen: Gen[Int] = Gen.chooseNum(1, 10)

  it should "price a basket of m apples with a n for d discount as a basket of (m/n)*d + (m%2) apples with no discount" in {
    forAll (itemsGen, reduceGen, reduceGen) { (m, r0, r1) =>
      val n = r0 + r1
      val d = r1
      val r = ((m / n) * d) + (m % n)
      val fullPrice = Checkout.price(List.fill(r)(Apple))
      Checkout.price(List.fill(m)(Apple), appleReduction = Reduction(n, d)) must be (fullPrice)
    }
  }

  it should "price a basket of m oranges with a n for d discount as a basket of (m/n)*d + (m%2) oranges with no discount" in {
    forAll (itemsGen, reduceGen, reduceGen) { (m, r0, r1) =>
      val n = r0 + r1
      val d = r1
      val r = ((m / n) * d) + (m % n)
      val fullPrice = Checkout.price(List.fill(r)(Orange))
      Checkout.price(List.fill(m)(Orange), orangeReduction = Reduction(n, d)) must be (fullPrice)
    }
  }

  it should "price a mixed discounted basket as the sum of the prices of separate discounted baskets" in {
    forAll (itemsGen, reduceGen, reduceGen, itemsGen, reduceGen, reduceGen) { (n, r0a, r1a, m, r0o, r1o) =>
      val appleReduction = Reduction(r0a + r1a, r1a)
      val orangeReduction = Reduction(r0o + r1o, r1o)

      val apples = List.fill(n)(Apple)
      val oranges = List.fill(m)(Orange)

      val applesDiscounted = Checkout.price(apples, appleReduction = appleReduction)
      val orangesDiscounted = Checkout.price(oranges, orangeReduction = orangeReduction)

      val mixedBasket = Random.shuffle(apples ++ oranges)

      Checkout.price(mixedBasket, appleReduction, orangeReduction) must be (applesDiscounted + orangesDiscounted)
    }
  }
}
