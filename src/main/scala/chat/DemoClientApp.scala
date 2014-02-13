package sample.cluster.simple

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.contrib.pattern.ClusterClient

case class Start
object DemoClientApp {
  def main(args: Array[String]): Unit = {
    if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", args(0))

    val system = ActorSystem("ClientSystem") // different from ServerApp's name: ServerSystem
    val clientActor = system.actorOf(Props[ClientActor])
    clientActor ! Start()
  }

}

class ClientActor extends Actor {
  var clusterClient: ActorRef = null
  def receive = {
    case _: Start =>
      val initial = Set(context.actorSelection("akka.tcp://ServerSystem@127.0.0.1:2551/user/receptionist"))
      this.clusterClient = context.actorOf(ClusterClient.props(initial))

      context become ready
      self ! 0
  }

  def ready: Receive = {
    case i: Integer =>
      clusterClient ! ClusterClient.SendToAll("/user/service_actor", " to-all: " + i)
      clusterClient ! ClusterClient.Send("/user/service_actor", " to-one:" + i, false)
      Thread.sleep(1000)
      self ! (i + 1)

    case x: String =>
      println(x)
  }
}

