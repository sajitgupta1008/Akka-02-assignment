package Models

import Services.DatabaseService
import akka.actor.ActorSystem
import org.mockito.Mockito._
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.collection.mutable

class LinkBillerActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  val database: DatabaseService = mock[DatabaseService]

  test("testing receive of LinkeBillerActor"){
    val username = "sajit"
    val acNo = 1
    val biller = List(Biller(username,username,acNo,username,acNo,0,0,0))
    when(database.getAccountNo(username)).thenReturn(acNo)
    when(database.addBiller(acNo,biller)).thenReturn(mutable.Map(acNo->biller))

    val actorRef = system.actorOf(LinkBillerActor.props(database))
    actorRef ! (username, biller)
    expectMsg("linked")
  }
}
