package Models

import Services.DatabaseService
import akka.actor.{Actor, ActorLogging, Props}
import akka.dispatch.{BoundedMessageQueueSemantics, RequiresMessageQueue}

class AccountGeneratorActor(database:DatabaseService) extends Actor with ActorLogging with RequiresMessageQueue[BoundedMessageQueueSemantics] {

  var accNo = 0

  override def receive: Receive = {

    case User(name, address, userName, deposit) if !database.containsAccount(userName) =>
      accNo += 1
      database.addAccount(Account(accNo, name, address, userName, deposit))
      log.info(s"Models.Account with accNo=$accNo created")
      sender() ! Account(accNo, name, address, userName, deposit)

    case User(_, _, userName, _) if database.containsAccount(userName) =>
      sender() ! Nil
      log.info("Username must be unique, cannot create account")

    case _ => sender() ! Nil ;log.info("invalid user details")
  }
}

object AccountGeneratorActor {

  def props(database:DatabaseService): Props = Props(classOf[AccountGeneratorActor],database)
}
