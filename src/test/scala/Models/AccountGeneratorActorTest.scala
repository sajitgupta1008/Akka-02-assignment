package Models

import Services.DatabaseService
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.collection.mutable

class AccountGeneratorActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
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

  test("testing receive of Models.AccountGeneratorActor by passing User") {
    val ref: ActorRef = system.actorOf(AccountGeneratorActor.props(database))
    when(database.addAccount(account)).thenReturn(mutable.Map(userName -> account))
    when(database.containsAccount(userName)).thenReturn(false)
    ref ! User(name, address, userName, amount)
    expectMsg(Account(1, name, address, userName, amount))
  }

  test("testing receive of Models.AccountGeneratorActor by passing User data that exists") {
    val ref: ActorRef = system.actorOf(AccountGeneratorActor.props(database))
    when(database.containsAccount(userName)).thenReturn(true)
    ref ! User(name, address, userName, amount)
    expectMsg(Nil)
  }
  test("testing receive of Models.AccountGeneratorActor by passing random data") {
    val ref: ActorRef = system.actorOf(AccountGeneratorActor.props(database))
    ref ! 213
    expectMsg(Nil)
  }

}
