package Models

import Services.DatabaseService
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.collection.mutable

class SalaryDepositActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val database: DatabaseService = mock[DatabaseService]
  val name = "sajit"
  val address = "lko"
  val userName = "sajit123"

  val amount = 9000
  val account = Account(1, name, address, userName, amount)
  val zero = 0
  val one = 1
  val two =2
  val three = 3
  val four = 4
  val categoryList = List("phone", "electricity", "internet", "food", "car")

  when(database.updateBalance(account.accountNo, amount)).thenReturn(mutable.Map(userName -> account.copy(initialAmount = account.initialAmount + amount)))
  when(database.getAccount(account.accountNo)).thenReturn(account.copy(initialAmount = account.initialAmount + amount))
  when(database.updateBalance(account.accountNo, 18000 - amount / 10))
    .thenReturn(mutable.Map(userName -> account))

  test("testing receive of SalaryDepositActor ") {
    val biller = List(Biller(categoryList(zero), name, one, userName, amount / 10, zero, zero, zero),
      Biller(categoryList(one), name, one, userName, amount / 10, zero, zero, zero),
      Biller(categoryList(two), name, one, userName, amount / 10, zero, zero, zero),
      Biller(categoryList(three), name, one, userName, amount / 10, zero, zero, zero),
      Biller(categoryList(four), name, one, userName, amount / 10, zero, zero, zero)
    )

    val ref = system.actorOf(SalaryDepositActor.props(database))
    ref ! (account, biller, amount)
    expectMsg("success")
  }


  test("testing receive of SalaryDepositActor with biller amount greater than salary ") {
    val biller = List(Biller(categoryList(zero), name, one, userName, 3*amount, zero, zero, zero),
      Biller(categoryList(one), name, one, userName,3*amount , zero, zero, zero),
      Biller(categoryList(two), name, one, userName, 3*amount, zero, zero, zero),
      Biller(categoryList(three), name, one, userName, 3*amount, zero, zero, zero),
      Biller(categoryList(four), name, one, userName, 3*amount, zero, zero, zero)
    )

    val ref = system.actorOf(SalaryDepositActor.props(database))
    ref ! (account, biller, amount)
    expectMsg("success")
  }
}


