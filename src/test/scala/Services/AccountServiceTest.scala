package Services

import Models.{Account, Biller, User}
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestActor, TestKit, TestProbe}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuiteLike, BeforeAndAfterAll}

class AccountServiceTest extends TestKit(ActorSystem("test-system")) with AsyncFunSuiteLike with MockitoSugar with BeforeAndAfterAll {

  val accService = new AccountService()
  val name = "sajit"
  val address = "lko"
  val userName = "sajit123"
  val amount = 9000

  import accService._

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testCreateAccount") {

    val user = User(name, address, userName, amount)
    val userList = List(user)
    val probe = TestProbe()

    probe.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case `user` => Account(1, name, address, userName, amount)
      }
      sender ! resturnMsg
      TestActor.KeepRunning
    })

    createAccount(userList, probe.ref).map(res =>
      assert(res == List(Account(1, name, address, userName, amount))
      ))
  }

  test("testLinkBiller") {
    val userNameList = List(userName)
    val category = "phone"
    val biller = List(Biller("phone", name, 1, userName, amount, 0, 0, 0))
    val billers = List(biller)

    val probe = TestProbe()
    probe.setAutoPilot((sender: ActorRef, msg: Any) => {
      val resturnMsg = msg match {
        case (`userName`, `biller`) => "linked"
      }
      sender ! resturnMsg
      TestActor.KeepRunning
    })

    linkBiller(userNameList,billers,probe.ref).map(res => assert(res==List("linked")))

  }

}
