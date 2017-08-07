package Services

import Models._
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

class AccountService{

  def createAccount(users: List[User], accGeneratorRef: ActorRef): Future[List[Account]] = {

    implicit val timeout = Timeout(10 seconds)

    val accounts = for (user <- users) yield
      (accGeneratorRef ? user).mapTo[Account]

    Future.sequence(accounts)

  }

  def linkBiller(userNames: List[String], billers: List[List[Biller]], linkBillerActorRef: ActorRef): Future[List[String]] = {

    val zippedList = userNames zip billers

    implicit val timeout = Timeout(10 seconds)

    val result = for ((username, biller) <- zippedList) yield
      (linkBillerActorRef ? (username, biller)).mapTo[String]

    Future.sequence(result)
    }

}


