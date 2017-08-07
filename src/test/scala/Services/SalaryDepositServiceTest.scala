package Services

import Models.{Account, Biller}
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestActor, TestKit, TestProbe}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuiteLike, BeforeAndAfterAll}

import scala.collection.mutable

class SalaryDepositServiceTest extends TestKit(ActorSystem("test-system")) with AsyncFunSuiteLike with MockitoSugar with BeforeAndAfterAll {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val employeeMap = Map(1 -> 9000)
  val database: DatabaseService = mock[DatabaseService]
  val name = "sajit"
  val address = "lko"
  val userName = "sajit123"
  val amount = 9000
  val account = Account(1, name, address, userName, amount)
  val biller = List(Biller("phone", name, 1, userName, amount, 0, 0, 0))


  test("testDepositSalary") {

    val probe = TestProbe()
    probe.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case (`account`, `biller`, `amount`) => "success"
      }
      sender ! resturnMsg
      TestActor.KeepRunning
    })

    when(database.getAccount(1)).thenReturn(account)
    when(database.getBillers).thenReturn(mutable.Map(1->biller))

    new SalaryDepositService(database).depositSalary(employeeMap, probe.ref).map(res =>
      assert(res == List("success")))
  }

}
