package Models

import Services.DatabaseService
import akka.actor.{Actor, ActorLogging, Props}

class LinkBillerActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {

    case (userName: String, billers: List[Biller]) =>
      val acNo = database.getAccountNo(userName)
      database.addBiller(acNo, billers)
      log.info(s"billers linked to account no : $acNo")
      sender() ! "linked"

    case _ => log.info("invalid details, cannot link billers")
  }
}

object LinkBillerActor {
  def props(database: DatabaseService): Props = Props(classOf[LinkBillerActor], database)
}

